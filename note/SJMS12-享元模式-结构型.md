#### 享元模式

##### 概念

- 将不可变的对象提取出来，作为可共享的单元，以达到对象复用，节省内存的目的

##### 使用前提

- 系统中存在大量重复对象，并且这些对象不可变。
- 案例
  - 棋牌游戏大厅中，每个棋局中的棋子都是一个对象，如果成千上万的棋局的所有棋子都需要实例化出对象，内存消耗会非常巨大。以象棋为例，棋子本身的属性(棋面，颜色)是不变的，将30种棋子抽离出来作为共享单元，剩下的只需要记录棋子位置即可
  - 文本编辑器中，每一个文字除了文本本身之外，都包含对应的格式信息（字体，颜色等），如果将每个文字当成一个对象，则会产生非常庞大的对象数量。而实际上在一个文本中，可用的格式信息是有限的，因此格式本身可以设置成享元，不同的文字可共享使用

##### 示例

- 棋局

  ```java
  // 享元类
  public class ChessPieceUnit {
      private int id;
      private String text;
      private Color color;
      public ChessPieceUnit(int id, String text, Color color) {
      this.id = id;
      this.text = text;
      this.color = color;
  }
  public static enum Color {
  	RED, BLACK
  }
  // ...省略其他属性和getter方法...
  }
  public class ChessPieceUnitFactory {
      private static final Map<Integer, ChessPieceUnit> pieces = new HashMap<>();
      static {
      pieces.put(1, new ChessPieceUnit(1, "車", ChessPieceUnit.Color.BLACK));
      pieces.put(2, new ChessPieceUnit(2,"馬", ChessPieceUnit.Color.BLACK));
      //...省略摆放其他棋子的代码...
  	}
      public static ChessPieceUnit getChessPiece(int chessPieceId) {
      return pieces.get(chessPieceId);
      }
  }
  public class ChessPiece {
      private ChessPieceUnit chessPieceUnit;
      private int positionX;
      private int positionY;
      public ChessPiece(ChessPieceUnit unit, int positionX, int positionY) {
      this.chessPieceUnit = unit;
      this.positionX = positionX;
      this.positionY = positionY;
      }
  	// 省略getter、setter方法
  }
  public class ChessBoard {
      private Map<Integer, ChessPiece> chessPieces = new HashMap<>();
      public ChessBoard() {
      init();
  	}
      private void init() {
      chessPieces.put(1, new ChessPiece(
      ChessPieceUnitFactory.getChessPiece(1), 0,0));
      chessPieces.put(1, new ChessPiece(
      ChessPieceUnitFactory.getChessPiece(2), 1,0));
      //...省略摆放其他棋子的代码...
  }
   public void move(int chessPieceId, int toPositionX, int toPositionY) {
   //...省略...
   }
  }
  
  ```

- 文本编辑器

  ```java
  public class CharacterStyle {
      private Font font;
      private int size;
      private int colorRGB;
      public CharacterStyle(Font font, int size, int colorRGB) {
          this.font = font;
          this.size = size;
          this.colorRGB = colorRGB;
      }
      @Override
      public boolean equals(Object o) {
      	CharacterStyle otherStyle = (CharacterStyle) o;
      	return font.equals(otherStyle.font) && size == otherStyle.size && colorRGB == otherStyle.colorRGB;
      }
  }
  public class CharacterStyleFactory {
      private static final List<CharacterStyle> styles = new ArrayList<>();
      public static CharacterStyle getStyle(Font font, int size, int colorRGB) {
          CharacterStyle newStyle = new CharacterStyle(font, size, colorRGB);
          for (CharacterStyle style : styles) {
              if (style.equals(newStyle)) {
                  return style;
              }
      	}
      	styles.add(newStyle);
      	return newStyle;
      }
  }
  public class Character {
      private char c;
      private CharacterStyle style;
      public Character(char c, CharacterStyle style) {
          this.c = c;
          this.style = style;
      }
  }
  public class Editor {
      private List<Character> chars = new ArrayList<>();
      public void appendCharacter(char c, Font font, int size, int colorRGB) {
     		Character character = new Character(c, CharacterStyleFactory.getStyle(font
      chars.add(character);
      }
  }
  ```

  

##### 享元和单例

- 享元是为了复用对象，节省内存开销。单例(多例)是为了限制对象个数

##### 享元和缓存

- 缓存是为了提升访问效率，而非复用和节省空间

##### 享元和对象池

- 对象池，线程池等是为了提高对象创建速度，池化技术主要是为了解决时间问题而非共享复用问题

##### Java中的享元

- Java Integer 
  - 在默认情况下，Java会默认创建Integer -128 到 127 共 256 个常量作为享元对象常量池。此外，通过修改虚拟机配置也可以调整初始化的对象数
  - 方法一：  -Djava.lang.Integer.IntegerCache.high=255
  - 方法二：  -XX:AutoBoxCacheMax=255
- Java String
  - 在某个字符串对象被创建时，会放到常量池中，下次创建相同的字符串时，不会新建对象，而是直接将引用指向常量池中已存在的对象
  - 在 Java String 类的实现中，JVM 开辟一块存储区专门存储字符串常量，这块存储区叫作 字符串常量池，类似于 Integer 中的 IntegerCache。不过，跟 IntegerCache 不同的是， 它并非事先创建好需要共享的对象，而是在程序的运行期间，根据需要来创建和缓存字符串 常量。

##### 注意事项

- 享元模式对 JVM 的垃圾回收并不友好，因为享元工厂类一直保存了对享元对象的 引用，这就导致享元对象在没有任何代码使用的情况下，也并不会被 JVM 垃圾回收机制自 动回收掉
- 如果对象的生命周期很短，也不会被密集使用，利用享元 模式反倒可能会浪费更多的内存