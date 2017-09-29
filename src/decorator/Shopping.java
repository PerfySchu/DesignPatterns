package decorator;

/**
 * 文件名：Shopping.java
 * 创建日期：2017/9/28 10:46
 * 说明：
 *
 * @author shupf@szkingdom.com
 */
public class Shopping {

    public static void main(String[] args) {
        Person person = new Teenager();

        person = new Shirt(person);
        person = new Casquette(person);

        System.out.println(person.getDescription() + "￥" + person.cost());
    }
}
