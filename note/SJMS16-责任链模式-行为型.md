### 责任链模式

#### 定义

- 将请求的发送和接收解耦，让多个接收对象都有机会处理这个请求。将这些接收对象串成一条链，并沿着这条链传递这个请求，直到链上的某个接收对象能够处理它为止。

#### 实现

##### 方法一

- 定义责任处理抽象类Handler，抽象方法 doHandle() 用来给子类实现，用以处理具体的业务逻辑
- 定义责任处理链 HandlerChain，基于链表结构，用来添加责任链环节，当前处理环节，下一个环节，以及链条是否到此结束

```java
public abstract class Handler{
    protected Handler successor = null;
    
    public void setSuccessor(Handler successor){
        this.successor = successor;
    }
    
    public final void handle(){
        boolean handled = doHandle();
        if(successor != null && !handled){
            successor.handle();
        }
    }
    
    protected abstract boolean doHandle();
}

public class HandlerA extends Handler{
    @Override
    protected booelan doHandle(){
        boolean handled = false;
        //责任A业务逻辑
        return handled;
    }
}

public class HandlerB extends Handler{
    @Override
    protected boolean doHandle(){
        boolean handled = false;
        //责任B业务逻辑
        return handled;
    }
}

public class HandleChain{
    private Handler head = null;
    private Handler tail = null;
    
    public void addHandler(Handler handler){
        handler.setSuccessor(null);
        
        if(head == null){
            head = handler;
            tail =handler;
            return;
        }
        tail.setSuccessor(handler);
        tail = handler;
    }
    
    public void handle(){
        if(head != null){
            head.handle();
        }
    }
}

//使用实例
public class Application{
    public static void main(String[] args){
        HandleChain chain = new HandlerChain();
        chain.addHandler(new HandlerA());
        chain.addHandler(new HandlerB());
        chain.handle();
    }
}
```

##### 方法二

- 跟方法一类似，将 HandlerChain 的结构从链表改成数组，直接通过遍历数组来进行责任链条调用
- 将原本用 Handler 抽象父类控制的链条调用直接放到 HandlerChain 中
- 相比于方法一跟简洁一些

```java
public interface IHandler{
    boolean handle();
}

public class HandlerA implements IHandler{
    @Override
    public boolean handle(){
        boolean handled = false;
        //责任A业务逻辑
        return handled;
    }
}

public class HandlerB implements IHandler{
    @Override
    public boolean handle(){
        boolean handled = false;
        //责任B业务逻辑
        return handled;
    }
}

public class HandlerChain{
    private List<IHandler> handlers = new ArrayList<>();
    
    public void addHandler(IHandler handler){
        this.handlers.add(handler);
    }
    
    public void handle(){
        for(Ihandler handler : handlers){
            boolean handled = handler.handler();
            if(handled){
                break;
            }
        }
    }
}

//使用示例
public class Application{
    public static void main(String[] args){
        HandlerChain chain = new HandlerChain();
        chain.addHandler(new HandlerA());
        chain.addHandler(new HandlerB());
        chain.handle();
    }
}
```



##### 变型式 --> 链条间不阻断，所有责任环节都处理一遍

- 将方法一和方法二中的 HandlerChain 关于 handled 的判断移除即可，保证链条的每个环节都可以被执行到

  

#### 应用场景

##### 敏感词过滤

```java
public interface SensitiveWordFilter{
    boolean doFilter(Content content);
}

public class SexyWordFilter implements SensitiveWordFilter{
    @Override
    public boolean doFilter(Content content){
        boolean leagal = true;
        //具体校验的业务逻辑
        return leagal;
    }
}
//剩下的各种敏感词校验类代码省略

public class SensitiveWordFilterChain{
    private List<SensitiveWordFilter> filters = new ArrayList<>();
    
    public void addFilter(SensitiveWordFilter filter){
        this.filter.add(filter);
    }
    
    public boolean filter(Content content){
        for(SensitiveWordFilter filter : filters){
            if(!filter.doFilter(content)){
                return false;
            }
        }
        return true;
    }
}

public class ApplicationDemo{
    public static void main(String[] args){
        SensitiveWordFilterChain filterChain = new SensitiveWordChain();
        filterChain.addFilter(new SexyWordFilter());
        filterChain.addFilter(new PoliticalWordFilter());
        
        boolean legal = filterChain.filter(new Content());
        if(!legal){
            //内容被拦截
        }else{
            //内容校验通过
        }
    }
}
```



##### Servlet Filter

- Servlet 过滤器，用来对HTTP请求进行过滤，比如鉴权、限流、记录日志、验证参数等。

