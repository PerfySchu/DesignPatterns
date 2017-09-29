package agent.flyImpl;

import agent.fly.FlyBehavior;

/**
 * ***************************************************************************
 * 统一支付平台(KUPP) V1.0
 * Copyright (C) 1998-至今, SHENZHEN KINGDOM COM Ltd.
 * All Rights Reserved.
 * ===========================================================================
 * 文 件 名：FlyNoWay.java
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
