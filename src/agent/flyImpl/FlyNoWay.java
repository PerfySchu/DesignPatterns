package agent.flyImpl;

import agent.fly.FlyBehavior;

/**
 * ***************************************************************************
 * ͳһ֧��ƽ̨(KUPP) V1.0
 * Copyright (C) 1998-����, SHENZHEN KINGDOM COM Ltd.
 * All Rights Reserved.
 * ===========================================================================
 * �� �� ����FlyNoWay.java
 * ģ�����ƣ�
 * ����˵����
 * ��    �ߣ�shupf@szkingdom.com
 * �������ڣ�2016/11/23
 * �� �� �ţ�1.0
 * �޸���ʷ��
 * �޸�����                       ����                         ����
 * ---------------------------------------------------------------------------
 * 2016/11/23                shupf@szkingdom.com              ��ʼ�汾
 * ****************************************************************************
 */
public class FlyNoWay implements FlyBehavior {

    private static FlyNoWay flyNoWay = new FlyNoWay();

    public static FlyNoWay getInstance(){
        return flyNoWay;
    }

    private FlyNoWay(){}

    @Override
    public void fly() {
        System.out.println("I cannot fly, wuwuwu...");
    }
}
