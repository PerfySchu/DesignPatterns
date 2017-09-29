package agent.flyImpl;

import agent.fly.FlyBehavior;

/**
 * ***************************************************************************
 * ͳһ֧��ƽ̨(KUPP) V1.0
 * Copyright (C) 1998-����, SHENZHEN KINGDOM COM Ltd.
 * All Rights Reserved.
 * ===========================================================================
 * �� �� ����FlyWithWings.java
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
public class FlyWithWings implements FlyBehavior {
    private static FlyWithWings flyWithWings = new FlyWithWings();
    public static FlyWithWings getInstance(){
        return flyWithWings;
    }

    private FlyWithWings(){}
    @Override
    public void fly() {
        System.out.println("I am flying with my wind, xiuxiuxiu...");
    }
}
