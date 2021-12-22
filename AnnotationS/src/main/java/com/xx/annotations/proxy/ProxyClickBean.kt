package com.xx.annotations.proxy

import android.app.Activity
import android.view.View
import java.lang.reflect.Method

class ProxyClickBea{
    var time: Long = 0
    lateinit var o: Any
    lateinit var m: Method
    lateinit var v: View
}