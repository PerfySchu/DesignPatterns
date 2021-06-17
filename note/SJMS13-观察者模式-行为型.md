#### 观察者模式（发布-订阅模式）

##### 概念

- 在对象间建立一对多的依赖，当一个对象状态改变时，所有依赖的对象都会自动收到通知
- 被依赖的对象叫被观察者，依赖的对象叫观察者

##### 模板代码

```java
public interface Subject{
    void registerObserver(Obeserver observer);
    void removeObserver(Observer observer);
    void notifyObservers(Message message);
}
public interface Observer{
    void update(Message message);
}
//被观察者
public class ConcrateSubject implements Subject{
    private List<Observer> observers = new ArrayList<>();
    
    @Override
    public void registerObserver(Observer observer){
        observers.add(observer);
    }
    @Override
    public void removeObserver(Obsever observer){
        observers.remover(observer);
    }
    @Override
    public void notifyObservers(Message message){
        for(Observer observer : observers){
            observer.update(message);
        }
    }
}
//观察者1
public class ConcreteObserverOne implements Observer{
    @Override
    public void update(Message message){
        //获取通知消息，并执行自己的逻辑
        System.out.println("ConcreteObserverOne is notified");
    }
}
//观察者2
public class ConcreteObserverTwo implements Observer{
    @Override
    public void update(Message message){
        //获取通知消息，并执行自己的逻辑
        System.out.println("ConcreteObserverTwo is notified");
    }
}
//使用
public class Demo{
    public static void main(String[] args){
        ConcreteSubject subject = new ConcreteSubject();
        subject.registerObserver(new ConreteObserverOne());
        subject.registerObserver(new ConreteObserverTwo());
        subject.notifyObserver(new Message());
    }
}
```



##### 使用场景

- 使用场景广泛，只要是不同行为代码的解耦，需要区分观察者与被观察者（通知与被通知者），均可使用此模式
- 邮件订阅，RSS，消息中间件中的发布订阅模式



##### 使用案例

- 用户注册，派优惠券，发邮件/短信通知

  ```java
  //耦合写法
  public class UserController{
      private UserService userService;
      private MsgService msgService;
      private CouponService couponService;
      
      public Long Register(String telephone, String password){
          //省去业务部分代码
          //注册
          Long userId = userService.register(telephone, password);
          //发消息
          msgService.postMessage(userId);
          //派发优惠券
          couponService.sendCoupon(userId);
          return userId;
      }
  }
  ```

  ```java
  //解耦写法
  //观察者接口
  public interface RegisterObserver{
      void handleRegSuccess(long userId);
  }
  //消息观察者
  public class RegMsgObserver implements RegisterObserver{
      private MsgService msgService;
      
      @Override
      public void handleRegSuccess(Long userId){
          msgServer.postMessage(userId);
      }
  }
  //优惠券观察者
  public class RegCouponObserver implements RegisterObserver{
      private CouponService couponService;
      
      @Override
      public void handleRegSuccess(Long userId){
          couponService.sendCoupon(userId);
      }
  }
  //注册入口
  public class UserController{
      private UserService userService;
      private List<RegisterObserver> regObservers = new ArrayList<>();
      
      public void setRegObserver(List<RegisterObserver> observers){
          regObservers.addAll(observers);
      }
      
      public Long register(String telephone, String password){
          Long userId = userService.register(telephone, password);
          for(RegisterObserver observer : regObservers){
              observer.handleRegSuccess(userId);
          }
          return userId;
      }
  }
  ```



##### 注

- 设计模式主要解决的问题就是解耦，创建型设计模式将创建和使用解耦，结构型设计模式将不同功能代码解耦 ，行为型模式将不同行为代码解耦
- 上述案例采用的是同步阻塞方式，注册完成后需要等待所有通知结果都处理完成才会返回。更优雅的做法是采用异步非阻塞的形式来实现，注册完之后开启新线程来通知观察者，注册线程直接返回。当然，更优雅的是采用 **EventBus** 的思想来实现（详见下篇）
- 对于跨应用（不同进程）的观察者通知，可以采用消息队列（Message Queue）的形式来实现



