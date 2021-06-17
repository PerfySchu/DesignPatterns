#### 适配器模式

##### 概念

- 将不兼容的接口转换为可兼容的接口，使原本由于接口不兼容而不能一起工作的类可以一起工作

##### 分类

- 类适配器

  ```java
  //适配后的接口
  public interface ITarget{
      void f1();
      void f2();
      void fc();
  }
  //被适配类（原始逻辑）
  public class Adaptee{
      public void fa(){
          //... ...
      }
      public void fb(){
          //... ...
      }
      public void fc(){
          //... ...
      }
  }
  //适配器
  public class Adaptor extends Adaptee implements ITarget{
      public void f1(){
          super.fa();
      }
      public void f2(){
          //重写f2逻辑
      }
      //fc可复用，无需重写
  }
      
  ```

  

- 对象适配器

  ```java
  //适配后的接口
  public interface ITarget{
      void f1();
      void f2();
      void fc();
  }
  //被适配类（原始逻辑）
  public class Adaptee{
      public void fa(){
          //... ...
      }
      public void fb(){
          //... ...
      }
      public void fc(){
          //... ...
      }
  }
  //适配器
  public class Adaptor implements ITarget{
      private Adaptee adaptee;
      public Adaptor(Adaptee adaptee){
          this.adaptee = adaptee;
      }
      public void f1(){
          adaptee.fa();
      }
      public void f2(){
          //重写f2方法
      }
      public void fc(){
          adaptee.fc();
      }
  }
  ```

#####  使用场景

- 作为一种补偿模式，用来补救原本代码的设计缺陷。属于一种无奈之举的设计模式

- 1.封装有设计缺陷的接口（比如引入的外部SDK）

  ```java
  //外部SDK
  public class OutterSDK{
      public static void staticFunction1(){
          //... ...
      }
      public void uglyNamingFunction2(){
          //... ...
      }
      public void tooManyParamsFunction3(){
          //... ...
      }
  }
  //使用适配器重构后的规范接口
  public interface ITarget{
      void function1();
      void function2();
      void function3();
  }
  //适配器
  public class SDKAdaptor extends OutterDSK implements ITraget{
      public void function1(){
          super.staticFunction1();
      }
      public void function2(){
          super.uglyNamingFunction2();
      }
      public void function3(ParamsWrapper paramWrapper){
          super.toManyParamsFunction3(paramWrapper.getParamA(), ...)
      }
  }
  ```

  

- 2.统一多个类的接口设计（比如将多个外部系统接口适配为统一的接口定义）

  ```java
  //外部方法A
  public class FilterA{
      public String filterSexyWords(String text){
          //... ...
      }
      public String filterPoliticalWords(String text){
          //... ...
      }
  }
  //外部方法B
  public class FilterB{
      //方法逻辑同A
  }
  //外部方法C
  public class FilterC{
     //方法逻辑同A
  }
  
  //未适配前的代码
  public class RiskManagement{
      private FilterA filterA = new FilterB();
      private FilterB filterB = new FilterB();
      private FilterC filterC = new FilterC();
      public String filterSensitiveWords(String text){
          String maskedText = filterA.filterSexyWords(text);
          maskedText = filterA.filterPoliticalWords(maskText);
          maskedText = filterB.filter(maskedText);
          maskedText = filterC.filter(maskedText, "***");
          return maskedText;
      }
  }
  
  //适配器模式改造
  public interface ISensitiveWordsFilter{
      String filter(String text);
  }
  public class FilterA implements ISensitiveWordsFilter{
      private FilterA filterA = new FilterA();
      public String filter(String text){
          String maskedText = filterA.filterSexyWords(text);
          return filterA.filterPoliticalWrods(maskedText);
      }
  }
  //FilterB 和 FilterC 代码跟 FilterA 类似，省去
  //适配后的使用
  public class RiskManagement{
      private List<ISensitiveWordsFilter> filters = new ArraysList<>();
      public void addSensitiveWordsFilter(IsensitiveWordsFilter filter){
          filters.add(filter);
      }
      public String filterSensitiveWords(String text){
          String maskedText = text;
          for(IsensitiveWordsFilter filter : filters){
              maskedText = filter.filter(maskedText);
          }
          return maskedText;
      }
  }
  ```

  

- 3.替换依赖的外部系统（比如从系统A迁移到系统B，使用适配器减少代码改动）

  ```java
  //外部系统A
  public interface IA{
      void fa();
  }
  public class A implements IA{
      public void fa(){
          //... ...
      }
  }
  public class Demo{
      private IA a;
      public Demo(IA a){
          this.a = a;
      }
  }
  Demo d = new Demo(new A());
  
  //将外部系统A替换成外部系统B
  public class BAdapter implements IA{
      private B b;
      public BAdapter(B b){
          this.b = b;
      }
      public void fa(){
          b.fb();
      }
  }
  Demo d = new Demo(new Adaptor(new B()));
  ```

  

- 4.兼容老版本接口

- 5.适配不同格式的数据



##### java日志中的适配器模式



##### 四种结构型模式的对比

- 代理：代理模式在不改变原始类接口的条件下，为原始类定义一个代理类，主要目的是控制访问，而非加强功能，这是它跟装饰器模式最大的不同。
- 桥接：桥接模式的目的是将接口部分和实现部分分离，从而让它们可以较为容易、也相对独立地加以改变。
- 装饰器：装饰者模式在不改变原始类接口的情况下，对原始类功能进行增强，并且支持多个装饰器的嵌套使用。
- 适配器：适配器模式是一种事后的补救策略。适配器提供跟原始类不同的接口，而代理模式、装饰器模式提供的都是跟原始类相同的接口。
- 以上四种模式本质上都是Wrapper模式，通过多加一层封装来实现对应的逻辑，结构上有相似之处。