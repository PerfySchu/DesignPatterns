#### 模板模式

##### 定义

- 在一个方法中定义一个算法骨架，将某些步骤推迟到子类中去实现

- 子类在不改变算法整体结构的情况下，可以重新定义算法中的某些步骤

  

##### 示例

```java
public abstract class AbstractClass{
    public final void templateMethod(){
        //业务逻辑1
        method1();
        //业务逻辑2
        method2();
    }
    
    protected abstract void method1();
    protected abstract void method2();
}

public class ConcreteClass1 extends AbstractClass{
    @Override
    protected void method1(){
        //业务逻辑1实现
    }
    
    @Override
    protected void method2(){
        //业务逻辑2实现
    }
}

public class ConcreteClass2 extends AbstractClass{
    @Override
    protected void method1(){
        //业务逻辑1实现
    }
    
    @Override
    protected void method2(){
        //业务逻辑2实现
    }
}

AbstractClass demo = ConcreteClass1();
demo.templateMethod();
```



##### 作用

- 复用
  - 当业务逻辑拆分比较合理，每一个小单元都可以独立成方法，通用逻辑可以在父类中设置默认实现，保证最大限度的复用
- 扩展
  - 当业务逻辑拆分合理，每一个小逻辑单元都可以在子类中重新实现

---

##### 回调

- 原理
  - **A类 **事先注册一个 **函数F** 到 **B类**，**A类** 调用 **B类** 的 函数P 时，**B类** 反过来调用 A类 注册到 **B类** 的**函数F**，其中 **函数F** 即 **回调函数**

- 示例

  ```java
  public interface ICallback{
      void methodToCallback();
  }
  
  public class BClass{
      public void process(ICallback callback){
          callback.methodToCallback();
      }
  }
  
  public class AClass{
      public static void main(String[] args){
          BClass b = new BClass();
          //使用匿名内部类实现回调
          b.process(new ICallback(){
              @Override
              public void methodToCallback(){
                  System.out.pritlin("Call back me");
              }
          });
      }
  }
  ```

- 同步回调与异步回调

- 回调与模板

  - 回调基于组合关系实现，而模板基于继承关系
  - 回调可以避免Java无法多继承导致的限制，可以按需往回调模板中注入回调对象

