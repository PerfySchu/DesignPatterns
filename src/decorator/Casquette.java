package decorator;

/**
 * �ļ�����Casquette.java
 * �������ڣ�2017/9/28 10:44
 * ˵����Ѽ��ñ
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
