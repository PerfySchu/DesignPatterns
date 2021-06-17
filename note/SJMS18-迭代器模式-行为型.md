###  迭代器模式
#### 原理与实现
##### 原理
- 迭代器模式也叫游标模式，将集合的遍历从集合本身抽离出来，分为容器和迭代器两个部分，让职责更单一

- 容器和迭代器又分别抽象出容器接口和迭代器接口，以达到面向接口编程

  ![iterator](.\image\iterator.png)

##### 实现案例

###### 基于ArrayList 和 LinkedList 的抽象实现

- 接口定义

  - 方案一将游标的下移和获取当前位置元素分开两个方法，比方案二合在一起的做法更为灵活

  ```java
  //方案一
  public interface Iterator<E>{
      //判断是否有下一个
      boolean hasNext();
      //游标下移一位
      void next();
      //获取当前位置元素
      E currentItem();
  }
  
  //方案二
  public interface Iterator<E>{
      //判断是否有下一个
      boolean hasNext();
      //游标下移一位，返回下一位的元素
      E next();
  }
  ```

- 方案一实现方式

  ```java
  public class ArrayIterator<E> implements Iterator<E>{
      private int cursor;
      private ArrayList<E> arrayList;
      
      public ArrayIterator(ArrayList<E> arrayList){
          this.cursor = 0;
          this.arrayList = arrayList;
      }
      
      @Override
      public boolean hasNext(){
          //为什么不是 size -1 呢 ？？？
          return cursor != arrayList.size();
      }
      
      @Override
      public void next(){
          cursor++;
      }
      
      @Override
      public E currentItem(){
          if(cursor >= arrayList.size()){
              throw new NoSuchElementException();
          }
          return araryList.get(cursor);
      }
  }
  
  public class Demo{
      public static void main(String[] args){
          ArrayList<String> names = new ArrayList<>();
          names.add("xyz");
          names.add("123");
          names.add("perfy");
      }
      
      Iterator<String> iterator = new ArrayIterator(names);
      while(iterator.hasNext()){
          system.out.println(iterator.currentItem());
          iterator.next();
      }
  }
  ```

- 上述代码中，iterator需要显示创建，创建时传入容器对象，可以考虑直接在容器对象中包含迭代器的创建过程，已做到隐藏迭代器的创建细节

  ```java
  public interface List<E>{
      Iterator iterator();
      //其他部分省略
  }
  
  public class ArrayList<E> implements List<E>{
      public Iterator iterator(){
          return new ArrayIterator(this);
      }
      //其他部分省略
  }
  
  public class Demo{
      public static void main(String[] args){
          List<String> names = new ArrayList<>();
          names.add("abc");
          names.add("perfy");
          names.add("sunshine");
          
          Iterator<String> iterator = names.iterator();
          while(iterator.hasNext()){
              System.out.println(iterator.currentItem());
              iterator.next();
          }
      }
  }
  
  
  ```



###### 迭代器设计基本思路

- 创建基本方法 hasNext()，currentItem()，next()

- 容器（集合）对象依赖注入进迭代器中

- 容器（集合）中封装迭代器的创建方法iterator()

  ![iterator2](.\image\iterator2.png)



##### 迭代器优劣

###### 集合遍历方式对比

```java
List<String> name = new ArrayList<>();
names.add("abc");
names.add("123");
names.add("perfy");

//方法一  for
for(int i=0; i<names.size(); i++){
    System.out.println(names.get(i) + ",");
}

//方法二 foreach
for(String name : names){
    System.out.println(names.get(i) + ",");
}

//方法三 迭代器
Iterator<String> iterator = names.iterator();
while(iterator.hasNext()){
    System.out.println(iterator.next() + ",");
}
```

- java中 foreach 是对 迭代器的封装，底层是一样的
- 对于数组链表这种比较简单的数据结构，使用for循环确实比迭代器要简洁，但是当数据结构复杂之后（比如说树和图），遍历方式复杂多样，不可能直接让使用者手写遍历方法，因此需要实现封装迭代函数
- 另外，当针对某个容器的迭代方式改变时，只需要扩展其迭代器，而不需要调整业务代码
- 每个迭代器的游标相互独立，多个迭代器对同一个的容器的遍历不会相互影响



#### 迭代器的失败机制

##### fail-fast

###### 集合遍历时删除

- 在迭代过程中发生删除动作，不一定会导致错误，运行结果也可能是正确的，视具体情况而定

- 数组的删除操作会涉及到数据搬移，即当删除 i 位置的元素时，原本 i+1 及其后的元素会往前平移补位
- 因此，当删除的元素位置在游标前，会导致游标所指元素往后挪位，如果删除的元素位置在游标后，则不会有影响

###### 集合遍历时添加

