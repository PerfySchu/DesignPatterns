package factory.hair;

/**
 * 文件名：LeftHair.java
 * 创建日期：2017/9/29 15:11
 * 说明：
 *
 * @author shupf@szkingdom.com
 */
public class LeftHair implements HairInterface {
    @Override
    public void draw() {
        System.out.println("draw a left hair");
    }
}
