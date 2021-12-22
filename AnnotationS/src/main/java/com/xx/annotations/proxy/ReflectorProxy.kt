package com.xx.annotations.proxy

import android.app.Activity
import android.view.View
import com.xx.annotations.OnClick
import com.xx.annotations.OnLongClick
import java.lang.Exception
import java.lang.reflect.Method
import java.lang.reflect.Proxy

object ReflectorProxy {
    private var viewCacheParams: HashMap<Method, ProxyClickBea> = HashMap()
    private var onClickTime: Long = 0

    /**
     * 利用动态代理，hook setOnClickListener接口
     * 1.获取当前类的class 用于hook某个方法
     * 2.获取view的控件id,
     * 3.通过反射方法找到View.setOnClickListener的方法
     * 4.通过反射方法找到当前类的hook方法
     * 5.创建动态代理，Proxy  代理类的classLoader，代理类的名称,多参， invoke
     *
     * 6.使用setOnClickListener(当前view,动态代理)
     * 7.动态代理执行,目标method.invoke(当前类，参数)
     * @param o 当前类的对象
     * @param view 需要绑定setOnClickListener()
     * @param clickMethod 目标代理执行方法
     */
    fun viewClickReflectProxy(o: Any, view: View, clickMethod: String) {
        val clazz = o.javaClass//获取当前类的class对象
        try {
            val setOnClickListener =
                view.javaClass.getMethod("setOnClickListener", View.OnClickListener::class.java)
            val clickView = clazz.getMethod(clickMethod, View::class.java)
            //代理目标类的方法,需要用目标的classLoader 加载, 并且指明代理的接口
            val proxy = Proxy.newProxyInstance(
                View.OnClickListener::class.java.classLoader, arrayOf<Class<*>>(
                    View.OnClickListener::class.java
                )
            ) {
                //has type android.view.View, got java.lang.Object[] 这里要注意clickView.invoke(o, *args) `*args`这个参数一定不能少了*否者会报错
                //这里的`*`的作用是吧数组拆分成可变参，所以我们在使用可变参的时候传入的是数组就会报错哦，一定要转化成可变参
                    proxy, method, args ->
                clickView.invoke(o, *args)
            }
            setOnClickListener.invoke(view, proxy)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clickProxy(o: Any, onClickTime: Long? = 100) {
        if (onClickTime != null) {
            this.onClickTime = onClickTime
        }
        val clazz = o.javaClass
        val methods = clazz.declaredMethods//获取所有类的方法
        methods.forEach { m ->
            val annotation = m.annotations//获取方法上的所有注解
            if (o !is Activity) {
                return
            }
            proxyClick(o, m, annotation)
        }
    }

    /**
     * 通过动态代理给注解添加点击监听事件
     */
    private fun proxyClick(o: Activity, m: Method, annotation: Array<Annotation>) {
        var methodType = "setOnClickListener"
        annotation.forEach {
            when (it.annotationClass.java) {
                OnClick::class.java -> {
                    //OnClick注解获取
                    methodType = "setOnClickListener"
                    val clazz = View.OnClickListener::class.java
                    val annotationValues = m.getAnnotation(OnClick::class.java)
                    parseMethodAnnotation(annotationValues?.value, o, methodType, m, clazz)
                }
                OnLongClick::class.java -> {
                    //OnLongClick注解获取
                    methodType = "setOnLongClickListener"
                    val clazz = View.OnLongClickListener::class.java
                    val annotationValues = m.getAnnotation(OnLongClick::class.java)
                    parseMethodAnnotation(annotationValues?.value, o, methodType, m, clazz)
                }
            }
        }
    }

    private fun parseMethodAnnotation(
        annotationValues: IntArray?,
        o: Activity,
        methodType: String,
        m: Method,
        clazz: Class<*>
    ) {
        try {
            if (annotationValues == null) {
                Exception("annotation Fields is null ")
            }
            synchronized(this) {

                val proxy = Proxy.newProxyInstance(
                    clazz.classLoader,
                    arrayOf<Class<*>>(clazz)
                ) { proxy, methods, args ->
                    if (viewCacheParams[m] == null) {
                        val proxyClickBean = ProxyClickBea()
                        proxyClickBean.o = o
                        proxyClickBean.m = m
                        proxyClickBean.time = System.currentTimeMillis()
                        viewCacheParams[m] = proxyClickBean
                        m.invoke(o, *args)
                    } else {
                        viewCacheParams[m]?.time?.let {
                            if (System.currentTimeMillis().minus(it) >= onClickTime) {
                                viewCacheParams[m]?.time = System.currentTimeMillis()
                                m.invoke(o, *args)
                            }
                        }
                    }
                }
                annotationValues?.forEach { id ->
                    val viewId = o.findViewById<View>(id)
                    val setonClickListener: Method = viewId.javaClass.getMethod(
                        methodType,
                        clazz
                    )
                    setonClickListener.invoke(viewId, proxy)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}