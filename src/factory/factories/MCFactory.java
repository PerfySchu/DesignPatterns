package factory.factories;

import factory.human.Boy;
import factory.human.Girl;
import factory.human.MCBoy;
import factory.human.MCGirl;

/**
 * 文件名：MCFactory.java
 * 创建日期：2017/9/29 15:43
 * 说明：
 *
 * @author shupf@szkingdom.com
 */
public class MCFactory implements HumanFactory {
    @Override
    public Boy getBoy() {
        return new MCBoy();
    }

    @Override
    public Girl getGirl() {
        return new MCGirl();
    }
}
