#### raindrop - 基于kotlin语言的依赖注入框架
* 使用xml文件格式配置
* 尽量少的使用外部库，最好只使用标准库
* 参考symofny的注入容器和spring

#### 引用外部库
*  dom4j

#### 快速开始
新建maven项目，编写xml配置文件命名为如下并放置resources目录下
``
<drop id="help" class="Test.Help" scope="instance">
        <constructor> 
            <constructor-arg name="message"  value="怪wo了"/>
        </constructor>
        <property name="action" value="simple"/>
</drop>
``
新建包Test,编写Help类如下
``
class Help(var message:String){
    var action: String? = null
}
``
编写启动文件
``
import Org.Raindrop.Core.Container

fun main(argv: Array<String>) {
    Container.construct()
    val help: Any? = Container.drop("help")
  //  val a: Any? = Container.drop("help")
    if(a is Test.Help){
        println(a.message)
    }
}
``
整个过程就是这样了