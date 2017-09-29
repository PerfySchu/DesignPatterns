package factory.factories;

import factory.human.Boy;
import factory.human.Girl;

/**
 * 文件名：HumanFactory.java
 * 创建日期：2017/9/29 15:47
 * 说明：
 *
 * @author shupf@szkingdom.com
 */
public interface HumanFactory {

    public Boy getBoy();

    public Girl getGirl();
}
