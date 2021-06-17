#### 工厂模式

##### 简单工厂

- 同一类对象，创建逻辑简单，则可以将对象的创建过程提取出来，封装成一个单独的类，按入参来创建所需对象

- 如果对象能复用，则可以在工厂类中预先创建对象并缓存起来，提升速度

  ```java
  public class Factory{
      public static Obj getInstance(String objType){
          IObj obj = null;
          if("one".equals(objType)){
              obj = new ObjOne();
          }else if("two".equals(objType)){
              obj = new ObjTwo();
          }else if("three".equals(objType)){
              obj = new ObjThree();
          }
          return obj;
      }
      //对象能复用的前提下，可事先缓存
      private static Map<String, IObj> objCache = new HashMap<>();
      static{
          objCache.put("one", new ObjOne());
          objCache.put("two", new ObjTwo());
          objCache.put("three", new ObjThree());
      }
      public static IObj getInstanceFromCache(String objType){
          return objCache.get(objType);
      }
  }
  
  public class Test{
      public void testFactory(String objType){
          IObject obj = Factory.getInstance(objType);
          obj.doSomething();
      }
  }
  ```

  

##### 工厂方法

- 若工厂内的对象无法复用，且对象数量多，有大量 if-else 代码，则不符合开闭原则。可考虑进一步提取抽象，将工厂类中的创建方法抽象出来，由具体的业务对象来实现

- ```java
  public interface Factory{
      IObj getInstance();
  }
  public class ObjOneFactory implements Factory{
      @Override
      public IObj getInstance(){
          return new ObjOne();
      }
  }
  public class ObjTwoFactory implements Factory{
      @Override
      public IObj getInstance(){
          return new ObjTwo();
      }
  }
  public class ObjThreeFactory implements Factory{
      @Override
      public IObj getInstance(){
          return new ObjThree();
      }
  }
  public class Test{
      public void testFactory(String objType){
          Factory objFactory = null;
          if("one".equals(objType)){
              objFactory = new ObjOneFactory();
          }else if("two".equals(objType)){
              objFactory = new ObjTwoFactory();
          }else if("three".equals(objType)){
              objFactory = new ObjThreeFactory();
          }
          IObj obj = objFactory.getInstance();
          obj.toSomething();
      }
  }
  ```

- 上述代码通过抽象出工厂方法，每个业务类有自己的工厂实现，但是获取具体的业务工厂时，又需要 if-else 判断，所以还是不够好。可以进一步考虑为业务工厂类再建一个工厂，专门生产工厂类。因为业务工厂类对象是可复用的，因此可以用缓存提高效率

  ```java
  public interface Factory{
      IObj getInstance();
  }
  public class ObjOneFactory implements Factory{
      @Override
      public IObj getInstance(){
          return new ObjOne();
      }
  }
  public class ObjTwoFactory implements Factory{
      @Override
      public IObj getInstance(){
          return new ObjTwo();
      }
  }
  public class ObjThreeFactory implements Factory{
      @Override
      public IObj getInstance(){
          return new ObjThree();
      }
  }
  public class FactoryMap{
      private static final Map<String, Factory> cacheMap = new HashMap<>();
      static{
          cacheMap.put("one", new ObjOneFactory());
          cacheMap.put("two", new ObjTowFactory());
          cacheMap.put("three", new ObjThreeFactory());
      }
      public Factory getObjFactory(String objType){
          return cacheMap.get(objType);
      }
  }
  public class Test{
      public void testFactory(String objType){
          Factory objFactory = FactoryMap.getObjFactory(objType);
          IObj obj = objFactory.getInstance();
          obj.toSomething();
      }
  }
  ```

  

##### 抽象工厂