#### 异步非阻塞观察者模式

##### 普通异步写法

```java
//方法1
public class RegMsgObserver implements RegObserver{
    private MsgService msgService;
    
    @Override
    public void handleRegSuccess(Long userId){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                msgService.postMessage(userId);
            }
        }).start();
    }
}
```

```java
//方法2
public class UserController{
    private UserService userService;
    private List<RegObserver> regObservers = new ArrayList<>();
    private Executor executor;
    
    public UserControler(Executor executor){
        this.executor = executor;
    }
    
    public void setRegObservers(List<RegObserver> observers){
        regObservers.addAll(observers);
    }
    
    public Long register(String telephone, String password){
        Long userId = userService.register(telephone, password);
        for(RegObserver observer : regObservers){
            executor.execute(new Runnable(){
                @Override
                public void run(){
                    observer.handleRegSuccess(userId);
                }
            });
        }
        return userId;
    }
}
```



##### EventBus （事件总线）实现

- 代码示例

```java
public class UserController{
    private UserService userService;
    private EventBus eventBus;
    private static final int DEFAULT_EVENTBUS_THREAD_POOL_SIZE = 20;
    
    public UserControler(){
        // eventBus = new EventBus();//同步阻塞式
        eventBus = new AsyncEventBus(Executors.newFixedThreadPool(DEFAULT_EVENTBUS_THREAD_POOL_SIZE));
    }
   
    public void setRegObservers(List<Object> observers){
        for(Object observer : observers){
            eventBus.register(observer);
        }
    }
    
    public Long register(String telephone, String password){
        long userId = userService.register(telephone, password);

        eventBus.post(userId);
        return userId;
    }
}

public class RegMsgObserver{
    private MsgService msgService;
    
    @Subscribe
    public void handleRegSuccess(Long userId){
        msgService.postMsg(userId);
    }
}

public class RegCouponObserver{
    private CouponService couponService;
    
    @Subscribe
    public void handleRegSuccess(Long userId){
        couponService.sendCoupon(userId);
    }
}
```



##### EventBus 方法概述

- **EventBus** 和 **AsyncEventBus**

  - EventBus 同步阻塞 `EventBus eventBus = new EventBus();`
  - AsyncEventBus 异步非阻塞，是 EventBus 的子类 `EventBus eventBus = new AsyncEventBus(Executors.newFixedThreadPool(8));`

- **register()函数**

  - 注册观察者，可以接受任何类型的观察者（Object）
  - `public void unregister(Object object);`

- **post()函数**

  - 给观察者发送消息
  - 消息只会发送给可以匹配的观察者，也就是接受参数和post参数对应的观察者，**即观察者只会接收到自己指定类型的消息**
  - `public void post(Object event);`

- **@Subscribe 注解**

  - 指定观察者处理消息通知的方法

  - ```java
    public RetisterObserver{
        @Subscribe
        public void f1(Long userId){}
        
        @Subscribe
        public void f2(String message){}
    }
    ```

  - 当观察者通过 **register()** 函数被注册到 EventBus 时，EventBus 会记录 f1 和 f2 的入参类型，当通过 **post()** 发送消息通知时，会根据消息对象类型来匹配对应的观察者处理函数，比如 **Long** 型的消息会推送给 **f1** , **String** 型的会推送给 **f2**

---

##### 手写 EventBus

- **Subscribe**

  ```java
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @Beta
  public @interface Subscribe {
  }
  ```

