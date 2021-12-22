package com.xx.annotations;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.xx.annotations.proxy.ProxyClickBea;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;

public class AnnotationProxy {
    public HashMap<Method, ProxyClickBea> viewCache = new HashMap<>();
    public long defaultTimeConfig = 0L;

    public void clickProxy(View o) {
        clickProxy(o, defaultTimeConfig);
    }

    public void clickProxy(Activity o) {
        clickProxy(o, defaultTimeConfig);
    }

    public <T> void clickProxy(T o, Long onClickTime) {
        Class<?> clazz = o.getClass();
        Method[] methods = clazz.getDeclaredMethods();//获取所有的注解方法
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();//方法上的所有注解
            method.setAccessible(true);
            parseAnnotation(o, method, annotations);
        }
    }

    private <T> void parseAnnotation(T o, Method method, Annotation[] annotations) {
        for (Annotation a : annotations) {
            Class<? extends Annotation> annotationType = a.annotationType();
            if (!annotationType.isAnnotationPresent(EventType.class))
                continue;


            if (a instanceof OnClick) {
                OnClick methodAnnotation = method.getAnnotation(OnClick.class);
                if (methodAnnotation == null) {
                    continue;
                }
                EventType eventType = annotationType.getAnnotation(EventType.class);
                if (eventType == null)
                    continue;
                methodProxy(methodAnnotation.value(), eventType, o, method);
            } else if (a instanceof OnLongClick) {
                OnLongClick methodAnnotation = method.getAnnotation(OnLongClick.class);
                if (methodAnnotation == null) {
                    continue;
                }
                EventType eventType = annotationType.getAnnotation(EventType.class);
                if (eventType == null)
                    continue;
                methodProxy(methodAnnotation.value(), eventType, o, method);
            }

        }
    }

    /**
     * 代理解析
     *
     * @param value
     * @param eventType
     * @param o
     * @param m
     */
    View view = null;

    private <T> void methodProxy(int[] value, EventType eventType, T o, Method m) {
        if (value == null) {
            return;
        }
        synchronized (this) {
            try {
                //动态创建对应类的代理对象
                Object listenerProxy = Proxy.newProxyInstance(eventType.listenerType().getClassLoader(), new Class[]{eventType.listenerType()}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return m.invoke(o, args);
                    }
                });
                for (int viewId : value) {
                    if (o instanceof Activity) {
                        view = ((Activity) o).findViewById(viewId);
                    } else if (o instanceof View) {
                        view = ((View) o).findViewById(viewId);
                    }

                    if (view == null)
                        continue;
                    Method mViewMethod = view.getClass().getMethod(eventType.listenerSetter(), eventType.listenerType());
                    //反射调用setter方法设置点击回调
                    mViewMethod.invoke(view, listenerProxy);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
