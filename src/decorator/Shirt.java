package decorator;

/**
 * 文件名：Shirt.java
 * 创建日期：2017/9/28 10:41
 * 说明：
 *
 * @author shupf@szkingdom.com
 */
public class Shirt extends ClothingDecorator {

    Person person;

    public Shirt(Person person){
        this.person = person;
    }

    @Override
    public String getDescription() {
        return person.getDescription() + "a shirt \n";
    }

    @Override
    public double cost() {
        return 100 + person.cost();
    }
}
