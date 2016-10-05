#### raindrop - 基于kotlin语言的依赖注入框架
* 使用xml文件格式配置
* 尽量少的使用外部库，最好只使用标准库
* 参考Spring的xml配置

#### 引用外部库
*  **[dom4j](http://dom4j.github.io/)**，用于解析xml文件

#### 已完成
* ref对于引用位置的限定去除，即当前的ref只能引用前面的drop而不能引用后面的drop
* drop位置的不限定
* 添加参数
* import功能，不同类型的xml文件可以分开放置

#### todo
* 对于容器内部二次初始化属性的优化，使之只初始化一次
* 代码优化
* bug修复

#### 快速开始
新建maven项目，编写xml配置文件命名为config.xml并放置resources目录下

```
<?xml version="1.0" encoding="UTF-8" ?>

<raindrop>
    <!--<parameters>
        <parameter key="help.msg">我就是这么屌</parameter>
    </parameters>-->
    <drop id="simple" class="Test.ActionImpl.ShitAction" scope="singleton" />

    <drop id="help" class="Test.Help" scope="instance">
        <constructor> <!--终极推荐做法-->
            <constructor-arg name="message"  value="怪wo"/>
        </constructor>
        <property name="action" ref="simple"/>
    </drop>

</raindrop>
```

新建包Test,编写Help类如下

```
class Help(var message:String){
    var action: String? = null
}
```

编写启动文件

```
import Org.Raindrop.Core.Container

fun main(argv: Array<String>) {
    Container.construct()
    val help: Any? = Container.drop("help")
    if(a is Test.Help){
        println(a.message)
    }
}
```

#### LICENSE
**MIT LICENSE**