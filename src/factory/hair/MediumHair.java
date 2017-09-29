package factory.hair;

/**
 * 文件名：MediumHair.java
 * 创建日期：2017/9/29 15:37
 * 说明：
 *
 * @author shupf@szkingdom.com
 */
public class MediumHair implements HairInterface {
    @Override
    public void draw() {
        System.out.println("draw a medium hairStyle");
    }
}
