/**
 * Created by ivan on 16/9/29.
 *
 */
import Org.Raindrop.Core.Container
//import Org.Raindrop.Parser.XmlParser


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
        //println(help.action)
        help.action?.act()
    }
    //val help = Help("init")
    //println(help.finalMsg)
    //println(help.msg)
    //help.msg = "重新赋值"
    //help.finalMsg = "重新赋值"
    //println(help.finalMsg)
    ////XmlParser.parse()
    //Parser.parseHelp()
    //XmlParser.parse()
}