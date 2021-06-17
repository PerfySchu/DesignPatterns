#### 建造者模式（生成器模式）

##### 使用场景

- 需要创建的对象比较复杂，参数列表长，并且存在必填和非必填的情况，使用普通的构造函数和getter和setter显得很臃肿

- 属性之间存在相互约束

- 希望创建的对象不可变，不能对外暴露setter

- 避免对象存在无效的中间状态，比如5个参数只初始化了3个，此时的对象是不能使用的。（也就是对象的构建是一次性完成的，而不是逐个属性设置得来）

  ```java
  Rectangle r = new Rectangle();//创建对象，此时对象不可用
  r.setWidth(10);//设置了一个属性，此时对象不可用
  r.setHeight(15);//全部属性设置完成，此时对象可用
  ```

  

##### 示例

```java
public class ResourcePoolConfig{
    private String name;
    private int maxTotal;
    private int maxIdle;
    private int minIdle;
    
    private ResourcePoolConfig(Builder builder){
        this.name = builder.name;
        this.maxTotal = builder.maxTotal;
        this.maxIdle = builder.maxIdle;
        this.mindIdle = builder.minIdle;
    }
    //此处使用的是静态内部内构造器，也可以使用普通的外部类作为构造器
    public static class Builder{
        private static final int DEFUALT_MAX_TOTAL = 8;
        private static final int DEFAULT_MAX_IDLE = 8;
        private static final int DEFAULT_MIN_IDLE = 0;
        
        private String name;
        private int maxTotal = DEFAULT_MAX_TOTAL;
        private int maxIdle = DEFAULT_MAX_IDLE;
        private int minIdle = DEFAULT_MIN_IDLE;
        
        public ResourcePoolConfig build(){
            //属性之间的相互约束，是否必填等校验在此处完成
            if(StringUtils.isBlank(name)){
                throw new IllegalArgumentException("资源池名称不能为空");
            }
            if(maxIdle > maxTotal){
                throw new IllegalArgumentException("最大空闲资源数不能超过最大资源数");
            }
            if(minIdle > maxTotal || minIdle > maxIdle){
                throw new IllegalArgumentException("最小闲资源数不能超过最大资源数，最小闲资源数不能超过最大空闲资源数);
            }
            return new ResourcePoolConfig(this);       
        }

		public Builder setName(String name){
			if(StringUtils.isBlank(name)){
				throw new IllegalArgumentException("资源名称不能为空");
			}
			this.name = name;
			return this;
		}
		public Builder setMaxTotal(int maxTotal){
			if(maxTotal <= 0){
				throw new IllegalArgumentException("最大资源数不能小于0");
			}
			this.maxTotal = maxTotal;
		}
		public Builder setMaxIdle(int maxIdle){
			if(maxIdle <= 0){
				throw new IllegalArgumentException("最大空闲资源数不能小于0");
			}
			this.maxIdle = maxIdle;
		}
		public Builder setMinIdle(int minIdle){
			if(minIdle <= 0){
				throw new IllegalArgumentException("最小空闲资源数不能小于0");
			}
			this.minIdle = minIdle;
		}
    }  
}
ResourcePoolConfig config = new ResourcePoolConfig.builder()
                                                   .setName("测试资源池")
                                                   .setMaxTotal(16)
                                                   .setMaxIdle(10)
                                                   .setMinIdle(12)
                                                   .build();
```



##### 建造者与工厂的区别

- 工厂是用来创建某一类相近的对象
- 建造者则是用来对一个复杂属性的对象进行定制化创建