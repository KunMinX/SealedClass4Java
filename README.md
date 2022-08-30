![](https://tva1.sinaimg.cn/large/e6c9d24ely1h5p8lqzj58j21ok0oa41p.jpg)

&nbsp;

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
implementation 'com.github.KunMinX:SealedClass4Java:1.4.0-beta'
```

&nbsp;

## 使用说明

1.创建一个接口，添加 SealedClass 注解，且接口名开头 _ 下划线，

```java
@SealedClass
public interface _TestEvent {
  void resultTest1(String a, int b);
  void resultTest2(String a, int b, int c);
}
```

2.编译即可生成目标类，例如 TestEvent，然后可以像 Kotlin 一样使用该类：

```java
TestEvent event = TestEvent.ResultTest1("textx");
switch (event.id) {
  case TestEvent.ResultTest1.ID:
    TestEvent.ResultTest1 event1 = (TestEvent.ResultTest1) event;
    event1.copy(1);
    event1.paramA;
    event1.resultB;
    break;
  case TestEvent.ResultTest2.ID:
    break;
}
```

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