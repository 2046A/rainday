/**
 * Created by ivan on 16/9/29.
 *
 */
import Org.Raindrop.Core.Container

fun main(argv: Array<String>) {
    Container.construct()
    val help: Any? = Container.drop("help")
    val a: Any? = Container.drop("help")
    if(a is Test.Help){
        println(a.message)
        a.message = "changed"
    }
    if(help is Test.Help){
        println(help.message)
        help.action?.act()
    }
}