/**
 * Created by ivan on 16/9/28.
 * Org.Raindrop.Core.Parser
 */
package Org.Raindrop.Parser
import Test.Help
import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.io.SAXReader
import java.io.InputStream


object XmlParser {
    fun test(){
        val reader = SAXReader()
        val document: Document
        val stream: InputStream? = this.javaClass.getResourceAsStream("/config.xml")//FileInputStream(File("/Users/ivan/Repositories/raindrop/src/main/java/Org.Raindrop.Core.Parser/config.xml"))
        if(stream != null){
            document = reader.read(stream)
        } else {
            println("配置文件读取失败")
            return
        }
        val list = document.rootElement.elements("drop")
        for(drop in list){
            if(drop is Element){
                val id = drop.attribute("id").text
                //val className = drop.attribute("class").text
                val classContext = Class.forName(drop.attribute("class").text)
                val constructor: Element? = drop.element("constructor")
                var parameterNumber: Int = 0
                if(constructor != null) {//使用构造器来构建实例
                    parameterNumber = constructor.elements("constructor-arg").size
                    val userParameters = constructor.elements("constructor-arg")
                    for(init in classContext.constructors){
                        if(init.parameterTypes.size == parameterNumber) {//那就是这个构造函数了
                            val callTypes = kotlin.arrayOfNulls<Class<*>>(parameterNumber)
                            val callParameters = kotlin.arrayOfNulls<Any>(parameterNumber)
                            for((index,type) in init.parameterTypes.withIndex()){
                                //获取参数传递时的名称
                                //init.get



                                callTypes[index] = type
                                val arg: Any? = userParameters.getOrNull(index)
                                if(arg is Element){
                                    callParameters[index] = arg.text//arg.attribute("text").value
                                }
                                //callParameters[index] = userParameters[index];
                                //val value: Element = constructor.elements("arg")[index]//.stringValue
                                //callParameters[index] = value?.toString()
                                //for(p in init.parameters){
                                //    if(type.javaClass.typeName == p.javaClass.typeName)
                                //        callParameters[index] = init.parameters[index]//Class.forName(type.name).newInstance()
                                //}
                                //if(type.javaClass.typeName == init.pa)
                                //callParameters[index] = parameters[index]
                            }
                            //这诡异的语法, 果然是有问题的嘛
                            val finalConstructor = classContext.getConstructor(callTypes.component1())
                            val instance = finalConstructor.newInstance(callParameters.component1())
                            if(instance is Help){
                                //instance.finalMsg = "快去，拜托"
                                println(instance.message)
                            }
                        }
                    }
                }
            }
        }
    }
}