- 跟遍历时删除元素一样，遍历时添加元素也会导致数据搬移，游标位置前添加，会整体向后平移，导致问题，游标位置后添加则没有影响
- 无论是添加还是删除都会导致结果无法预期。相比于直接出现错误，未决的结果更加可怕

###### 未决结果解决方案

- 1、遍历时不允许添加和删除元素
- 2、添加或删除元素之后，让遍历报错
- 在可行性方面，第二种方案是比较合适

###### modCount

- 在 ArrayList 中定义一个成员变量 modCount 用来记录被修改的次数，每次添加或删除将 modCount 加 1

- 创建迭代器时，将 modCount 值传递给迭代器中的  expectedModCount 成员变量，

- 在每次调用迭代器中的方法时，检查 expectedModCount 是否等于 modCount，即检查迭代途中是否发生过增删，如果不相等直接抛出异常

  ```java
  public class ArrayIterator implements Iterator{
      private int cursor;
      private ArrayList arrayList;
      private int expectedModCount;
      
      public ArrayIterator(ArrayList arrayList){
          this.cursor = 0;
          this.arrayList = arrayList;
          this.expectedModCount = arrayList.modCount;
      }
      
      @Override
      public boolean hasNext(){
          checkModCount();
          return cursor < arrayList.size();
      }
      
      @Override
      public void next(){
          checkModCount();
          cursor++;
      }
      
      @Override
      public Object currentItem(){
          checkModCount();
          return arrayList.get(cursor);
      }
      
      @Override
      public void checkModCount(){
          if(arrayList.modCount != expectedModCount){
              thorw new ConcurrentModificationException();
          }
      }
  }
  
  public class Demo{
      public static void main(String[] args){
          List<String> names = new ArrayList<>();
          names.add("a");
          names.add("b");
          names.add("c");
          names.add("d");
          names.add("e");
          
          Iterator<String> iterator = names.iterator();
          iterator.next();
          names.remove("a");
          iterator.next();//此处会抛出异常 ConcurrentModificationException
      }
  }
  ```

  

##### 遍历的同时安全的删除集合元素

###### Java 提供的解决方案

- 在迭代器中封装一个 remove() 方法，用来在集合遍历中安全的删除当前游标的前一个元素

- 有操作限制，一个next() 后只能紧跟着一个 remove() 操作，连续使用 remove() 会报错。很鸡肋

  ```java
  public class Demo{
      public static void main(String[] args){
          List<String> names = new ArrayList<>();
          names.add("a");
          names.add("b");
          names.add("c");
          names.add("d");
          names.add("e");
          
          Iterator<String> iterator = names.iterator();
          iterator.next();
          iterator.remove();
          iterator,remove();//会抛出异常 IllegalStateException
      }
  }
  
  public class ArrayList<E>{
      transient Object[] elemetnData;
      private int size;
      
      public Iterator<E> iterator(){
          return new Itr();
      }
      
      private class Itr implements Iterator<E> {
          int cursor;
          int lastRet = -1;//游标上一次的位置（用来做删除使用）
          int expectedModCount = modCount;
          
          Itr(){}
          
          public boolean hasNext(){
              return cursor != size;
          }
          
          @SuppressWarnings("unchecked")
          public E next(){
              checkModCount();
              int i = cursor;
              if(i > size){
                  throw new NoSuchElementException();
              }
              Object[] elementData = ArrayList.this.elementData;
              if(i >= elementData.length){
                  throw new ConcurrentModificationException();
              }
              cursor = i+1;
              return (E)elementData[lastRet = i];
          }
          
          public void remove(){
              if(lastRet < 0){
                  throw new IllegalStateException();
              }
              checkModCount();
              
              try{
                  ArrayList.this.remove(lastRet);
                  //删除元素之后，将游标位置前移，以防数据平移导致的下标挪位，使某个元素遍历不到
                  cursor = lastRet;
                  //每次删除元素后都将 lastRet 置为-1，避免重复调用 remove
                  lastRet = -1;
                  expectedModCount = modCount;
              }catch(IndexOutOfBoundsException ex){
                  throw new ConcurrentModificationException();
              }
          }
      }
  }
  ```

  

#### 支持快照的迭代器

##### 快照迭代器

- 迭代器操作的对象是容器的副本（快照），容器内的元素发生增删，不会影响快照的内容。因此可以避免增删操作对迭代的影响

##### 实现方式

###### 代码框架

```java 
public ArrayList<E> implements List<E>{
    
    @Override
    public void add(E obj){
        //TODO:待实现
    }
    
    @Override
    public void remove(E obj){
        //TODO:待实现
    }
    
    @Override
    public Iterator<E> iterator(){
        return new SnapshotArrayIterator(this);
    }
}

public class SnapshopArrayIterator<E> implements Iterator<E>{
    
    @Override
    public boolean hasNext(){
        //TODO:待完善
    }
    
    @Override
    public E next(){ //返回当前元素，并将游标后移一位
        //TODO:待完善
    }
}
```

