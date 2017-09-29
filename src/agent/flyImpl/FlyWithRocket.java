package agent.flyImpl;

import agent.fly.FlyBehavior;

/**
 * ***************************************************************************
 * ͳһ֧��ƽ̨(KUPP) V1.0
 * Copyright (C) 1998-����, SHENZHEN KINGDOM COM Ltd.
 * All Rights Reserved.
 * ===========================================================================
 * �� �� ����FlyWithRocket.java
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
public class FlyWithRocket implements FlyBehavior {
    private static FlyWithRocket flyWithRocket = new FlyWithRocket();
    public static FlyWithRocket getInstance(){
        return flyWithRocket;
    }

    private FlyWithRocket(){}

    @Override
    public void fly() {
        System.out.println("I'm flying with Rocket,hahaha...");
    }
}
