package singleton;

/**
 * 文件名：SingleTest.java
 * 创建日期：2017/9/29 14:45
 * 说明：
 *
 * @author shupf@szkingdom.com
 */
public class SingleTest {

    public static void main(String[] args) {
        System.out.println("get lazy1");
        LazyMode lazyMode1 = LazyMode.getInstance();
        System.out.println("get lazy2");
        LazyMode lazyMode2 = LazyMode.getInstance();
        System.out.println(lazyMode1 == lazyMode2 ? "相等":"不等");

        System.out.println("get hungry1");
        HungryMode hungryMode1 = HungryMode.getInstance();
        System.out.println("get hungry2");
        HungryMode hungryMode2 = HungryMode.getInstance();
        System.out.println(hungryMode1 == hungryMode2 ? "相等":"不等");
    }
}
