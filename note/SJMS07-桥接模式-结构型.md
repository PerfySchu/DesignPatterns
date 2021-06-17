#### 桥接模式（桥梁模式）

##### 概念及原理

- 一个类存在两个或多个独立变化的维度，可以通过组合的方式，让这两个或多个维度可以进行独立扩展。（将抽象和实现解耦，让它们可以独立变化？？？）
- 我的理解：将相关联的两个模块分别抽象，通过组合的方式形成一个桥接器。两个模块可独立在各自的抽象基础上实现具体的业务，模块间通过桥接器来沟通调用

##### 示例

- JDBC 与 数据库驱动 Driver

![image-20210410172030002](.\image\bridge.png)

- 告警与通知（多种告警类型，多种通知渠道）

```java
//消息通知接口
public interface MsgSender{
    void send(String message);
}
//电话通知
public class TelephoneMsgSender implements MsgSender{
    private List<String> telephone;
    
    public TelephoneMsgSender(List<String> telephone){
        this.telephone = telephone;
    }
    
    @Override
    public void send(String message){
        //... ...
    }
}
//邮件通知
public class EmailMsgSender implements MsgSender{
	//与电话通知结构类似
}
//微信通知
public class EmailMsgSender implements MsgSender{
	//与电话通知结构类似
}

//相当于一个桥接器
public abstract class Notifacation{
    protected MsgSender msgSender;
    public Notification(MsgSender msgSender){
        this.msgSender = msgSender;
    }
    public abstract void notify(String message);
}

//严重消息通知
public class SevereNotification extends Notification{
    public SevereNotification(MsgSender msgSender){
        super(msgSender);
    }
    @Override
    public void notify(String message){
        msgSender.send(message);
    }
}
//紧急消息通知
public class UrgencyNotification extends Notification{
   //... ... 与严重消息通知结构一致
}
//一般消息通知
public class NormalNotification extends Notification{
   //... ... 与严重消息通知结构一致
}
```

- 上述代码案例中，通过提取出一个桥接器 **Notification**，实现了 **消息类型** 和 **通知途经** 的桥接，两端均可根据需要实现具体的业务需求，而且可以通过配置的方式灵活组合。