- 定义一个抽象工厂类，用来负责多种不同类型的对象创建（比较极端的例子是Spring Bean 工厂）

  ```java
  public interface IFactory{
      IObj createObj();
      IHuman createHuman();
      IAnimal createAnimal()
  }
  public class ObjFactory implements IFactory{
      @Override
      public IObj createObj(){
          return new ObjOne();
      }
  }
  public class HumanFactory implements IFactory{
      @Override
      public IHuman(){
          return new HumanOne();
      }
  }
  ```

  

##### 依赖注入（DI）

- 依赖注入容器相当于一个非常大的工厂类

- 依赖注入容器一般需要支持 **配置解析**、**对象创建**、**生命周期管理** 这三项核心功能

  - 配置解析

    - 容器与具体业务解耦，不事先预设任何对象，而是提供一套通用的对象创建模式，预留出可配置的位置给具体的业务来使用。通常都是使用配置文件（spring也支持注解解析）

      ```java
      public class ObjOne{
          private Human human;
          public ObjOne(Human human){
              this.human = human;
          }
      }
      public class Human{
          private int age;
          private String name;
          public Human(int age, String name){
              this.age = age;
              this.name = name;
          }
      }
      ```

      ```xml
      <beans>
      	<bean id="objOne" class="xyz.perfy.ObjOne">
          	<constructor-arg ref="human"/>
          </bean>
          <bean id="human" class="xyz.perfy.Human">
          	<constructor-arg type="int" value=18/>
              <constructor-arg type="String" value="PerfySchu"/>
          </bean>
      </beans>
      ```

  - 对象创建

    - 基于Java反射机制，根据配置文件解析结果，创建所需的对象

  - 生命周期管理

    - 对象作用范围 scope， 常见有 单例singleton，原型prototype（新版spring中还有 request， session， global session
    - 加载方式 ：懒加载 lazy-init
    - 初始化，销毁方法 init-method, destory-method

- DI框架核心工厂类设计（BeanFactory)

  - 根据配置文件解析得到 BeanDefinition 来创建对象，其中 BeanDefinition 包含对象的最用范围，加载方式，初始-销毁方法等一系列参数。对于singleton类型的对象会有一个map缓存起来。

- Spring 如何解决循环依赖

  - 构造器循环依赖无法解决
  - prototype 对象的循环依赖是无法解决的
  - 单例循环依赖可以解决
  - 总而言之，只有单例的 setter 或 field 注入循环依赖可以解决
  - Spring 创建Bean的过程
    - 实例化，即调用对象构造方法实例化对象 （createBeanInstance)
    - 填充属性，对bean的属性依赖进行填充赋值 (populateBean)
    - 初始化，即调用bean配置中指定的 init 方法 (initializeBean)
  - 三级缓存
    - singletonFactories : 单例对象工厂缓存
    - earlySingletonObjects : 提前暴露的单例对象缓存 
    - singletonObjects : 单例对象缓存 

  ```java
  protected Object getSingleton(String beanName, boolean allowEarlyReference){
      Object singletonObject = this.singletonObjects.get(beanName);
      if(singletonObject == null && isSingletonCurrentlyInCreation(beanName)){
          synchronized(this.singletonObjects){
              singleObject = this.earlySingletonObjects.get(beanName);
              if(singleObject == null && allowEarlyReference){
                  ObjectFactory<?> singletonFactory = this.singletonFactories.getObject();
                  if(objectFactory != null){
                      signletonObject = singletonFactory.getObject();
                      this.earlySingletonObjects.put(beanName, singletonObject);
                      this.singletonFactories.remove(beanName);
                  }
              }
          }
      }
      return (singletonObject != NULL_OBJECT ? singletonObject : null);
  }
  ```

  - 循环依赖解决过程

    - 实例化 -> 实例进入 SingletonFactories 中，可以对外暴露引用

    - 初始化，发现有引用其他实例，将当前实例暂时搁置，初始化引用的实例。因为有提前暴露出自己的引用，被引用的实例在引用自身时不会遇到问题，可以直接从 SingletonFactories 中拿到自身引用。

    - 待被引用实例初始化完之后，再继续初始化自身

      

