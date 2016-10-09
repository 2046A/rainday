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
* container从单例变为需要类

#### 快速开始
新建maven项目，编写xml配置文件命名为config.xml并放置resources目录下

```
<?xml version="1.0" encoding="UTF-8" ?>

<rainday>

    <rain id="help" class="Test.Help" scope="instance">
        <property name="message" value="%help.message%" />
        <!--<property name="action" ref="simple"/>-->
    </rain>
    <import resource="/parameters.xml" />
    <import resource="/refs.xml" />

</rainday>
```

parameters.xml放置于resources目录下

```
<?xml version="1.0" encoding="UTF-8" ?>

<rainday>
    <parameters>
        <parameter key="help.message">你就是好人啊</parameter>
    </parameters>
</rainday>

```

refs.xml同理
```
<?xml version="1.0" encoding="UTF-8" ?>

<rainday>
    <!--<rain id="simple" class="Test.ActionImpl.SimpleAction" scope="singleton" />-->
</rainday>
```

新建包Test,编写Help类如下

```
class Help{
    var message: String? = null
}
```

编写启动文件

```
import org.rainday.core.Context

fun main(argv: Array<String>) {
    val context = Context("/config.xml")
    val help: Any? = context.bean("help")
    if(a is Test.Help){
        println(a.message)
    }
}
```

#### LICENSE
**MIT LICENSE**