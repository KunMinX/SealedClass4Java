![](https://tva1.sinaimg.cn/large/e6c9d24ely1h5ouspvmkyj21fd0u0tbu.jpg)

研发故事：[《Kotlin Sealed Class 太香了，Java 8 也想用怎么办？》](https://juejin.cn/post/7137571636781252622/)

&nbsp;

## 依赖

项目根目录 build.gradle 添加如下依赖：

```
allprojects {
    repositories {
        // ...
        maven { url 'https://www.jitpack.io' }
    }
}
```

模块 build.gradle 添加如下依赖：

```groovy
implementation 'com.github.KunMinX:Java8-Sealed-Class:1.3.0-beta'
```

&nbsp;

## 使用说明

1.创建一个接口，添加 SealedClass 注解，且接口名开头 _ 下划线，

```java
@SealedClass
public interface _TestEvent {
  void resultTest1(@Param String a, int b);
  void resultTest2(@Param String a, @Param int b, int c, String d);
}
```

2.编译即可生成目标类，例如 TestEvent，然后可以像 Kotlin 一样使用该类：

```java
//创建一个 “密封类” 实例
TestEvent event = TestEvent.ResultTest1("textx");

//根据 id 分流
switch (event.id) {
  case TestEvent.ResultTest1.ID:
    TestEvent.ResultTest1 event1 = (TestEvent.ResultTest1) event;

    //拷贝
    event1.copy(1);
    
    //获取参数
    Log.d("---", event1.paramA);
    Log.d("---", String.valueOf(event1.resultB));
    break;
  case TestEvent.ResultTest2.ID:
    break;
}
```

提示：

1.当参数列表为空，例如 void test()，属于无参消息发送场景，故不对其提供 copy 方法。

2.@Param 参数是专为 [MVI-Dispatcher](https://github.com/KunMinX/MVI-Dispatcher) 模型设计，日常使用无视即可。

&nbsp;

## License

```
Copyright 2019-present KunMinX

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```