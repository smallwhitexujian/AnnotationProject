## java通过反射获取属性,方法,构造方法,注解

```java
public class xxx() {
    public void Annotation(){
        System.out.println("============================Field===========================");
        System.out.println(Arrays.toString(clazz.getFields())); // 自身和父亲的公有字段
        System.out.println("------------------");
        System.out.println(Arrays.toString(clazz.getDeclaredFields()));  //自身所有字段
        System.out.println("============================Method===========================");
        System.out.println(Arrays.toString(clazz.getMethods()));   //自身和父亲的公有方法
        System.out.println("------------------");
        System.out.println(Arrays.toString(clazz.getDeclaredMethods()));// 自身所有方法
        System.out.println("============================Constructor===========================");
        System.out.println(Arrays.toString(clazz.getConstructors()));   //自身公有的构造方法
        System.out.println("------------------");
        System.out.println(Arrays.toString(clazz.getDeclaredConstructors()));   //自身的所有构造方法
        System.out.println("============================AnnotatedElement===========================");
        System.out.println(Arrays.toString(clazz.getAnnotations()));    //获取自身和父亲的注解
        System.out.println("------------------");
        System.out.println(Arrays.toString(clazz.getDeclaredAnnotations()));  //只获取自身的注解
        System.out.println("------------------");
    }
}
```

## java元注解 @Target注解用法

元注解Target值,而且该值都是Annotation_Type的.表示它们只能声明在元注解上
```java
public enum ElementType {
    /**用于描述类、接口(包括注解类型) 或enum声明 Class, interface (including annotation type), or enum declaration */
    TYPE,

    /** 用于描述域 Field declaration (includes enum constants) */
    FIELD,

    /**用于描述方法 Method declaration */
    METHOD,

    /**用于描述参数 Formal parameter declaration */
    PARAMETER,

    /**用于描述构造器 Constructor declaration */
    CONSTRUCTOR,

    /**用于描述局部变量 Local variable declaration */
    LOCAL_VARIABLE,

    /** Annotation type declaration */
    ANNOTATION_TYPE,

    /**用于描述包 Package declaration */
    PACKAGE,

    /**
     * 用来标注类型参数 Type parameter declaration
     * @since 1.8
     */
    TYPE_PARAMETER,

    /**
     *能标注任何类型名称 Use of a type
     * @since 1.8
     */
    TYPE_USE
}
```

## java元注解 @Retention注解用法

`@Retention`注解用来声明注解的生命周期，即表明注解会被保留到哪一个阶段,它的值需要从枚举类中获取。
```java
public enum RetentionPolicy {
    /**
     * 注解仅在源文件中，编译class文件后注解将被移除
     */
    SOURCE,

    /**
     * 注解被保留在class文件中，虚拟机加载class文件时注解将被移除
     * 这也是默认的生命周期
     */
    CLASS,

    /**
     * 注解在源文件和编译后的class文件都存在，
     * 并且虚拟机加载class文件时不会注解被移除
     */
    RUNTIME
}
```

## java元注解 @Inherited

`@Inherited`不需要设置具体的值。如果一个注解被`@Inherited`标注，表示允许子类继承父类的该注解

## java元注解 @Documented

`@Documented`不需要设置具体的值。如果一个注解被`@Documented`标注，则表示该注解会被生成到Javadoc中