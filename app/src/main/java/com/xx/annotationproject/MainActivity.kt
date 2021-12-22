package com.xx.annotationproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.xx.annotations.AnnotationProxy
import com.xx.annotations.OnClick
import com.xx.annotations.OnLongClick
import com.xx.annotations.proxy.ReflectorProxy

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ReflectorProxy.clickProxy(this)
//        AnnotationProxy().clickProxy(this)
    }

    @OnClick(R.id.str)
    fun onClick(view: View) {
        Log.e("TAG", "=====>" + view.alpha)
    }


    @OnClick(R.id.str2)
    fun onLongClick(view: View) {
        Toast.makeText(this, "xxxxsssxxxxxx", Toast.LENGTH_SHORT).show()
    }
}