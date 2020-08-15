# `MVVM`模式实现数据双向绑定

[TOC]

## 一、理论

### 1.1 `MVVM`架构思想

> * 降低耦合：一个`ViewModel`层可以绑定不同的`View`层，当`Model`变化时`View`可以不变；
> * 可重用性：可以把一些视图逻辑放在`ViewModel`层中，让很多`View`重用这些视图逻辑。

![image](https://github.com/tianyalu/NeMvvmDataBinding/raw/master/show/mvvm_structure.png)

### 1.2 `MVVM`与`DataBinding`关系流程

 `MVVM`与`DataBinding`关系流程如下图所示：

![image](https://github.com/tianyalu/NeMvvmDataBinding/raw/master/show/mvvm_databinding_structure.png)

### 1.3 编译时绑定布局与对象

#### 1.3.1 `APT`预编译方式

编译时通过`APT`把布局文件解析成两个文件：`ActivityXxxBinding`和`ActivityXxxBindingImpl`。

两个文件路径如下：

```bash
NeMvvmDatabinding/app/build/generated/data_binding_base_class_source_out/debug/dataBindingGenBaseClassesDebug/out/com/sty/ne/mvvmdatabinding/databinding/ActivityLoginBinding.java
NeMvvmDatabinding/app/build/generated/ap_generated_sources/debug/out/com/sty/ne/mvvmdatabinding/databinding/ActivityLoginBindingImpl.java
```

![image](https://github.com/tianyalu/NeMvvmDataBinding/raw/master/show/apt_generate_databinding.png)

#### 1.3.2 内部处理布局文件控件

布局`xml`文件同样会拆分成两个文件：`activity_mvvm_xxx.xml`和`activity_mvvm_xxx-layout.xml`，第一个文件为每个控件添加了一个`tag`标签，第二个文件是为了能够让`MVVM`能够尽快地找到某个布局中的控件。

两个文件路径如下：

```bash
NeMvvmDatabinding/app/build/intermediates/data_binding_layout_info_type_merge/debug/mergeDebugResources/out/activity_login-layout.xml
NeMvvmDatabinding/app/build/intermediates/incremental/mergeDebugResources/stripped.dir/layout/activity_login.xml
```

![image](https://github.com/tianyalu/NeMvvmDataBinding/raw/master/show/apt_generate_xml.png)

#### 1.3.3 关联`Activity`组件与布局

![image](https://github.com/tianyalu/NeMvvmDataBinding/raw/master/show/relate_activity_layout.png)

#### 1.3.4 生成设置`Model`帮助类

点击`build`按钮的时候，扫描整个项目所有`Module`中`layout`下的所有有`<data>`标签的`xml`文件:

![image](https://github.com/tianyalu/NeMvvmDataBinding/raw/master/show/scan_all_layout_xml.png)

生成`ViewDataBinding`类：

![image](https://github.com/tianyalu/NeMvvmDataBinding/raw/master/show/generate_viewbinding_class.png)

## 二、实践

`MVVM`结合`DataBinding`实现步骤如下：

### 2.1 开启`DataBinding`

在对应模块（`Module`）的`build.gradle`中添加如下代码：

```groovy
android {
  //...
  dataBinding {
    enabled = true
  }
}
```

### 2.2 建立`Model`文件

建立一个`java bean`，比如一个非常简单的`UserInfo`类：

```java
public class UserInfo {
    //被观察的属性（切记：必须是public修饰符，因为是DataBinding的规范 xml中要使用 标识.成员属性）
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> pwd = new ObservableField<>();
}
```

### 2.3 修改布局文件

> 1. 最外层需要用`<layout></layout>`嵌套起来；
>
> 2. 配置`data`-->`variable`信息；
>
> 3. 需要使用`data`-->`variable`-->`name`标识；
>
>    ​	成员属性格式：@{标识.成员属性}      @{`String.valueOf`(标识.成员属性)}     @{标识}

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <!--定义该布局需要绑定的数据名称和类型-->
    <data>
        <variable
            name="loginViewModel"
            type="com.sty.ne.mvvmdatabinding.vm.LoginViewModel" />
    </data>
  	<!-- ... -->
  	<TextView android:text="@{userInfo.name}"/>
</layout>
```

### 2.4 数据的单向绑定与双向绑定

#### 2.4.1 单向绑定

* 方式一

```java
public class UserInfo extends BaseObservable {
    private String name;
    private String pwd;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
        notifyPropertyChanged(BR.pwd);
    }
}
```

```java
userInfo.setName("天涯路");
```

* 方式二

```java
public class UserInfo{
    //被观察的属性（切记：必须是public修饰符，因为是DataBinding的规范 xml中要使用 标识.成员属性）
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> pwd = new ObservableField<>();
}
```

```java
userInfo.name.set("天涯路")
```

#### 2.4.2 双向绑定

在单向绑定的基础上，一个“=”解决问题

```xml
<EditText
          android:id="@+id/et_pwd"
          android:layout_width="match_parent"
          android:layout_height="wrap_content"
          android:layout_marginLeft="20dp"
          android:layout_marginRight="20dp"
          android:layout_marginTop="20dp"
          android:addTextChangedListener="@{loginViewModel.pwdInputListener}"
          android:text="@={loginViewModel.userInfo.pwd}"
          android:hint="请输入密码"
          android:inputType="textPassword"/>
```

## 三、`MVVM`的缺点

`MVVM`的缺点是内存消耗大，其原因如下：

### 3.1 数组过多

```java
//ActivityLoginBindingImpl.java 29行
private ActivityLoginBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
  super(bindingComponent, root, 2
        , (android.widget.Button) bindings[3]
        , (android.widget.EditText) bindings[1]
        , (android.widget.EditText) bindings[2]
       );
  this.btnLogin.setTag(null);
  this.etName.setTag(null);
  this.etPwd.setTag(null);
  this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
  this.mboundView0.setTag(null);
  setRootTag(root);
  // listeners
  invalidateAll();
}
```

### 3.2 全局监听，`runnable`过多，`Model`的每次改变都会刷新界面

```java
//ViewDataBinding.class 154行    
static {
        if (VERSION.SDK_INT < VERSION_CODES.KITKAT) {
            ROOT_REATTACHED_LISTENER = null;
        } else {
            ROOT_REATTACHED_LISTENER = new OnAttachStateChangeListener() {
                @TargetApi(VERSION_CODES.KITKAT)
                @Override
                public void onViewAttachedToWindow(View v) {
                    // execute the pending bindings.
                    final ViewDataBinding binding = getBinding(v);
                    binding.mRebindRunnable.run();
                    v.removeOnAttachStateChangeListener(this);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                }
            };
        }
    }
```

### 3.3 `handler`使用过多

```java
//ViewDataBinding.class 567行    
protected void requestRebind() {
  if (mContainingBinding != null) {
    mContainingBinding.requestRebind();
  } else {
    final LifecycleOwner owner = this.mLifecycleOwner;
    if (owner != null) {
      Lifecycle.State state = owner.getLifecycle().getCurrentState();
      if (!state.isAtLeast(Lifecycle.State.STARTED)) {
        return; // wait until lifecycle owner is started
      }
    }
    synchronized (this) {
      if (mPendingRebind) {
        return;
      }
      mPendingRebind = true;
    }
    if (USE_CHOREOGRAPHER) {
      mChoreographer.postFrameCallback(mFrameCallback);
    } else {
      mUIThreadHandler.post(mRebindRunnable);
    }
  }
}
```

