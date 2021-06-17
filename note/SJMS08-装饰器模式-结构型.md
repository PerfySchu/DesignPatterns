#### 装饰器模式

##### 概念

- 使用组合替代继承，解决继承关系过于复杂的问题，实现对原始类功能的增强。
- 可以对同一个类嵌套使用多个装饰器
- 需要跟原始类继承或实现相同的抽象类或接口

##### 使用实例

```java
public interface IA {
    void f();
}
public class A implements IA{
    public void f(){
        //do something
    }
}
public class ADecorator implements IA{
    private IA a;
    private ADecorator(IA a){
        this.a = a;
    }
    public void f(){
        //增强功能
        doSomething();
        //原始功能
        a.f();
        //增强功能
        doOtherthing();
    }
}
```

##### 装饰器与代理的区别

- 本质上都是对原功能的增强。
- 代理模式增强的功能跟原功能无业务相关性，且对用户透明，有系统产生代理对象，无需用户参与。
- 装饰器增强的功能跟原功能业务相关，实现的是对原功能的特殊化定制，每个装饰器都是对原功能在某个方向上的强化。用户根据需要自主选择所需要的装饰器。