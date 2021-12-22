package com.xx.annotations

import android.view.View
import java.lang.annotation.Inherited

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)//在method|Field添加注解
@Retention(AnnotationRetention.RUNTIME)//存在运行时
@EventType(listenerSetter = "setOnClickListener",listenerType = View.OnClickListener::class)
@Inherited//表示可以被继承
@MustBeDocumented//可以描述在javadoc中
annotation class OnClick(vararg val value: Int)
//点击事件