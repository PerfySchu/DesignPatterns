package decorator;

/**
 * 文件名：Casquette.java
 * 创建日期：2017/9/28 10:44
 * 说明：鸭舌帽
 *
 * @author shupf@szkingdom.com
 */
public class Casquette extends HatDecorator {

    Person person;

    public Casquette(Person person){
        this.person = person;
    }

    @Override
    public String getDescription() {
        return person.getDescription() + "a casquette \n";
    }

    @Override
    public double cost() {
        return 75 + person.cost();
    }
}
