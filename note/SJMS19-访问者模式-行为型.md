### 访问者模式

#### 先导案例

##### 问题描述

- 实现一个文件批处理器，可以将不同格式（PDF，PPT，Word）的文件内容读取出来，转存到txt文件中

##### 基础实现

###### 代码

```java
//文件资源抽象类
public abstract class ResourceFile{
    protected String filePath;
    
    public ResourceFile(String filePath){
        this.filePath = filePath;
    }
    
    //提取内容并转存txt
    public abstract void extract2txt();
}

//PPT文件处理
public class PPTFile extends ResourceFile{
    public PPTFile(String filePath){
        super(filePath);
    }
    
    @Override
    public void extract2txt(){
        //TODO：从PPT文件中抽取出内容并存入txt
        
        System.out.println("Extract PPT");
    }
}

//PDF文件处理
public class PdfFile extends ResourceFile{
    public PdfFile(String filePath){
        super(filePath);
    }
    
    @Override
    public void extract2txt(){
        //TODO：从Pdf文件中抽取出内容并存入txt
        
        System.out.println("Extract Pdf");
    }
}


//Word文件处理
public class WordFile extends ResourceFile{
    public WordFile(String filePath){
        super(filePath);
    }
    
    @Override
    public void extract2txt(){
        //TODO：从Word文件中抽取出内容并存入txt
        
        System.out.println("Extract Word");
    }
}

//入口类
public class ToolApplication{
    public static void main(String[] args){
        List<ResourceFile> resourceFiles = listAllResourceFiles(args[0]);
        for(ResourceFile resourceFile : resourceFiles){
            resourceFile.extract2txt();
        }
    }
    
    private static List<ResourceFile> listAllResourceFiles(String resourceDirectory){
        List<ResourceFile> resourceFiles = new ArrayList<>();
        //TODO:一下代码可优化成工厂类
        resourceFiles.add(new PdfFile("a.pdf"));
        resourceFiles.add(new PPTFile("b.ppt"));
        resourceFiles.add(new WordFile("c.word"));
    }
}
```

###### 说明

- 上述代码能满足基础业务需求，但是扩展性比较差，耦合性强
- 如果除了提取文本之外，还需要加上压缩、解压、索引等一些列操作，则每个文件实现类上都需要修改
- 可以考虑将文件结构和文件操作分开，文件的操作抽取出来成单独的类，根据文件类型重载方法



##### 优化方案

###### 代码

```java
public abstract class ResourceFile{
    protected String filePath;
    public ResourceFile(String filePath){
        this.filePath = filePath;
    }
}

//Pdf文件处理
public class PdfFile extends ResourceFile{
    public PdfFile(String filePath){
        super(filePath);
    }
}
//PPT文件和Word文件处理省略

//文件内容提取器
public class Extractor{
    //PPT内容提取
    public void extract2txt(PPTFile pptFile){
        //TODO:提取PPT内容的操作
        System.out.println("Extract ppt");
    }
    //Pdf内容提取
    public void extract2txt(PdfFile pdfFile){
        //TODO:提取PPT内容的操作
        System.out.println("Extract ppt");
    }
    //word内容提取
    public void extract2txt(PPTFile pptFile){
        //TODO:提取Word内容的操作
        System.out.println("Extract word");
    }
}

//入口类
public class ToolApplication{
    public static void main(String[] args){
        Extractor extractor = new Extractor();
        List<ResourceFile resourceFiles = listAllResourceFile(args[0]);
        for(ResourceFile resourceFile : resourceFiles){
            //此行代码编译会报错，因为编译期间无法确认 resourceFiles到底是哪个子类
            extractor.extract2txt(resourceFile);
        }
    }
    
    private static List<ResourceFile> listAllResourceFiles(String resourceDirectory){
        List<ResourceFile> resourceFiles = new ArrayList<>();
        //TODO:一下代码可优化成工厂类
        resourceFiles.add(new PdfFile("a.pdf"));
        resourceFiles.add(new PPTFile("b.ppt"));
        resourceFiles.add(new WordFile("c.word"));
    }
}
```

###### 说明

- extractor.extract2txt(resourceFile); 这行代码会报错，代码编译期间无法判断具体是哪个 resourceFile子类，因此上面的设计方案实际上无法正常工作



