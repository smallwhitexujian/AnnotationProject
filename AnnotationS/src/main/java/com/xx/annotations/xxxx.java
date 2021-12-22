package com.xx.annotations;

import android.app.Activity;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class xxxx {

    private static <T> void bindEvent(T t) {
        Class cls = t.getClass();
        //获取对应类下面的所有方法
        Method[] methods = cls.getDeclaredMethods();

        for (Method method : methods) {
            //获取方法上的所有注解
            Annotation[] annotations = method.getAnnotations();

            for (Annotation annotation : annotations) {
                //获取注解类型
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (!annotationType.isAnnotationPresent(EventType.class))
                    continue;


                EventType eventType = annotationType.getAnnotation(EventType.class);
                if (eventType == null)
                    continue;

                //获取需要代理的类
                Class listenerType = eventType.listenerType();
                //获取绑定方法名
                String listenerMethod = eventType.listenerSetter();

                try {
                    Method valueMethod = annotationType.getDeclaredMethod("value");
                    //通过反射我们不需要关心到底是OnClick还是OnLongClick 注解
                    int[] viewIds = (int[]) valueMethod.invoke(annotation);
                    method.setAccessible(true);


                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }

        }
    }

}
