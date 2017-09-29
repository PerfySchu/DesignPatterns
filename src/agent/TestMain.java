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
 * 统一支付平台(KUPP) V1.0
 * Copyright (C) 1998-至今, SHENZHEN KINGDOM COM Ltd.
 * All Rights Reserved.
 * ===========================================================================
 * 文 件 名：TestMain.java
 * 模块名称：
 * 功能说明：
 * 作    者：shupf@szkingdom.com
 * 创建日期：2016/11/23
 * 版 本 号：1.0
 * 修改历史：
 * 修改日期                       姓名                         内容
 * ---------------------------------------------------------------------------
 * 2016/11/23                shupf@szkingdom.com              初始版本
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
