package factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 文件名：PropertiesLoader.java
 * 创建日期：2017/9/29 15:26
 * 说明：
 *
 * @author shupf@szkingdom.com
 */
public class PropertiesLoader {

    private static Map<String, String> map;

    static{
        Properties properties = new Properties();
        map = new HashMap<String, String>();

        try {
            InputStream in  = PropertiesLoader.class.getResourceAsStream("type.properties");
            properties.load(in);
            Enumeration enumeration = properties.propertyNames();
            while(enumeration.hasMoreElements()){
                String key = (String) enumeration.nextElement();
                map.put(key, properties.getProperty(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getClassName(String key){
        return map.get(key);
    }
}
