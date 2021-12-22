package com.xx.annotations

import android.view.View
import java.lang.annotation.Inherited

@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@EventType(listenerSetter = "setOnLongClickListener",listenerType = View.OnLongClickListener::class)
@Inherited
@MustBeDocumented
annotation class OnLongClick(vararg val value: Int)
//长按事件