#### 单例设计模式

##### 定义

- 一个类只允许创建一个实例

##### 解决的问题

- 1.避免资源访问冲突
- 2.表示业务上的全局唯一，即业务上应该只有一份的对象实现（如配置信息）


##### 注意的事项

- 构造函数权限private，避免通过外部new对象
- 对象创建时的并发问题
- 是否支持延迟加载
- 获取实例的性能是否高

##### 实现方式

###### 饿汉

```java
public class Singleton{
    private static Singleton instance = new Singleton();
    private Singleton(){}
    public static Singleton getInstance(){
        return instance;
    }
}
```



###### 懒汉

```java
public class Singleton{
    private static Singleton instance;
    private Singleton(){}
    private static Singleton getInstance(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }
}
```



###### 双重检测

```java
public class Singleton{
    //高版本的jdk不需要volatile修饰
    private static Singleton instance;
    private Singleton(){}
    public static Singleton getInstance(){
        if(instance == null){
            //对类本身加锁，而不是对象
            synchronized(Singleton.class){
                if(instance == null){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```



###### 静态内部类

```java
public class Singleton{
    private Singltton(){}
    private static class InstanceHolder{
        private static final Singleton instance = new Singleton();
    }
    public static Singleton getInstance(){
        return InstanceHolder.instance;
    }
}
```



###### 枚举

```java
public enum Singleton{
    INSTANCE;
}
```



##### 单例存在的问题及替代方案

###### 存在的问题

- 违反OOP特性，对继承和多态性的支持不友好
- 单例隐藏了类之间的依赖的关系
- 对代码扩展性不友好
- 对代码可测试性不友好
- 不支持带参构造函数

###### 替代方案

- 使用静态方法

##### 单例模式进阶

###### 理解单例模式中的唯一性

- 唯一性的作用范围
- 线程唯一：线程内是唯一的，线程间不唯一（进程中不唯一）
- 进程唯一：线程内是唯一的，线程间也是唯一的，但是进程间不唯一
- 集群唯一：由多个进程构成的进程集群之间唯一

###### 线程唯一的单例

- 以线程ID作为key，实例作为value构建一个map，来保证线程内唯一，线程间不唯一(java 中的 ThreadLocal)

  ```java
  public class Singleton{
  	private static final ConcurrentHashMap<Long, Singleton> instances = new ConcurrentHashMap<>();
      private Singleton(){}
     	public static getInstance(){
         Long currentThreadId = Thread.currentThread().getId();
         instances.putIfAbsent(currentThreadId, new Singleton());
         return instances.get(currentThreadId);
     }
  }
  ```

  

###### 集群环境下的单例

- 保证进程集群间的唯一性

- 进程间通过外部共享区使用同一个序列化对象

- 每次使用都需要操作序列化和反序列化，用完存回共享区

- 为保证任何时刻进程间唯一，需要对对象进行加锁和释放锁

- **对Java而言，单例的唯一性指的是类加载器唯一**

  

###### 多例模式

- 多例模式，不是指可以创建无限多个实例，而是可以指定固定的实例数

  ```java
  public class Singleton{
      private long serverNo;
      private String serverAddress;
      private Singleton(long serverNo, String serverAddress){
          this.serverNo = serverNo;
          this.serverAddress = serverAddress;
      }
      
      private static final int SERVER_COUNT = 3;
      private static final Map<Long, Singleton> instances = new HashMap<>();
      static{
          instances.put(1L, new Instance(1L, "address1"));
          instances.put(2L, new Instance(2L, "address2"));
          instances.put(3L, new Instance(3L, "address3"));
      }
      public Singleton getInstance(long serverNo){
          return instances.get(serverNo);
      }
      public Singleton randomInstance(){
          Random r = new Random();
          int no = r.nexInt(SERVER_COUNT) + 1;
          return instances.get(no);
      }
  }
  ```

  