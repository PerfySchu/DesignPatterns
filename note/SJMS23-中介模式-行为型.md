### 中介模式

#### 原理和实现

##### 原理

- 定义一个单独的中介对象，来分装一组对象之间的交互。将这组对象之间的交互委派给与中介对象交互，以避免对象之间直接交互

![agency](D:\Notes\设计模式\image\agency.jpg)

##### 应用示例

###### 飞机与中控塔台

- 飞行途中需要时刻知道其他飞机的位置，以避免航道互相干扰
- 飞机之间不直接交互，而是直接请求塔台，报告自己的位置和获取其他飞机的位置
- 如果飞机间直接交互，通讯网络会非常复杂和凌乱，而使用中心塔台则每个飞机只需要和塔台交互

###### UI页面组件与事件处理

- 在一个复杂的页面中，一个组建的操作会影响其他组件的显示情况。比如点击登录，显示登录相关表单，隐藏注册表单；点击注册，隐藏登录表单，显示注册表单

- 组件和组件之间相互依赖，相互操作

  ```java
  public class UIControl{
      private static final String LOGIN_BTN_ID = "login_btn";
      private static final String REG_BTN_ID = "reg_btn";
      private static final String USERNAME_INPUT_ID = "username_input";
      private static final String PASSWORD_INPUT_ID = "password_input";
      private static final String REPEAT_PASSWORD_INPUT_ID = "repeat_password_inpupt";
      private static final String HINT_TEXT_ID = "hint_text";
      private static final String SELECTION_ID = "selection";
      
      public static void main(String[] args){
          Button loginButton = (Button)findViewById(LOGIN_BTN_ID);
          Button regButton = (Button)findViewById(REG_BTN_ID);
          Input usernameInput = (Input)findViewById(USERNAME_INPUT_ID);
          Input passwordInput = (Input)findViewById(PASSWORD_INPUT_ID);
          Input repeatedPswdInput = (Input)findViewById(REPEAT_PASSWORD_INPUT_ID);
          Input hintText = (Input)findViewById(HINT_TEXT_ID);
          Selection selection = (Selection)findViewById(SELECTION_ID);
          
          //登录按钮事件处理
          loginButton.setOnClickListener(new OnClickListener(){
              @Override
              public void onClick(View v){
                  String username = usernameInput.text();
                  String password = passwordInput.text();
                  //其他校验业务逻辑
              }
          });
           
          //其他按钮和组价事件处理... 略
      }
  }
  ```

- 上述代码中，组件定义和组件相互控制耦合在一起，可以使用中介模式，将组件之间的相互控制抽离到中介对象中，组件只需要跟中介交互，代码如下

  ```java
  public interface Mediator{
      void handleEvent(Component component, String event);
  }
  
  public class LandingPageDialog implements Mediator{
      private Button loginButton;
      private Button regButton;
      private Selection selection;
      private Input usernameInput;
      private Input passwordInput;
      private Input repeatedPswdInput;
      private Text hintText;
      
      @Override
      public void handleEvent(Component component, String event){
          if(component.equals(loginButton)){
              String username = usernameInput.text();
              String password = passwordInput.text();
              //TODO: 其他业务逻辑校验
          }else if(component.equals(regButton)){
              //TODO: 获取usernameInput, passwordInput, repeatedPswdInput数据
              //TODO: 其他校验
          }else if(component.equals(selection)){
              String selectedItem = selection.select();
              if(selectedItem.equals("login")){
                  usernameInput.show();
                  passwordInput.show();
                  repeatedPswdInput.hide();
                  hintText.hide();
                  //TODO: 其他页面逻辑
              }else if(selectedItem.equals("register")){
                  //TODO: 处理注册表单的显示和隐藏
              }
          }
      }
  }
  
  public class UIControl{
      private static final String LOGIN_BTN_ID = "login_btn";
      private static final String REG_BTN_ID = "reg_btn";
      private static final String USERNAME_INPUT_ID = "username_input";
      private static final String PASSWORD_INPUT_ID = "password_input";
      private static final String REPEAT_PASSWORD_INPUT_ID = "repeat_password_inpupt";
      private static final String HINT_TEXT_ID = "hint_text";
      private static final String SELECTION_ID = "selection";
      
      public static void main(String[] args){
          Button loginButton = (Button)findViewById(LOGIN_BTN_ID);
          Button regButton = (Button)findViewById(REG_BTN_ID);
          Input usernameInput = (Input)findViewById(USERNAME_INPUT_ID);
          Input passwordInput = (Input)findViewById(PASSWORD_INPUT_ID);
          Input repeatedPswdInput = (Input)findViewById(REPEAT_PASSWORD_INPUT_ID);
          Input hintText = (Input)findViewById(HINT_TEXT_ID);
          Selection selection = (Selection)findViewById(SELECTION_ID);
          
          Mediator dialog = new LandingPageDialog();
          dialog.setLoginButton(loginButton);
          dialog.setRegButton(regButton);
          dialog.setUsernameInput(usernameInput);
          dialog.setPasswordInput(passwordInput);
          dialog.setRepeatedPswdInput(repeatedPasswordInput);
          dialog.setHintText(hintText);
          dialog.setSelection(selection);
          
          //登录按钮事件处理
          loginButton.setOnClickListener(new OnClickListener(){
              @Override
              public void onClick(View v){
                  dialog.handleEvent(loginButton, "click");
              }
          });
       
          regButton.setOnClickListener(new OnClickListener(){
             @Override
              public void onClick(View v){
                  dialog.handleEvent(regButton, "click");
              }
          });
           
          //其他按钮和组件事件处理... 略
      }
  }
  ```

###### 优劣分析

- 中介模式虽然能解决组件之间的复杂操作网络，但是需要一个中控上帝对象，当逻辑复杂时，这个中控对象会非常庞大，因此中介模式的使用也是要酌情而定，在交互的负责度和中控的复杂度之间做好取舍

#### 中介模式和观察者模式

- 观察者模式中，观察者与被观察这一般都是单向关系，参与者之间的交互关系比较有条理
- 中介模式的参与者之间有复杂的相互调用关系，因为太过复杂，所以才引入上帝对象，来统一调用。
- 两者都是为了让参与者之间解耦，简化交互关系，但是应用场景存在区别