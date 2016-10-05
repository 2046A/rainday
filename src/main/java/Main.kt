/**
 * Created by ivan on 16/9/29.
 *
 */
import Org.Raindrop.Core.Container

fun main(argv: Array<String>) {
    Container.construct("/config.xml")
    val help: Any? = Container.drop("help")
    val a: Any? = Container.drop("help")
    if(a is Test.Help){
        println(a.message)
        a.message = "这是怪我的了"
        //a.message?.act()
    }
    if(help is Test.Help){
        println(help.message)
        help.action?.act()
    }
}