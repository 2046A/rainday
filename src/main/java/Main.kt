/**
 * Created by ivan on 16/9/29.
 *
 */
import Test.Help
import Parser.XmlParser


fun main(argv: Array<String>) {
    val help = Help("init")
    println(help.finalMsg)
    //println(help.msg)
    //help.msg = "重新赋值"
    help.finalMsg = "重新赋值"
    println(help.finalMsg)
    XmlParser.test()
}