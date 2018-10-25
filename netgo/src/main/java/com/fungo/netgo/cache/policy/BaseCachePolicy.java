package com.fungo.netgo.cache.policy;

import android.graphics.Bitmap;

import com.fungo.netgo.cache.CacheEntity;
import com.fungo.netgo.cache.CacheFlowable;
import com.fungo.netgo.cache.CacheManager;
import com.fungo.netgo.cache.CacheMode;
import com.fungo.netgo.request.base.Request;
import com.fungo.netgo.utils.HttpUtils;
import com.fungo.netgo.utils.RxUtils;

import org.reactivestreams.Publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Function;
import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * @author Pinger
 * @since 18-10-25 上午10:04
 */
public class BaseCachePolicy<T> implements CachePolicy<T> {

    protected Request<T, ? extends Request> mRequest;

    public BaseCachePolicy(Request<T, ? extends Request> request) {
        mRequest = request;
    }

    @Override
    public T requestSync() {
        return null;
    }


    /**
     * 发起异步请求
     */
    @Override
    public void requestAsync() {
    }


    /**
     * 构建缓存加载观察者
     */
    protected Flowable<CacheFlowable<T>> prepareCacheFlowable() {
        return Flowable.create(new FlowableOnSubscribe<CacheFlowable<T>>() {
            @Override
            public void subscribe(FlowableEmitter<CacheFlowable<T>> emitter) {
                if (!emitter.isCancelled()) {
                    CacheEntity<T> cacheEntity = prepareCache();
                    // TODO 缓存的回调
                    if (cacheEntity != null) {
                        System.out.println("-----------> 有缓存数据--------");
                        emitter.onNext(new CacheFlowable<>(true, cacheEntity.getData()));
                    } else {
                        System.out.println("-----------> 无缓存数据--------");
                    }
                    emitter.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER);
    }


    /**
     * 构建异步网络请求加载观察者
     */
    Flowable<CacheFlowable<T>> prepareAsyncRequestFlowable() {
        Flowable<Response<ResponseBody>> requestFlowable = null;
        switch (mRequest.getMethod()) {
            case GET:
                requestFlowable = mRequest.getAsync();
                break;
            case POST:
                requestFlowable = mRequest.postAsync();
                break;
        }

        Flowable<CacheFlowable<T>> resultFlowable = null;
        if (requestFlowable != null) {
            resultFlowable = requestFlowable
                    .flatMap(new Function<Response<ResponseBody>, Publisher<T>>() {
                        @Override
                        public Publisher<T> apply(Response<ResponseBody> response) throws Exception {

                            T t = mRequest.getCallBack().convertResponse(response.body());
                            System.out.println("-----------> 请求网络数据成功--------");

                            saveCache(response.headers(), t);
                            System.out.println("-----------> 保存缓存成功--------");

                            return Flowable.just(t);
                        }
                    })
                    .flatMap(RxUtils.<T>getCacheFunction());
        }

        return resultFlowable;
    }


    /**
     * 构建同步网络请求
     */
    CacheFlowable<T> prepareSyncRequest() {
        Response<ResponseBody> response = null;
        T t = null;
        try {
            switch (mRequest.getMethod()) {
                case GET:
                    response = mRequest.getSync();
                    break;
                case POST:
                    response = mRequest.postSync();
                    break;
            }

            t = mRequest.getCallBack().convertResponse(response.body());

            // 保存缓存
            saveCache(response.headers(), t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CacheFlowable<>(false, t);
    }

    /**
     * 加载缓存
     */
    @Override
    public CacheEntity<T> prepareCache() {
        //check the config of cache
        if (mRequest.getCacheKey() == null) {
            mRequest.cacheKey(HttpUtils.createUrlFromParams(mRequest.getUrl(), mRequest.getParams().getUrlParams()));
        }
        if (mRequest.getCacheMode() == null) {
            mRequest.cacheMode(CacheMode.NO_CACHE);
        }

        CacheMode cacheMode = mRequest.getCacheMode();
        CacheEntity<T> cacheEntity = null;
        if (cacheMode != CacheMode.NO_CACHE) {
            //noinspection unchecked
            cacheEntity = (CacheEntity<T>) CacheManager.getInstance().get(mRequest.getCacheKey());
            HttpUtils.addCacheHeaders(mRequest, cacheEntity, cacheMode);
            if (cacheEntity != null && cacheEntity.checkExpire(cacheMode, mRequest.getCacheTime(), System.currentTimeMillis())) {
                cacheEntity.setExpire(true);
            }
        }

        if (cacheEntity == null || cacheEntity.isExpire() || cacheEntity.getData() == null || cacheEntity.getResponseHeaders() == null) {
            cacheEntity = null;
        }
        return cacheEntity;
    }


    /**
     * 请求成功后根据缓存模式，更新缓存数据
     *
     * @param headers 响应头
     * @param data    响应数据
     */
    protected void saveCache(Headers headers, T data) {
        if (mRequest.getCacheMode() == CacheMode.NO_CACHE) return;    //不需要缓存,直接返回
        if (data instanceof Bitmap) return;             //Bitmap没有实现Serializable,不能缓存

        CacheEntity<T> cache = HttpUtils.createCacheEntity(headers, data, mRequest.getCacheMode(), mRequest.getCacheKey());
        if (cache == null) {
            //服务器不需要缓存，移除本地缓存
            CacheManager.getInstance().remove(mRequest.getCacheKey());
        } else {
            //缓存命中，更新缓存
            CacheManager.getInstance().replace(mRequest.getCacheKey(), cache);
        }
    }

}