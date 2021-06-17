### 状态模式

#### 基本概念

##### 有限状态机（Finite State Machine)

- 由状态（State）、事件（Event）、动作（Action）三部分组成，其中，事件也被称为转移条件（Transition Condition），由事件触发状态的转移及动作执行（动作转移非必须）。

  

#### 案例分析
##### 超级马里奥

###### 马里奥状态

- SmallMario，SuperMario，FireMario，CapeMario

###### 状态转移事件

- E1-吃蘑菇，E2-吃斗篷，E3-吃火焰，E4-受伤害(遇到怪物)

![SuperMario](.\image\SuperMario.png)

###### 代码框架

```java
//状态
public enum State{
    SMALL(0),
    SUPER(1),
    FIRE(2),
    CAPE(3);
    
    private int value;
    
    private State(int value){
        this.value = value;
    }
    
    public int getValue(){
        return this.value;
    }
}

public class MarioStateMachine{
    private int score;
    private State currentState;
    
    public MarioStateMachine(){
        this.score = 0;
        this.currentState = State.SMALL;
    }
    
    public void obtainMushRoom(){
        //获取蘑菇
    }
    
    public void obtainCape(){
        //获取斗篷
    }
    
    public void obtainFireFlower(){
        //获取火焰花
    }
    
    public void meetMonster(){
        
    }
    
    public int getScore(){
        return this.score;
    }
    
    public State getCurrentState(){
        return this.currentState;
    }
}

public class ApplicationDemo{
    public void static main(String[] args){
        MarioStateMachine mario = new MarioStateMachine();
        mario.obtainMushRoom();
        int score = mario.getScore();
        State state = mario.getCurrentState();
        System.out.println("mario score: " + score + "; state: " + state);
    }
}
```

###### 实现方案

- 分支逻辑法

  - if-else硬编码，对于状态少的场景可以用，多了则无法维护

  ```java
  public class MarioStateMachine{
      private int score;
      private State currentState;
      
      public MarioStateMachine(){
          this.score = 0;
          this.currentState = State.SMALL;
      }
      
      public void obtainMushRoom(){
          if(currentState.equals(State.SMALL)){
              this.currentState = State.SUPER;
              this.score += 100;
          }
      }
      
      public void obtainCape(){
          if(currentState.equals(State.SMALL) || currentState.equals(State.SUPER)){
              this.currentState = State.CAPE;
              this.score += 200;
          }
      }
      
      public void obtainFireFlower(){
          if(currentState.equals(State.SMALL) || currentState.equals(State.SUPER)){
              this.currentState = State.FIRE;
              this.score += 300;
          }
      }
      
      public void meetMonster(){
          if(currentState.equals(State.SUPER)){
              this.currentState = State.SMALL;
              this.score -= 100;
              return;
          }
          
          if(currentState.equals(State.CAPE)){
              this.currentState = State.SMALL;
              this.score -= 200;
              return;
          }
          if(currentState.equals(State.FIRE)){
              this.currentState = State.SMALL;
              this.score -= 300;
              return;
          }
      }
      
      public int getScore(){
          return this.score;
      }
      
      public State getCurrentState(){
          return this.currentState;
      }
  }
  ```

  

- 查表法

  - 使用二维表来描述状态和事件会触发的结果，直观且方便维护
  - 当事件太复杂，或者要触发的状态转移和动作执行比较多，表格维护就无法满足

  ![MarioTable](.\image\MarioTable.png)

  ```java
  public enum Event{
      GOT_MUSHROOM(0),
      GOT_CAPE(1),
      GOT_FIRE(2),
      MEET_MONSTER(3);
      
      private int value;
      
      private Event(int value){
          this.value = value;
      }
      
      public int getValue(){
          return this.value;
      }
  }
  
  public class MarioStateMachine{
      private int score;
      private State currentState;
      
      /** 状态转换表 */
      private static final State[][] transitionTable = {
          {SUPER, CAPE, FIRE, SMALL},
          {SUPER, CAPE, FIRE, SMALL},
          {CAPE, CAPE, CAPE, SMALL},
          {FIRE, FIRE, FIRE, SMALL}
      };
      /** 分数转换表 */
      private static final int[][] actionTable = {
          {100, 200, 300, 0},
          {0, 200, 300, -100},
          {0, 0, 0, -200},
          {0, 0, 0, -300}
      };
      
      public MarioStateMachine(){
          this.score = 0;
          this.currentState = State.SMALL;
      }
      
      public void obtainMushRoom(){
          executeEvent(Event.GOT_MUSHROOM);
      }
      
      public void obtainCape(){
          executeEvent(Event.GOT_CAPE);
      }
      
      public void obtainFireFlower(){
          executeEvent(Event.GOT_FIRE);
      }
      
      public void meetMonster(){
          executeEvent(Event.MEET_MONSTER);
      }
      
      private void executeEvent(Event event){
          int stateValue = currentState.getValue();
          int eventValue = event.getValue();
          this.currentState = transitionTable[stateValue][eventValue];
          this.score = actionTable[stateValue][eventValue];
      }
      
      public int getScore(){
          return this.score;
      }
      
      public State getCurrentState(){
          return this.currentState;
      }
  }
  ```

  