- **ObserverAction**

  ```java
  public class ObserverAction {
      private Object target;
      private Method method;
  
      public ObserverAction(Object target, Method method){
          this.target = target;
          this.method = method;
          this.method.setAccessible(true);
      }
  
      public void execute(Object event){
          try {
              method.invoke(target, method);
          }catch (InvocationTargetException | IllegalAccessException e){
              e.printStackTrace();
          }
      }
  }
  ```

- **ObserverRegistry**

  ```java
  public class ObserverRegistry {
      private ConcurrentMap<Class<?>, CopyOnWriteArraySet<ObserverAction>> registry = new ConcurrentHashMap<>();
  
      public void registry(Object observer){
          Map<Class<?>, Collection<ObserverAction>> observerActions = findAllObserverActions(observer);
          for (Map.Entry<Class<?>, Collection<ObserverAction>> entry : observerActions.entrySet()) {
              Class<?> eventType = entry.getKey();
              Collection<ObserverAction> eventAction = entry.getValue();
              CopyOnWriteArraySet<ObserverAction> registeredEventActions = registry.get(eventType);
              if(registeredEventActions == null){
                  registry.putIfAbsent(eventType, new CopyOnWriteArraySet<ObserverAction>());
                  registeredEventActions = registry.get(eventType);
              }
              registeredEventActions.addAll(eventAction);
          }
      }
  
      public List<ObserverAction> getMatchedObserverActions(Object event){
          List<ObserverAction> matchedObservers = new ArrayList<>();
          Class<?> postedEventType = event.getClass();
          for (Map.Entry<Class<?>, CopyOnWriteArraySet<ObserverAction>> entry : registry.entrySet()) {
              Class<?> eventType = entry.getKey();
              Collection<ObserverAction> eventAction = entry.getValue();
              if(postedEventType.isAssignableFrom(eventType)){
                  matchedObservers.addAll(eventAction);
              }
          }
          return matchedObservers;
      }
  
      private Map<Class<?>, Collection<ObserverAction>> findAllObserverActions(Object observer) {
          Map<Class<?>, Collection<ObserverAction>> observerActions = new HashMap<>();
          Class<?> clazz = observer.getClass();
          for(Method method : getAnnotatedMethods(clazz)){
              Class<?>[] parameterTypes = method.getParameterTypes();
              Class<?> eventType = parameterTypes[0];
              if(!observerActions.containsKey(eventType)){
                  observerActions.put(eventType, new ArrayList<ObserverAction>());
              }
              observerActions.get(eventType).add(new ObserverAction(observer, method));
          }
          return observerActions;
      }
  
      private List<Method> getAnnotatedMethods(Class<?> clazz) {
          List<Method> annotatedMethods = new ArrayList<>();
          for (Method method : clazz.getDeclaredMethods()) {
              if (method.isAnnotationPresent(Subscribe.class)) {
                  Class<?>[] parameterTypes = method.getParameterTypes();
                  Preconditions.checkArgument(parameterTypes.length == 1,
                          "Method %s has @Subscribe annotation but has %s parameters.", method, parameterTypes.length);
                  annotatedMethods.add(method);
              }
          }
          return annotatedMethods;
      }
  }
  ```

  

- **EventBus**

  ```java
  public class EventBus {
      private Executor executor;
      private ObserverRegistry registry = new ObserverRegistry();
  
      public EventBus(){
          this(MoreExecutors.directExecutor());
      }
  
      protected EventBus(Executor executor){
          this.executor = executor;
      }
  
      public void register(Object observer){
          registry.registry(observer);
      }
  
      public void post(final Object event){
          List<ObserverAction> observerActions = registry.getMatchedObserverActions(event);
          for (final ObserverAction observerAction : observerActions) {
              executor.execute(new Runnable() {
                  @Override
                  public void run() {
                      observerAction.execute(event);
                  }
              });
          }
      }
  }
  ```

  

- **AsyncEventBus**

  ```java
  public class AsyncEventBus extends EventBus{
      public AsyncEventBus(Executor executor){
          super(executor);
      }
  }
  ```

  

