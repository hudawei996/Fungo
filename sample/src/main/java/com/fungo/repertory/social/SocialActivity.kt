package com.fungo.repertory.social

import android.content.Intent
import android.graphics.Bitmap
import android.text.method.ScrollingMovementMethod
import android.view.View
import com.fungo.baselib.base.basic.BaseActivity
import com.fungo.imagego.ImageManager
import com.fungo.imagego.listener.OnImageListener
import com.fungo.repertory.R
import com.fungo.socialgo.share.SocialApi
import com.fungo.socialgo.share.config.PlatformType
import com.fungo.socialgo.share.listener.OnAuthListener
import com.fungo.socialgo.share.listener.OnShareListener
import com.fungo.socialgo.share.media.*
import kotlinx.android.synthetic.main.activity_social.*

/**
 * @author Pinger
 * @since 2018/4/30 20:48
 * 社会化组件
 */
class SocialActivity : BaseActivity() {

    private var platformType: PlatformType? = null
    private var shareMedia: IShareMedia? = null

    private var mImageUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1530450487&di=0adadc8f9b25f8f7176a4e79eca56580&src=http://img0.ph.126.net/HTy8QOnZk_jQ9T2wfOEvNA==/3141823690144359477.jpg"
    private var mShareUrl = "https://www.baidu.com/"


    override val layoutResID: Int
        get() = R.layout.activity_social

    override fun initView() {
        setToolBar(getString(R.string.social), true)
        tvConsole.movementMethod = ScrollingMovementMethod.getInstance()
    }

    override fun initEvent() {
        containerType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbTypeText -> {
                    shareMedia = ShareTextMedia()
                    (shareMedia as ShareTextMedia).text = getString(R.string.share_text)
                }
                R.id.rbTypeImage -> {
                    ImageManager.instance.loadBitmap(this, mImageUrl, object : OnImageListener {
                        override fun onSuccess(bitmap: Bitmap?) {
                            shareMedia = ShareImageMedia()
                            (shareMedia as ShareImageMedia).image = bitmap
                        }

                        override fun onFail(msg: String) {
                        }
                    })


                }
                R.id.rbTypeTextImage -> {
                    ImageManager.instance.loadBitmap(this, mImageUrl, object : OnImageListener {
                        override fun onSuccess(bitmap: Bitmap?) {
                            shareMedia = ShareTextImageMedia()
                            (shareMedia as ShareTextImageMedia).image = bitmap
                            (shareMedia as ShareTextImageMedia).text = getString(R.string.share_text)
                        }

                        override fun onFail(msg: String) {
                        }

                    })

                }

                R.id.rbTypeLink -> {
                    ImageManager.instance.loadBitmap(this, mImageUrl, object : OnImageListener {
                        override fun onSuccess(bitmap: Bitmap?) {
                            shareMedia = ShareWebMedia()
                            (shareMedia as ShareWebMedia).thumb = bitmap
                            (shareMedia as ShareWebMedia).description = getString(R.string.share_text)
                            (shareMedia as ShareWebMedia).webPageUrl = mShareUrl
                            (shareMedia as ShareWebMedia).title = getString(R.string.share_title)
                        }

                        override fun onFail(msg: String) {
                        }

                    })

                }
            }
        }

        containerPlatform.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbPlatformQQ -> platformType = PlatformType.QQ
                R.id.rbPlatformQzon -> platformType = PlatformType.QZONE
                R.id.rbPlatformWx -> platformType = PlatformType.WEIXIN
                R.id.rbPlatformWxFriend -> platformType = PlatformType.WEIXIN_CIRCLE
                R.id.rbPlatformSina -> platformType = PlatformType.SINA_WB
            }
        }
    }

    override fun initData() {
        rbTypeText.isChecked = true
        rbPlatformQQ.isChecked = true
    }

    fun onQQLogin(view: View) {
        showProgress()
        SocialApi.get(this).doOauthVerify(this, PlatformType.QQ, object : OnAuthListener {
            override fun onComplete(platform_type: PlatformType?, map: MutableMap<String, String>?) {
                performLoginSuccess(map)
            }

            override fun onError(platform_type: PlatformType?, err_msg: String?) {
                dismissProgress()
                tvConsole.text = "QQ登录发生错误:$err_msg"
            }

            override fun onCancel(platform_type: PlatformType?) {
                dismissProgress()
                tvConsole.text = "QQ登录取消"
            }

        })
    }


    fun onWxLogin(view: View) {

    }

    fun onSinaLogin(view: View) {
        showProgress()
        SocialApi.get(this).doOauthVerify(this, PlatformType.SINA_WB, object : OnAuthListener {
            override fun onComplete(platform_type: PlatformType?, map: MutableMap<String, String>?) {
                performLoginSuccess(map)
            }

            override fun onError(platform_type: PlatformType?, err_msg: String?) {
                dismissProgress()
                tvConsole.text = "WB登录发生错误:$err_msg"
            }

            override fun onCancel(platform_type: PlatformType?) {
                dismissProgress()
                tvConsole.text = "WB登录取消"
            }

        })
    }

    fun onShare(view: View) {
        if (platformType == null || shareMedia == null) {
            tvConsole.text = "分享失败"
            return
        }

        showProgress()
        SocialApi.get(this).doShare(this, platformType, shareMedia, object : OnShareListener {
            override fun onComplete(platform_type: PlatformType?) {
                dismissProgress()
                tvConsole.text = "分享成功"
            }

            override fun onError(platform_type: PlatformType?, err_msg: String?) {
                dismissProgress()
                tvConsole.text = "分享错误:$err_msg"
            }

            override fun onCancel(platform_type: PlatformType?) {
                dismissProgress()
                tvConsole.text = "取消分享"
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        SocialApi.get(this).onActivityResult(requestCode, resultCode, data)
    }


    private fun performLoginSuccess(map: MutableMap<String, String>?) {
        dismissProgress()
        if (map == null || map.isEmpty()) {
            tvConsole.text = "数据为空"
            return
        }
        val builder = StringBuilder()

        for (key in map.keys) {
            builder.append("$key : ").append("${map[key]}").append("\n")
        }

        tvConsole.text = builder.toString()

    }
}