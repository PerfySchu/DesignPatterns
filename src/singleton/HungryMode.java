package singleton;

/**
 * 文件名：HungryMode.java
 * 创建日期：2017/9/29 14:46
 * 说明：饿汉模式
 *
 * @author shupf@szkingdom.com
 */
public class HungryMode {
    //对象实例私有且静态
    private static HungryMode hungryMode = new HungryMode();

    //构造方法私有
    private HungryMode(){

    }

    //获取对象实例的方法公开且静态
    public static HungryMode getInstance(){
        return hungryMode;
    }
}