- 状态模式

  - 将事件触发的状态转移和动作执行，拆分到不同的状态类中，避免分支逻辑判断

    ```java
    //所有状态的接口
    public interface IMario{
        State getName();
        
        void obtainMushRoom();
        void obtainCape();
        void obtainFireFlower();
        void meetMonster();
    }
    
    //初始状态
    public class SmallMario implements IMario{
        private MarioStateMachine stateMachine;
        
        public SmallMario(MarioStateMachine stateMachine){
            this.stateMachine = stateMachine;
        }
        
        @Override
        public State getName(){
            return State.SMALL;
        }
        
        @Override
        public void obtainMushRoom(){
            stateMachine.setCurrentState(new SuperMario(stateMachine));
            stateMachine.setScore(stateMachine.getScore + 100);
        }
        
        @Override
        public void obtainCape(){
            stateMachine.setCurrentState(new CapeMario(stateMachine));
            stateMachine.setScore(stateMachine.getScore + 200);
        }
        
        @Override
        public void obtainFireFlower(){
            stateMachine.setCurrentState(new FireMario(stateMachine));
            stateMachine.setScore(stateMachine.getScore + 300);
        }
        
        @Override
        public void meetMonster(){
            //die
        }
    }
    
    //超级状态
    public class SuperMario implements IMario{
        private MarioStateMachine stateMachine;
        
        public SuperMario(MarioStateMachine stateMachine){
            this.stateMachine = stateMachine;
        }
        
        @Override
        public State getName(){
            return State.SUPER;
        }
        
        @Override
        public void obtainMushRoom(){
            //do nothing
        }
        
        @Override
        public void obtainCape(){
            stateMachine.setCurrentState(new CapeMario(stateMachine));
            stateMachine.setScore(stateMachine.getScore + 200);
        }
        
        @Override
        public void obtainFireFlower(){
            stateMachine.setCurrentState(new FireMario(stateMachine));
            stateMachine.setScore(stateMachine.getScore + 300);
        }
        
        @Override
        public void meetMonster(){
            stateMachine.setCurrentState(new SmallMario(stateMachine));
            stateMachine.setScore(stateMachine.getScore() - 100);
        }
    }
    
    // CapeMario、FireMario 代码省略
    
    public class MarioStateMachine{
        private int score;
        //使用接口及实现类，而不是枚举来表示各种状态
        private IMario currentState;
        
        public MarioStateMachine(){
            this.score = 0;
            this.IMario = new SmallMerio(this);
        }
        
        public void obtainMushRoom(){
            this.currentState.obtainMushRoom();
        }
        
        public void obtainCape(){
            this.currentState.obtainCape();
        }
        
        public void obtainFileFlower(){
            this.currentState.obtainFireFlower();
        }
        
        public void meetMonster(){
            this.currentState.meetMonster();
        }
        
        public int getScore(){
            return this.score;
        }
        
        public State getCurrentState(){
            return this.currentState.getName();
        }
        
        public void setScore(int score){
            this.score = score;
        }
        
        public void setScurrentState(IMario currentState){
            this.currentState = currentState;
        }
    }
    ```

- 优化后的状态模式

  ```java
  public interface IMario{
      State getName();
      void obtainMushroom(MarioStateMachine stateMachine);
      void obtainCape(MarioStateMachine stateMachine);
      void obtainFireFlower(MarioStateMachine stateMachine);
      void meetMonster(MarioStateMachine stateMachine);
  }
  
  public class SmallMario impmements IMario{
      private static final SmallMario instance = new SmallMario();
      private SmallMario(){}
      //单例
      public static SmallMario getInstance(){
          return instance;
      }
      
      @Override
      public State getName(){
          return State.SMALL;
      }
      
      @Override
      public void obtainMushroom(MarioStateMachine stateMachine){
          stateMachine.setCurrentState(SuperMerio.getInstance());
          stateMachine.setScore(sateMachine.getScore + 100)
      }
      
      @Override
      public void obtainCape(MarioStateMachine stateMachine){
          stateMachine.setCurrentState(CapeMario.getInstance());
          stateMachine.setScore(stateMachine.getScore() + 200);
      }
      
      @Override
      public void obtainFireFlower(MarioStateMachine stateMachine){
          stateMachine.setCurrentState(FireMario.getInstance());
          stateMachine.setScore(stateMachine.getScore + 300);
      }
      
      @Override
      public void meetMonster(MarioStateMachine stateMachine){
          // die
      }
  }
  
  //SuperMario, CapeMario, FireMario 代码省略
  
  public class MarioStateMachine{
      private int score;
      private IMario stateMachine;
      
      public MarioStateMachine(){
          this.score = 0;
          this.currentState = SmallMario.getInstance();
      }
      
      public void obtainMushroom(){
          this.currentState.obtainMushroom(this);
      }
      
      public void obtainCape(){
          this.currentState.obtainCape(this);
      }
      
      public void obtainFire(){
          this.currentState.obtainFireFlower(this);
      }
      
      public void meetMonster(){
          this.currentState.meetMonster(this);
      }
      
      public int getScore(){
          return this.score;
      }
      
      public State getCurrentState(){
          return this.currentState.getName();
      }
      
      public void setScore(int score){
          this.score = score;
      }
      
      public void setCurrentState(IMario currentState){
          this.currentState = currentState;
      }
  }
  ```




###### 总结

- 对于状态很多，但是触发的动作和业务逻辑少的（游戏），可以优先使用查表法
- 对于状态不多，但是出发的动作和业务非常复杂的情况（电商），优先使用状态模式

