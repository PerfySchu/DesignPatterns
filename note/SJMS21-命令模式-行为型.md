### 命令模式

#### 定义和原理

##### 定义

- 将请求（命令）封装成一个对象，这样可以使用不同的请求参数化其他对象（将不同的请求依赖注入到其他对象），并且能够支持请求（命令）的队列执行、记录日志、撤销等功能

##### 原理

- 将函数封装成对象，设计一个包含函数的对象，实例化该对象作为参数在函数之间传递。

##### 使用场景

- 控制命令的执行，比如异步、延迟、排队执行命令、撤销重做命令、存储命令、记录命令执行日志等



#### 实战

##### 需求场景

- 游戏服务器轮询获取客户端发来的请求，获取请求后，借助命令模式，将请求包含的数据和处理逻辑封装成命令对象，并存储在队列中，然后从队列中取出一定数量的命令来执行，执行完之后再次轮询请求

##### 代码

```java
public interface Command{
    void execute();
}

//获取宝石
public class GotDiamondCommand implements Command{
    //省略其他部分
    
    public GotDiamondCommand(/*数据*/){
    	//... ...
    }
    
    @Override
    public void execute(){
        //执行相应的逻辑
    }
}

//其他命令对象省略 获得星星，攻击障碍物，存档
//GotStarCommand/HitObstacleCommand/ArchiveCommand

public class GameApplication{
    private static final int MAX_HANDLED_REQ_COUNT_PRE_LOOP = 100;
    private Queue<Command> queue = new LinkedList<>();
    
    public void mainLoop(){
        while(true){
            List<Request> requests = new ArrayList<>();
            
            //省略 request 轮询赋值过程
            
            for(Request request : requests){
                Event event = request.getEvent();
                Command command = null;
                if(event.equals(Event.GOT_DIAMOND)){
                    command = new GotDiamondCommand(/*数据*/);
                }else if(event.equals(Event.GOT_STAR)){
                    command = new GotStarCommand(/*数据*/);
                }else if(event.equals(Event.HIT_OBSTACLE)){
                    command = new HitObstacleCommand(/*数据*/);
                }else if(event.equals(Event.ARCHIVE)){
                    command = new ARCHIVECommand(/*数据*/);
                }//其他的命令逻辑
            }
            queue.add(command);
        }
        
        int handledCount = 0;
        while(handledCount < MAX_HANDLED_REQ_COUNT_PRE_LOOP){
            if(queue.isEmpty){
                break;
            }
           	Command command = queue.poll();
            command.execute();
        }
    }
}
```



