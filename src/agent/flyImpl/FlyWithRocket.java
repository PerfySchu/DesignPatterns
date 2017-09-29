package agent.flyImpl;

import agent.fly.FlyBehavior;

/**
 * ***************************************************************************
 * 统一支付平台(KUPP) V1.0
 * Copyright (C) 1998-至今, SHENZHEN KINGDOM COM Ltd.
 * All Rights Reserved.
 * ===========================================================================
 * 文 件 名：FlyWithRocket.java
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
