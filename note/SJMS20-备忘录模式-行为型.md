### 备忘录模式（快照模式）

#### 原理与实现

##### 概念

- 在不违背封装原则的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态，以便后续恢复这个对象到之前的状态

##### 案例

###### 需求

- 实现一个简单的文本编辑工具，可以输入，输出文本内容，并支持撤销输入的内容
- 包含三个操作：普通的文本键入，列出文本内容 :list指令，撤销输入 :undo指令
- 备忘录模式本身很灵活，没有固定的代码范式

###### 代码实现

```java
//文本实体
public class InputText{
    private StringBuilder text = new StringBuilder();
    
    public String getText(){
        return text.toString();
    }
    
    public void append(String input){
        text.append(input);
    }
    
    public void setText(String text){
        this.text.replace(0, this.text.length(), text);
    }
}

//快照管理
public class SnapshotHolder{
    private Stack<InputText> snapshots = new Stack<>();
    
    public InputText popSnapshot(){
        return snapshots.pop();
    }
    
    //深拷贝一份副本
    public void pushSnapshot(InputText inputText){
        InputText deepClonedInputText = new InputText();
        deepClonedInputText.setText(inputText.getText());
        snapshots.push(deepClonedInputText);
    }
}

public class ApplicantMain{
    public static void main(String[] args){
        InputText inputText = new InputText();
        SnapshotHolder snapshotHolder = new SnapshotHolder();
        Scanner scanner = new Scanner(System.in);
        wihle(scanner.hasNext){
            String input = scanner.next();
            if(input.equals(":list")){
                System.out.println(inputText.getText());
            }else if(input.equals(":undo")){
                InputText snapshot = snapshotHolder.popSnapshot();
                inputText.setText(snapshot.getText());
            }else if(input.equals(":quit")){
                break;
            }else{
                snapshotHolder.pushSnapshot(inputText);
                inputText.append(input);
            }
        }
    }
}
```

###### 说明

- 上述代码InputText对外暴露了set方法，可能会被其他业务使用，违背的封装的原则
- 快照本身应该是不可变对象，而上述直接使用InputText对象的备份作为快照，会受到InputText对外暴露的接口影响内容，因此也违背了封装原则

##### 优化实现

###### 代码

```java
//文本定义
public class InputText{
    private StringBuilder text = new StringBuilder();
    
    public String getText(){
        return text.toString();
    }
    
    public void append(String input){
        text.append(input);
    }
    
    public Snapshot createSnapshot(){
        return new Snapshot(text.toString());
    }
    
    public void restoreSnapshot(Snapshot snapshot){
        this.text.replace(0, this.text.length(), snapshot.getText());
    }
}

//快照定义
public class Snapshot{
    private String text;
    
    public Snapshot(String text){
        this.text = text;
    }
    
    public String getText(){
        return this.text;
    }
}

public class SnapshotHolder{
    private Stack<Snapshot> snapshots = new Stack<>();
    
    public Snapshot popSnapshot(){
        return snapshots.pop();
    }
    
    public void pushSnapshot(Snapshot snapshot){
        snapshots.push(snapshot);
    }
}

public class ApplicationMain{
    public static void main(String[] args){
        InputText inputText = new InputText();
        SnapshotHolder holder = new SnapshotHolder();
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String input = scanner.next();
            if(input.equals(":list")){
                System.out.println(inputText.getText());
            }else if(input.equals(":undo")){
                Snapshot snapshot = holder.popSnapshot();
                inputText.restoreSnapshot(snapshot);
            }else if(input.equals(":quit")){
                return;
            }else{
                holder.pushSnapshot(inputText.createSnapshot());
                inputText.append(input);
            }
        }
    }
}
```

#### 优化

##### 问题

- 当备份频率高，且备份数据量大的时候，会出现备份消耗的时间过长，空间过大

##### 方案

- 当撤销操作仅支持顺序操作的时候，可以采用**高频率增量备份**的方式来进行，记录当前的操作以及操作引起的变化即可
- 而当撤销恢复操作可以跨越版本，直接恢复到某个时间节点时，则必须有全量备份，因此要采用**低频全量备份**+**高频增量备份**结合的方式。需要恢复时，还原到需要恢复时间最近的全量版本，然后结合增量备份完成