##### 访问者模式

###### 代码

```java
//文件资源抽象类
public abstract class ResourceFile{
    protected String filePath;
    public ResourceFile(String filePath){
        this.filePath = filePath;
    }
    abstract public void accept(Visistor visitor);
}

//pdf文件实现类
public class PdfFile extends ResourceFile{
    public PdfFile(String filePath){
        super(filePath);
    }
    
    @Override
    public void accept(Visitor visitor){
        visitor.visit(this);
    }
}
//PPT 和 Word 实现类省略

//访问者接口
public interface Visitor{
    void visit(PdfFile pdfFile);
    void visit(PPTFile pptFile);
    void visit(WordFile wordFile);
}

//文件内容提取器（访问者）
public class Extractor implements Visitor{
    @Override
    public void visit(PPTFile pptFile){
        //PPT文件内容提取
        System.out.println("Extract ppt");
    }
    @Override
    public void visit(PdfFile pdfFile){
        //PDF文件内容提取
        System.out.println("Extract pdf");
    }
    @Override
    public void visit(WordFile wordFile){
        //word文件内容提取
        System.out.println("Extract word");
    }
}

//文件压缩器（访问者）
public class Compressor implements Visitor{
    @Override
    public void visit(PPTFile pptFile){
        //压缩PPT文件
        System.out.println("Compress ppt");
    }
    @Override
    public void visit(PdfFile pdfFile){
        //压缩PDF文件
        System.out.println("Compress pdf");
    }
    @Override
    public void visit(WordFile wordFile){
        //压缩Word文件
        System.out.println("Compress word");
    }
}

public class ToolApplication{
    public static void main(String[] args){
        //文件内容提取
        Extractor extractor = new Extractor();
        List<ResourceFile> resourceFiles = listAllResourceFiles(args[0]);
        for(ResourceFile resourceFile : resourceFiles){
            resourceFile.accept(extractor);
        }
        
        //文件压缩
        Compressor compressor = new Compressor();
        for(ResourceFile resourceFile : resourceFiles){
            resourceFile.accept(compressor);
        }
    }
    
    private static List<ResourceFile> listAllResourceFiles(String resourceDirectory){
        List<ResourceFile> resourceFiles = new ArrayList<>();
        //TODO:一下代码可优化成工厂类
        resourceFiles.add(new PdfFile("a.pdf"));
        resourceFiles.add(new PPTFile("b.ppt"));
        resourceFiles.add(new WordFile("c.word"));
    }
}
```

###### 说明

- 将被操作对象对象抽象成 ResourceFile, 将操作行为抽象成 Visitor
- 将操作(访问)和对象本身(被访问者)解耦，在接口中通过accept()方法来组合

###### 概念

- 允许一个或者多个操作应用到一组对象上，将操作和对象本身解耦。保持操作和对象的职责单一性，利于扩展

![visitor1](.\image\visitor1.png)



#### Single Dispatch 和 Double Dispatch

##### Single Dispatch

- 存在多态的情况下，运行哪个对象，根据对象**运行时**的类型决定
- 运行对象的哪个方法，根据**编译时**方法的参数决定

##### Double Dispatch

- 存在多态的情况下，运行哪个对象，根据对象**运行时**的类型决定
- 运行对象的哪个方法，根据**运行时**方法的参数决定

##### 两者差别

- Single： 只有对象是在运行时确定的，方法在编译期就已经确定
- Double： 对象和方法都是在运行时确定
- 也即是说 Single 和 Double 的区别在于运行时状态由单一参数决定还是两个参数决定

##### 结论

- 支持双分派的语言，使用哪个对象和使用对象的哪个方法，都可以在运行期间动态决定，**因此不需要访问者模式**，下述问题将不复存在

  ```java
   for(ResourceFile resourceFile : resourceFiles){
       //此行代码编译会报错，因为编译期间无法确认 resourceFiles到底是哪个子类
       extractor.extract2txt(resourceFile);
   }
  ```

- 双分派情况下，ResourceFile在运行期间动态变化，可以调用到不同文件的 extract2txt 重载方法，因此不需要访问者模式

