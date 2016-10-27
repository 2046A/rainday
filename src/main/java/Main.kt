/**
 * Created by ivan on 16/9/29.
 *
 */
import org.rainday.beans.Context

fun main(argv: Array<String>) {
    val context = Context("/config.xml")
    val help: Any? = context.bean("help")
    val a: Any? = context.bean("help")
    if(a is Test.Help){
        println(a.message)
        a.message = "这是怪我的了"
    }
    if(help is Test.Help){
        println(help.message)
        help.action?.act()
    }
}