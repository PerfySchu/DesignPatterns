package factory.factories;

import factory.PropertiesLoader;
import factory.hair.HairInterface;
import factory.hair.LeftHair;
import factory.hair.RightHair;

/**
 * 文件名：HairFactory.java
 * 创建日期：2017/9/29 15:14
 * 说明：
 *
 * @author shupf@szkingdom.com
 */
public class HairFactory {

    public HairInterface getHair(String key){
        if(key.equals("left")){
            return new LeftHair();
        }else if(key.equals("right")){
            return new RightHair();
        }

        return null;
    }

    public HairInterface getHairByClass(String className){
        try {
            HairInterface hair = (HairInterface)Class.forName(className).newInstance();
            return hair;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public HairInterface getHairByKey(String key){
        try {
            HairInterface hair = (HairInterface)Class.forName(PropertiesLoader.getClassName(key)).newInstance();
            return hair;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }
}
