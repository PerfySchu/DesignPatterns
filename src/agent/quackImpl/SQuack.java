package agent.quackImpl;

import agent.quack.QuackBehavior;

/**
 * ***************************************************************************
 * ͳһ֧��ƽ̨(KUPP) V1.0
 * Copyright (C) 1998-����, SHENZHEN KINGDOM COM Ltd.
 * All Rights Reserved.
 * ===========================================================================
 * �� �� ����SQuack.java
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
public class SQuack implements QuackBehavior {
    @Override
    public void quack() {
        System.out.println("SQuack");
    }
}
