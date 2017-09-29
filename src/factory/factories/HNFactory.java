package factory.factories;

import factory.human.Boy;
import factory.human.Girl;
import factory.human.HNBoy;
import factory.human.HNGirl;

/**
 * 文件名：HNFactory.java
 * 创建日期：2017/9/29 15:43
 * 说明：
 *
 * @author shupf@szkingdom.com
 */
public class HNFactory implements HumanFactory {


    @Override
    public Boy getBoy() {
        return new HNBoy();
    }

    @Override
    public Girl getGirl() {
        return new HNGirl();
    }
}
