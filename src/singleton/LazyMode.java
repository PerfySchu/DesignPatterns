package singleton;

/**
 * 文件名：LazyMode.java
 * 创建日期：2017/9/29 14:45
 * 说明：懒汉模式
 *
 * @author shupf@szkingdom.com
 */
public class LazyMode {
    //对象实例私有且静态
    private static LazyMode lazyMode;

    //构造方法私有
    private LazyMode(){

    }

    //获取对象实例的方法公开且静态
    public static LazyMode getInstance(){
        //第一次请求的时候才初始化一个实例
        if(lazyMode == null){
            lazyMode = new LazyMode();
            System.out.println("init lazymode");
        }

        return lazyMode;
    }
}
