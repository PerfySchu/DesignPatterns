### 解释器模式

#### 概念和原理

##### 定义

- 为某个语言定义它的语法表示，并定义一个解释器用来处理这个语法 ？？？

- 此处语言是一个泛指，泛指一起有固定的语法规则的表达式，比如数学计算公式，规则表达式等

##### 先导实例

###### 说明

- 简单的加减乘除四则运算，且不含优先级
- 表达式先写数字，再写运算符，以空格隔开(形如：8 3 2 4 + - *)
- 每次从头部取两个数字，从结尾去一个运算符进行，当前运算结果放入开头位置
- 循环以上步骤

###### 代码

```java
public class ExpressionInterpreter{
    private Deque<Long> numbers = new LinkedList<>();
    
    public long interpret(String expression){
        String[] elements = expression.split(" ");
        int length = elements.length;
        for(int i=0; i<(length+1)/2; i++){
            numbers.addLast(Long.parseLong(elements[i]));
        }
        
        for(int i=(length+1)/2; i<length; ++i){
            String operator = elements[i];
            boolean isValid = "+".equals(operator) || "-".equals(operator) || "*".equals(operator) || "/".equals(operator);
            if(!isValid){
                throw new RuntimeException("Expression is invalid: " + expression);
            }
            
            long number1 = numbers.pollFirst();
            long number2 = numbers.pollFirst();
            long resutl = 0;
            if(operator.equals("+")){
                result = number1 + number2;
            }else if(operator.equals("-")){
                result = number1 - number2;
            }else if(operator.equals("*")){
                result = number1 * number2;
            }else if(operator.equals("/")){
                result = number1 / number2;
            }
            number.addFirst(result);
        }
        
        if(numbers.size() != 1){
            throw new RuntimeException("Expression is invalid: " + expression);
        }
        
        return numbers.pop();
    }
}
```



##### 优化方案

###### 问题分析

- 上述简易的四则运算解析器，将加减乘除都耦合在同一个函数中，在运算语法简单的情况下无问题，但是当语法复杂起来之后，会显得很凌乱，因此可以考虑拆分代码，将逻辑分散到独立的小类中
- 表达式本身由 **数字** 和 **运算符** 构成，因此拆分的子类可以分为两部分，一部分处理数字，一部分处理运算符，细分之后产生5个小类，分别是 NumberExpression，AdditionExpression，SubstractionExpression，MultiplicationExpression，DivisionExpression

###### 重构代码

```java
public interface Expression{
	//解析方法
    long interpret();
}

public class NumberExpression implements Expression{
    private long number;
    
    public NumberExpression(long number){
        this.number = number;
    }
    
    public NumberExpression(String number){
        this.number = Long.parseLong(number);
    }
    
    @Override
    public long interpret(){
        return this.number;
    }
}

public AdditionExpression implements Expression{
    private Expression exp1;
    private Expression exp2;
    
    public AdditionExpression(Expression exp1, Expression exp2){
        this.exp1 = exp1;
        this.exp2 = exp2;
    }
    
    @Overrid
    public long interpret(){
        return exp1.interpret() + exp2.interpret();
    }
}

//减法，乘法和除法代码省略

public class ExpressionInterpreter{
    private Deque<Expression> numbers = new LinkedList<>();
    
    public long interpret(String expression){
        String[] elements = expression.split(" ");
        int length = elements.length;
        for(int i=0; i<(length+1)/2; i++){
            numbers.addLast(new NumberExpression(elements[i]));
        }
        
        for(int i=(length+1)/2; i<length; ++i){
            String operator = elements[i];
            boolean isValid = "+".equals(operator) || "-".equals(operator) || "*".equals(operator) || "/".equals(operator);
            if(!isValid){
                throw new RuntimeException("Expression is invalid: " + expression);
            }
            
            Expression exp1 = numbers.pollFirst();
            Expression exp2 = numbers.pollFirst();
            Expression combinedExp = null;
            if(operator.equals("+")){
                combinedExp = new AdditionExpression(exp1, exp2);
            }else if(operator.equals("-")){
                combinedExp = new SubstractionExpression(exp1, exp2);
            }else if(operator.equals("*")){
                combinedExp = new MultiplicationExpression(exp1, exp2);
            }else if(operator.equals("/")){
                combinedExp = new DivisionExpression(exp2, exp2);
            }
            long result = combinedExp.interpret();
            number.addFirst(new NumberExpression(result));
        }
        
        if(numbers.size() != 1){
            throw new RuntimeException("Expression is invalid: " + expression);
        }
        
        return numbers.pop();
    }
}
```



