package decorator;

/**
 * 文件名：Person.java
 * 创建日期：2017/9/28 10:35
 * 说明：
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
