package decorator;

/**
 * �ļ�����Teenager.java
 * �������ڣ�2017/9/28 10:36
 * ˵����
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
