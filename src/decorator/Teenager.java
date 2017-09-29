package decorator;

/**
 * 文件名：Teenager.java
 * 创建日期：2017/9/28 10:36
 * 说明：
 *
 * @author shupf@szkingdom.com
 */
public class Teenager extends Person {
    public Teenager(){
        description = "Shoppping List:";
    }

    @Override
    public double cost() {
        return 0;
    }
}
