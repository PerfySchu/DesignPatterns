package decorator;

/**
 * �ļ�����Shirt.java
 * �������ڣ�2017/9/28 10:41
 * ˵����
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
