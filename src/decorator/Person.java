package decorator;

/**
 * �ļ�����Person.java
 * �������ڣ�2017/9/28 10:35
 * ˵����
 *
 * @author shupf@szkingdom.com
 */
public abstract class Person {
    String description = "Unkown";

    public String getDescription(){
        return description;
    }

    public abstract double cost();
}
