/**
 * Created by ivan on 16/9/29.
 *
 */
import Org.Raindrop.Core.ApplicationContext

fun main(argv: Array<String>) {
    val context = ApplicationContext.context("/config.xml")
    val help: Any? = context.rain("help")
    val a: Any? = context.rain("help")
    if(a is Test.Help){
        println(a.message)
        a.message = "这是怪我的了"
    }
    if(help is Test.Help){
        println(help.message)
        help.action?.act()
    }
}