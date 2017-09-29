package agent;

import agent.abstractDuck.Duck;
import agent.ducks.GreenDuck;
import agent.ducks.RedDuck;
import agent.ducks.ModelDuck;
import agent.flyImpl.FlyNoWay;
import agent.flyImpl.FlyWithRocket;
import agent.flyImpl.FlyWithWings;
import agent.quackImpl.MuteQuack;
import agent.quackImpl.Quack;
import agent.quackImpl.SQuack;

/**
 * ***************************************************************************
 * ͳһ֧��ƽ̨(KUPP) V1.0
 * Copyright (C) 1998-����, SHENZHEN KINGDOM COM Ltd.
 * All Rights Reserved.
 * ===========================================================================
 * �� �� ����TestMain.java
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
public class TestMain {
    public static void main(String[] args) {

        Duck redDuck = new RedDuck();
        redDuck.display();
        redDuck.setFlyBehavior(FlyWithRocket.getInstance());
        redDuck.setQuackBehavior(new SQuack());
        redDuck.performFly();
        redDuck.performQuack();
        redDuck.setFlyBehavior(FlyNoWay.getInstance());
        redDuck.setQuackBehavior(new Quack());
        redDuck.performFly();
        redDuck.performQuack();

        Duck modelDuck = new ModelDuck();
        modelDuck.display();
        modelDuck.setFlyBehavior(FlyWithWings.getInstance());
        modelDuck.setQuackBehavior(new MuteQuack());
        modelDuck.performQuack();
        modelDuck.performFly();

        Duck greenDuck = new GreenDuck();
        greenDuck.display();
        greenDuck.setFlyBehavior(FlyWithWings.getInstance());
        greenDuck.setQuackBehavior(new SQuack());
        greenDuck.performFly();
        greenDuck.performQuack();

    }
}
