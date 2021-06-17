### 策略模式

#### 概述

- 定义一组算法，将每个算法封装起来，让它们可以相互替换。策略模式是算法的变化独立于它们的使用者。
- 将策略的定义、创建、使用三部分解耦

#### 实例

##### 策略定义

- 一个策略接口，一组策略接口的实现类

  ```java
  public interface Strategy{
      void algorithmInterface();
  }
  
  public class ConcreteStrategyA implements Strategy{
      @Override
      public void algorithmInterface(){
          //具体实现
      }
  }
  
  public class ConcreteStrategyB implements Strategy{
      @Override
      public void algorithmInterface(){
          //具体实现
      }
  }
  ```

##### 策略创建

- 在一组策略中，通过类型（type）来具体判断使用哪一个策略。另外，可使用工厂模式将策略的按类型创建的具体过程封装起来。

  - 对于无状态的策略对象，不用每次都创建，可通过工厂类缓存起来。

  	```java
  public class StrategyFactory{
      private static final Map<String, Strategy> strategies = new HashMap<>();
      
      static{
          strategies.put("A", new ConcreteStrategyA());
          strategies.put("B", new ConcreteStrategyB());
      }
      
      public static Strategy getStratety(String type){
          if(type == null || type.isEmpty){
              throw new IllegalArgumentException("type should not be empty.");
          }
          return strategies.get(type);
      }
  }

  - 对于有状态的策略对象，需要每次都创建新的对象

    ```java
    public class StrategyFactory{
      public static Strategy getStratety(String type){
          if(type == null || type.isEmpty){
              throw new IllegalArgumentException("type should not be empty.");
          }
          if("A".equals(type)){
              return new ConcreteStrategyA();
          }
          if("B".equals(type)){
              return new ConcreteStrategyB();
          }
          return null;
      }
    }
    ```

##### 策略使用

- 1、运行时动态确定需要使用哪个策略，即在程序运行期间，根据程序当前的上下文参数（系统配置，用户输入、计算结果等）来决定使用哪个策略

- 2、代码中直接指定所使用的策略

  ```java
  
  public class UserCache{
      private Map<String, User> cacheData = new HashMap<>();
      private EvictionStrategy eviction;
      
      public UserCache(EvictionStrategy eviction){
          this.eviction = eviction;
      }
      
      //其他逻辑
  }
  
  //运行时动态确定使用的策略
  public class Application{
      public static void main(String[] args){
          EvictionStrategy evictionStrategy = null;
          Properties props = new Properties();
          props.load(new FileInputStream("./config.properties"));
          String type = props.getProperty("eviction.type");
          evictionStrategy = EvictionStrategyFactory.getEvictionStrategy(type);
          UserCache userCache = new UserCache(evictionStrategy);
          //其他逻辑
      }
  }
  
  //编码时指定使用策略
  public class Application{
   	public static void main(String[] args){
          EvictionStrategy eviction = new LruEvictionStrategy();
          UserCache uerCache = new UserCache(eviction);
          
          //其他逻辑
      }   
  }
  ```



#### 使用策略模式替换if-else

##### 实例

- 普通 if-else 代码

	```java
//if-else 代码
public class OrderService{
    public double discount(Order order){
        double discount = 0.0;
        OrderType type = order.getType();
        if(OrderType.NORMAL.equals(type)){
            //普通折扣
        }else if(OrderType.GROUPON){
            //团购折扣
        }else if(OrderType.PROMOTION){
            //促销折扣
        }
        return discount;
    }
}

- 策略模式代码

  ```java
  //策略模式代码
  public interface DiscountStrategy{
      double calDiscount(Order order);
  }
  
  public class NormalDiscountStrategy{
      @Override
      double calDiscount(Order order){
          //计算普通折扣
      }
  }
  //省略 团购折扣 和 促销折扣代码
  
  public class DiscountStrategyFactory{
      private static final Map<OrderType, DiscountStratety> strategies = new HashMap<>();
      
      static{
          strategies.put(OrderType.NORMAL, new NormalDiscountStrategy());
          strategies.put(OrderType.GROUPON, new GouponDiscountStrategy());
          strategies.put(OrderType.PROMOTION, new PromotionDiscountStrategy());
      }
      
      public static DiscountStrategy getDiscountStrategy(OrderType type){
          return strategies.get(type);
      }
  }
  
  public class OrderService{
      public double discount(Order order){
          OrderType type = order.getType();
          DiscountStrategy strategy = DiscountStrategyFactory.getDiscountStrategy(type);
          return strategy.calDiscount(order);
      }
  }
  
  ```

- 虽然消除了 if-else，但是跟策略模式本身无缘，是得益于策略工厂，工厂预先缓存了各个策略的实现类。但是如果策略对象每次都不同，无法缓存，那么工厂中也需要 if-else 分支

- 如果想要完全消除 if-else， 可以使用反射来实例化具体的策略对象。预先给策略对象注解，根据注解及条件实例化策略对象
- 代码中并非完全不能使用 if-else，而是在一定限度内也没问题，为了完全避免 if-else 而引入大量代码又是也是得不偿失的