#### 解释器模式实战

##### 接口告警规则

###### 需求

- 完成一个接口监控告警规则解析器，根据接口调用出错次数和每分钟调用数，来判断是否应该发送告警信息
- 告警规则只负责处理接口信息，不负责收集接口数据，可以理解为数据从其他模块生成好直接送过来
- 假设自定义告警规则只支持 `||、&&、>、<、==`这五个运算符，其中 `< > ==`优先级比`|| &&`要高
- 用户可以自定义需要判断的对象（key）

###### 代码框架

```java
public class AlertRuleInterpreter{
    //形如 key1>100 && key2<1000 || key2==200
    public AlertRuleInterpreter(String ruleExpression){
        //TODO:
    }
    
    public boolean interpret(Map<String, Long> stats){
        //TODO:
    }
}

public class DemoTest{
    public static void main(String[] args){
        String rule = "key1>100 && key2<30 || key3<100 || key4==88";
        AlertRuleInterpreter interpreter = new AlertRuleInterperter(rule);
        Map<String, Long> stats = new HashMap<>();
        stats.put("key1", 101L);
        stats.put"Key3", 121L);
        status.put("key4", 88L);
        boolean alert = interpreter.interpret(stats);
        System.out.println(alert);
    }
}
```

###### 代码实现

```java
public interface Expression{
    boolean interpret(Map<String, Long> stats);
}

//大于表达式解析
public class GreaterExpression implements Expression{
    private String key;
    private long value;
    
    public GreaterExpression(String strExpression){
        String[] elements = strExpression.trim().split("\\s+");
        //表达式必须包含3个元素，且中间元素是大于号
        if(elements.length!=3 || !elements[1].trim().equals(>)){
            throw new RuntimeException("Expression is invalid: " + expression);
        }
        this.key = elements[0].trim();
        this.value = Long.parseLong(elements[2].trim());
    }
    
    public GreaterExpression(String key, long value){
        this.key = key;
        this.value = value;
    }
    
    @Override
    public boolean interpret(Map<String, Long> stats){
        if(!stats.containsKey(key)){
            return false;
        }
        long statValue = stats.get(key);
        return statValue > value;
    }
}

//省略 小于，等于 的代码 LessExpression/EqualExpression

//且表达式解析
public class AndExpression implements Expression{
    private List<Expression> expression = new ArrayList<>();
    
    public AndExpression(String strAndExpression){
        String[] strExpression = strAndExpression.split("&&");
        for(String strExpr : strExpression){
            if(strExpr.contains(">")){
                expression.add(new GreaterExpression(strExpr));
            }else if(strExpr.contains("<")){
                expression.add(new LessExpression(strExpr));
            }else if(strExpr.contains("==")){
                expression.add(new EqualsExpression(strExpr));
            }else{
                throw new RuntimeException("Exception is invalid: " + strAndExpression);
            }
        }
    }
    
    public AndExpression(List<Expression> expressions){
        this.expressions.addAll(expressions);
    }
    
    @Override
    public boolean interpret(Map<String, Long> stats){
        for(Expression expr : expressions){
            // 且逻辑，只要有一个不成立就返回false
            if(!expr.interpret(stats)){
                return false;
            }
        }
        return true;
    }
}

//或表达式解析
public class OrExpression implements Expression{
    private List<Expression> expressions = new ArrayList<>();
    
    public OrExpression(String strOrExpression){
        String[] orExpressions = strOrExpression.split("\\|\\|");
        for(String orExp : prExpressions){
            expression.add(new OrExpression(orExpr));
        }
    }
    
    public OrExpression(List<Expression> expressions){
        this.expressions.addAll(expression);
    }
    
    @Override
    public boolean interpret(Map<String, Long> stats){
        for(Expression expr : expressions){
            //或逻辑，只要有一个成立就返回true
            if(expr.interpret(stats)){
                return true;
            }
        }
        return false;
    }
}

public class AlertRuleInterpreter{
    //规则表达式
    private Expression expression;
    
    public AlertRuleInterpreter(String ruleExpression){
        this.expression = ruleExpression;
    }
    
    //stats 待验证的条件组合
    public boolean interpret(Map<String, Long> stats){
        return expression.interpret(stats);
    }
}
```