###### 实现方案一

- 直接在迭代器中拷贝一份容器中的元素，后续的遍历都基于这个备份来进行
- 方案简单，但是需要拷贝容器，内存占用大

```java
public class SnapshotArrayIterator<E> implements Iterator<E>{
    private int cursor;
    private ArrayList<E> snapshot;
    
    public SnapshotArrayIterator(ArrayList<E> arrayList){
        this.cursor = 0;
        this.snapshot = new ArrayList<>();
        this.snapshot.addAll(arrayList);
    }
    
    @Override
    public boolean hasNext(){
        return cursor < snapshot.size();
    }
    
    @Override
    public E next(){
        E currentItem = snapshot.get(cursor);
        cursor++;
        return currentItem;
    }
}
```

###### 实现方案二

- 给容器中的每个元素加上两个时间戳，一个是创建时间戳 **addTime**，一个是删除时间戳 **deleteTime**
- 元素的删除只做逻辑删除，并不会实际从容器删除
- 迭代器创建时记录当前时间戳 **snapshotTime**，迭代时，只获取符合 `addTime < snapshotTime < deleteTime` 条件的元素作为迭代元素
- 注：该方案未考虑扩容的情况

```java 
public class ArrayList<E> implements List<E>{
    private static final int DEFAULT_CAPACITY = 10;
    private int actualSize;
    private int totalSize;
    
    private Object[] elements;
    private long[] addTimestamps;
    private long[] delTimestamps;
    
    public ArrayList(){
        this.elements = new Object[DEFAULT_CAPACITY];
        this.addTimestamps = new long[DEFAULT_CAPACITY];
        this.delTimestamps = new long[DEFAULT_CAPACITY];
        this.totalSize = 0;
        this.actualSize = 0;
    }
    
    @Override
    public void add(E obj){
        elements[totalSize] = obj;
        addTimestamps[totalSize] = System.currentTimeMillis();
        delTimestamps[totalSize] = Long.MAX_VALUE;
        totalSize++;
        actualSize++;
    }
    
    @Override
    public void remove(E obj){
        for(int i=0; i<totalSize; ++i){
            if(elements[i].equals(obj)){
                delTimestamps[i] = System.currentTimeMillis();
                actualSize--;
            }
        }
    }
    
    public int actualSize(){
        return this.actualSize();
    }
    
    public int totalSize(){
        return this.totalSize();
    }
    
    public E get(int i){
        if(i >= totalSize){
            throw new IndexOutOfBoundsException();
        }
        return (E) elements[i];//此处不考虑已删除元素 ？？？
    }
    
    public getAddTimestamp(int i){
        if(i >= totalSize){
            throw new IndexOutOfBoundsException();
        }
        return addTimestamps[i];
    }
    
    public long getDelTimestamp(int i){
        if(i >= totalSize){
            throw new IndexOutOfBoundsException();
        }
        return delTimestamps[i];
    }
}

public class SnapshotArrayIterator<E> implements Iterator<E>{
    private long snapshotTimestamp;
    private int cursorInAll;//整个容器中的下标（非快照下标）
    private int leftCount;//快照中剩余的元素数量
    priavte ArrayList<E> arrayList;
    
    public SnapshotArrayIterator(ArrayList<E> arrayList){
        this.snapshotTime = System.currentTimeMillis();
        this.cursorInAll = 0;
        this.leftCount = arrayList.size();
        this.arrayList = arrayList;
        
        justNext();//跳至下一个元素
    }
    
    @Override
    public boolean hasNext(){
        return this.leftCount >=0; //为什么是 >= 而不是 >
    }
    
    @Override
    public E next(){
        E currentItem = arrayList.get(cursorInAll);
        justNext();
        return currentItem;
    }
    
    private void justNext(){
        while(cursorInAll < arrayList.totalSize()){
            long addTimestamp = arrayList.getAddTimestamp(cursorInAll);
            long delTimestamp = arrayList.getDelTimestamp(cusrosInAll);
            if(snapshotTimestamp > addTimestamp && snapshotTimestap < delTimestamp){
                leftCount0--;
                break;
            }
            cursorInAll++;
        }
    }
}
```

- 上述方法虽然支持快照遍历，但是元素没有实际删除，会导致访问元素时无法通过下标直接获取，而是需要先判断元素状态，访问效率大大降低。另外不删除元素也会导致内存占用过大
- 解决方案：在ArrayList中放置两个数组，一个支持标记删除，用来做快照遍历，另一个不支持删除，用来做随机访问

