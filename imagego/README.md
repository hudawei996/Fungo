
# ImageGo(二次图片加载框架)
[![](https://www.jitpack.io/v/PingerOne/ImageGo.svg)](https://www.jitpack.io/#PingerOne/ImageGo)


> 使用Kotlin二次封装图片加载框架ImageGo。提供多种API，支持随时替换底层加载框架Glide、Picasso和Fresco，支持加载带边框圆形、多方位圆角、高斯模糊等特效图片，支持使用进度条加载，全局GIF加载，保存图片到本地，获取图片缓存大小，清除图片缓存等多种功能。
>
> 这个库是公司的业务衍生出来的，项目一直都在使用，遇到的问题基本都修复了，大部分的图片加载模式应该是可以满足的。这个库使用的是kotlin代码编写的，代码量不大。

##  诞生缘由
在项目开发中，大部分开发者都会选择使用Glide，Picasso或者Fresco图片加载框架来加载图片。博主也不例外，选择的是Glide，虽然在使用的过程中遇到一些问题，但是总的来说Glide还是很Skr的。

随着业务发展，产品提出了加载webp动图的需求，很遗憾的是，Glide并不支持加载webp动图，但是Fresco却可以。没办法，那就只能将Glide替换成Fresco，令人崩溃的是，Fresco使用的并不是系统的ImageView，而是自定义的SimpleDraweeView，而且API也不一样。替换起来工作量浩大，令人头疼。

之前使用Glide是有考虑到以后换图片加载框架的可能，所以也使用了ImageManager来对Glide的加载进行管理，但是ImageManager内的加载实现是写死Glide加载的，并没有抽取接口，没办法只能重新二次封装一个图片加载框架，来支持Glide,Picasso和Fresco框架随时替换。

## 实现原理

#### 切换底层加载框架
为了实现项目替换图片加载框架的需求，这里将常用的方法抽取到顶层接口ImageStrategy中，通过策略设计模式，设置具体的实现类，最终由实现类去完成加载图片。一般在项目的Application中去设置图片的加载策略就可以使用了，如果没有设置，默认使用的是Glide加载策略。


#### 图片加载属性配置
为了能和Glide等图片加载框架一样，非常简单的配置图片加载相关的属性，这里提供了ImageOptions这个类来进行配置，一些常用的比如占位图，是否Gif图，内存缓存策略，磁盘缓存策略等使用默认配置就好了。为了能更加方便的直接使用，这里也将平时用得多的抽取成了API的方式，如果需要更多的功能可以手动去配置ImageOptionsle。


#### 实现圆形圆角高斯模糊特效图片
调用loadCircle、loadBlur、loadRound等方法可以加载各种特效图片，这些API其实是主动设置ImageOptions中的isCircle、isRound、isBlur等属性为true，当在策略的具体实现类中时，会对这个属性一一进行判断，如果匹配到了，就会调用相关的API实现这些特效。这里提供的特效参考自[glide-transformations](https://github.com/wasabeef/glide-transformations)。




## 使用方法

1. 在项目根目录的build.gradle文件中添加jitpack仓库

        allprojects {
            repositories {
                google()
                jcenter()
                maven { url 'https://jitpack.io' }
            }
        }

2. 在application的build.gradle文件中引入仓库依赖

        dependencies {
            implementation 'com.github.PingerOne:ImageGo:2.0.1'
        }

3. 在Application中设置图片加载策略，默认使用Glide加载

        ImageGoEngine
                 .setDebug(true)   // 开发模式
                 .setAutoGif(true) // 是否自动加载GIF，开启后loadImage方法会自动加载GIF图片
                 .setImageStrategy(GlideImageStrategy())  // 图片加载策略


## 代码示例
* 加载全局图片

        loadImage(Any, View)
        loadImage(Any, View, OnImageListener)

* 加载GIF图片

        loadGif(Any, View)
        loadGif(Any, View, OnImageListener)

* 加载Bitmap图片

        loadBitmap(Context,Any,OnImageListener)

* 加载进度条图片

        loadProgress(String,View,OnProgressListener)

* 加载圆形图片

        loadCircle(String, View)
        loadCircle(String, View, borderWidth, borderColor)

* 加载圆角图片

        loadRound(String, View)
        loadRound(String, View, roundRadius)
        loadRound(String, View, roundRadius, roundType)

* 加载高斯模糊图片

        loadBlur(String, View)
        loadBlur(String, View, blurRadius)

* 保存图片

        saveImage(Context, Any)
        saveImage(Context, Any, OnImageSaveListener)



## 常用API
| Name | Description |
| :- | :- |
| loadImage | 加载全局图片 |
| loadGif   | 加载GIF图片 |
| loadProgress |加载进度条图片|
| loadBitmap   |加载生成Bitmap图片|
| loadCircle   |加载圆形图片|
| loadRound    |加载圆角图片|
| loadBlur |加载高斯模糊图片|
| saveImage |保存图片到本地|
| clearImageDiskCache |清理磁盘中的图片缓存|
| clearImageMemoryCache |清理内存中的图片缓存|
| getCacheSize |获取图片缓存的大小|
| resumeRequests |开始所有的图片请求|
| pauseRequests |暂停所有的图片请求|
| downloadImage |缓存图片到本地|




---
> 欢迎大家访问我的[简书](http://www.jianshu.com/u/64f479a1cef7)，[博客](http://pingerone.com/)和[GitHub](https://github.com/PingerOne)。