- 日常使用样例

  ```java
  public class LogFilter implements Filter{
      @Override
      public void init(FilterConfig filterConfig) throws ServletException{
       	//创建filter时自动调用的方法
          //可用来初始化参数等操作
      }
      
      @Override
      public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
          System.out.println("拦截客户端发来的请求");
          chain.doFilter(request, response);
          System.out.println("拦截发送给客户端的响应");
      }
      
      @Override
      public void destroy(){
          //在销毁Filter时自动调用
      }
  }
  ```

  ```xml
  //web.xml中的配置项
  <filter>
  	<filter-name>logFilter</filter-name>
      <filter-class>com.perfy.LogFilter</filter-class>
  </filter>
  <filter-mapping>
  	<filter-name>logFilter</filter-name>
      <url-pattern>/*</url-pattern>
  </filter-mapping>
  ```

- 上述代码中，FilterChain 在 Servlet 中只存在接口规范，具体由对应的 web 服务器来实现，tomcat 中的简化实现版本如下

  ![ServletFilter](C:\Users\PerfySchu\Desktop\Note\image\ServletFilter.jpg)

  ```java
  public final class ApplicationFilterChain implements FilterChain{
      private int pos = 0;//当前执行的 filter 下标
      private int n; //filter 个数
      private ApplicationFilterConfig[] filters;
      private Servlet servlet;
      
      @Override
      public void doFilter(ServletRequest request, ServletResponse response){
          if(pos < n){
              ApplicationFilterConfig filterConfig = filters[pos++];
              Filer filter = filterConfig.getFilter();
              filter.doFilter(reqeust, response, this);
          }else{
              //所有的filter都执行完，继续执行 servlet
              servlet.service(request, response);
          }
      }
      
      public void addFilter(ApplicationFilterConfig filterConfig){
          for(ApplicationFilter filter : filters){
              if(filer == filterConfig){
                  return;l
              }
          }
          if(n == filter.length){
              ApplicationFilterConfig[] newFilters = new ApplicationFilterConfig[n + INCREMENT];
              System.arraycopy(filters, 0, newFilters, 0, n);
              filters = newFilters;
          }
          filters[n++] = filterConfig;
      }
  }
  ```

  

##### Spring Interceptor

- SpringMVC 中，用来对请求进行来拦截处理，功能类似于 ServletFilter

  ![SpringInterceptor](.\image\SpringInterceptor.jpg)

  ```java
  public class LogInterceptor implements HandlerInterceptor{
      @Overrid
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response) throw Exception{
          System.out.println("拦截客户端发来的请求");
          return true;//继续后续的处理
      } 
      
      @Override
      public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
       	System.out.println("拦截发送给客户端的响应");   
      }
      
      @Override
      public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
       	System.out.println("总会被执行");   
      }
  }
  ```

  ```xml
  //SpringMVC 配置文件
  <mvc:interceptors>
  	<mvc:interceptor>
      	<mvc:mapping path="/*"/>
          <bean class="com.perfy.LogInterceptor"/>
      </mvc:interceptor>
  </mvc:interceptors>
  ```

- HandleExecutionChain，Interceptor的责任链处理器，将请求的拦截和相应的拦截拆分到两个函数中 applyPreHandle() 和 applyPostHandle()，简化版的代码如下

  ```java
  public class HandleExecutionChain{
      private finale Object handler;
      private HandleInterceptor[] interceptors;
      
      public void addInterceptor(HandlerInterceptor interceptor){
          initInterceptorList().add(interceptor);
      }
      
      boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response){
          HandlerInterceptor[] interceptor = getInterceptor();
          if(!ObjectUtils.isEmpty(interceptors)){
              for(int i=0; i<interceptors.length; i++){
                  HandlerInterceptor interceptor = interceptor[i];
                  if(!interceptor.preHandle(requeset, response, this.handler)){
                      triggerAfterCompletion(request, response, null);
                  	return false;
                  }
              }
          }
      }
      return true;
  }
  
  void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView mv){
      HandlerInterceptor[] interceptors = getInterceptors();
      if(!ObjectUtils.isEmpty(interceptors)){
          for(int i=interceptors.length-1; i>=0; i--){
              HandlerInterceptor interceptor = interceptor[i];
              interceptor.postHandle(reqeust, response, this.handler, mv);
          }
      }
  }
  
  void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, Exception ex) throws Exception{
      HandlerInterceptor[] interceptors = getInterceptors();
      if(!ObjectUtils.isEmpty(interceptors)){
          for(int i=this.interceptorIndex; i>=0; i--){
              HandlerInterceptor interceptor = interceptors[i];
              try{
                  interceptor.afterCompletion(request, response, this.handler, ex);
              }catch(Throwable ex2){
                  logger.error("HandlerInterceptor.afterCompletion threw Exception", ex2);
              }
          }
      }
  }
  ```

  
