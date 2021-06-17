#### 代理模式

##### 概念

- 再不改变原始类的情况下，通过引入代理人来给原始类附加新功能

##### 实现方式

- 1.基于共同接口扩展

  ```java
  //抽象接口
  public interface IUserController{
      void retister(String username, String password);
      void login(String username, String password);
  }
  //业务实现类
  public class UserController{
      @Override
      public void register(String username, String password){
          //... ...
      }
      @Override
      public void login(String username, String password){
          //... ...
      }
  }
  //业务增强代理类
  public class UserControllerProxy{
      //业务增强的工具类
      private LogCollector logCollector;
      //持有原始业务类的对象
      private UserController userController;
      public UserContollerProxy(UserController userController){
          this.userController = userController;
          this.logCollector = new LogCollector;
      }
      @Override
      public void login(String username, String password){
          logCollector.start();
          userController.login(String username, String password);
          logCollector.stop();
      }
      @Override
      public void register(String username, String password){
          logCollector.start();
          userController.register(String username, String password);
          logCollector.stop();
      }
  }
  
  //使用实例
  IUserController userContorller = new UserControllerProxy(new UserController());
  ```

- 2.基于继承的扩展（在无法抽象出接口，或业务对象来自于三方无法变更时）

  ```java
  //基于继承的扩展
  public class UserControllerProxy extents UserController{
      private LogCollector logCollector;
      public UserControllerProxy(){
          this.logCollector = new LogCollector();
      }
      public void register(String username, String password){
          logCollector.start();
          //直接调用父类的业务方法即可
          super.register();
          logCollector.stop();
      }
      public void login(String username, String password){
          logCollector.start();
          //直接调用父类的业务方法即可
          super.login();
          logCollector.stop();
      }
  }
  //使用示例
  UserControllerProxy usercontroller = new UserControllerProxy();
  ```

- 3.动态代理

  - 前两种方法的缺陷
    - 需要将业务方法全部重新实现一遍，以实现功能增强
    - 所有涉及要到增强的类，都需要创建一个专门的代理类
  - 动态代理的概念
    - 事先不创建代理类，而是在系统运行的时候，根据需要动态地为原始对象创建代理对象，然后在系统中替换掉原始对象，以实现代理功能。
    - Java原生支持动态代理实现（基于Proxy 和 InvocationHandler)

  ```java
  public class LogCollectorProxy{
      private LogCollector logCollector;
      public LogCollectorProxy(){
          this.logCollector = new LogCollector();
      }
      
      public Object createProxy(Object proxiedObject){
          Class<?>[] interfaces = proxiedObject.getInterfaces();
            handler = new DynamicProxyHandler(proxiedObject);
          return Proxy.newProxyInstance(proxiedObject.getClass().getClassLoader(), interfaces, handler);
      }
      
      private class DynamicProxyHandler implements InvocationHandler{
          private Object proxiedObject;
          public DynamicProxyHandler(Object proxiedObject){
              this.proxiedObject = proxiedObect;
          }
          
          public Object invode(Object proxy, Method method, Object[] args) throw  Throwable  {
              logCollector.start();
              Object result = method.invoke(proxiedObject, args);
              logCollector.end();
              return result;
          }
      }
  }
  
  //使用示例
  LogCollectorProxy proxy = new LogCollectorProxy();
  IUserContorller userController = (IUserController) proxy.createProxy(new UserController());
  ```

  - Spring AOP 的底层实现就是基于动态代理，用户通过配置指定被代理的类，然后指定需要附加的方法，以实现对原方法的扩展增强

##### 使用场景

- 业务系统的非功能性需求开发：日志，监控，统计，事务等
- RPC 框架远程服务代理
- 接口数据缓存