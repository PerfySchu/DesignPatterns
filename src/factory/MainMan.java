package factory;

import com.sun.org.apache.xpath.internal.SourceTree;
import factory.factories.HNFactory;
import factory.factories.HairFactory;
import factory.factories.HumanFactory;
import factory.factories.MCFactory;
import factory.hair.LeftHair;
import factory.hair.MediumHair;
import factory.hair.RightHair;
import factory.human.Boy;
import factory.human.Girl;

/**
 * 文件名：MainMan.java
 * 创建日期：2017/9/29 15:13
 * 说明：
 *
 * @author shupf@szkingdom.com
 */
public class MainMan {

    public static void main(String[] args) {
        HairFactory hairFactory = new HairFactory();
        LeftHair leftHair = (LeftHair) hairFactory.getHair("left");
        leftHair.draw();

        RightHair rightHair = (RightHair) hairFactory.getHairByClass("factory.hair.RightHair");
        rightHair.draw();


        MediumHair mediumHair = (MediumHair) hairFactory.getHairByKey("medium");
        mediumHair.draw();


        HumanFactory humanFactory = new MCFactory();
        Girl girl = humanFactory.getGirl();
        girl.draw();

        humanFactory = new HNFactory();
        Boy boy = humanFactory.getBoy();
        boy.draw();
    }


}
