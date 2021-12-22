package com.xx.annotations

import java.lang.annotation.ElementType
import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EventType(val listenerSetter: String,val listenerType: KClass<*>)
