/**
 * Created by ivan on 16/10/5.
 * Org.Raindrop.Parser.ParserImpl
 */
package Org.Raindrop.Parser.ParserImpl

import Org.Raindrop.Core.ClassConstructor
import org.dom4j.Document
import org.dom4j.Element
import Org.Raindrop.Core.Container
import Org.Raindrop.Core.Scope
import Org.Raindrop.Parser.Parser
import Org.Raindrop.Parser.Util
import org.dom4j.dom.DOMElement

import java.lang.reflect.Field
import java.util.*

class XmlParser(val path: String):Parser {

    var parameterList: HashMap<String, String> = hashMapOf<String, String>()

    /**
     * 解析parameters里面的参数，返回一个哈西表作为转换之用
     */
    fun parseParameter(document: Document): HashMap<String, String>? {//解析参数配置
        val list = hashMapOf<String, String>()
        val parameters = document.rootElement.element("parameters").elements("parameter")
        if (parameters != null) {
            for (element in parameters) {
                if (element is Element) {
                    val key = "%" + element.attribute("key").value + "%"
                    list[key] = element.text
                }
            }
        }
        return list
    }

    override fun parse(): HashMap<String, ClassConstructor>? {
        val valueContainer = hashMapOf<String, ClassConstructor>()//<Pair<String,ClassConstructor>>()
        val context = Util.read(path)
        if (context != null) {
            val rootElement = context.rootElement
            var rootParameters = context.rootElement.element("parameters")
            if(rootParameters == null){//创建一个
                context.rootElement.addElement("parameters")
                rootParameters = context.rootElement.element("parameters")
            }
            val importResourceList = Util.parseImportTag(context)
            if (importResourceList != null) {
                for (resource in importResourceList) {
                    val tmpContext = Util.read(resource)
                    if (tmpContext != null) {
                        val drops = Util.parseDropTag(tmpContext)
                        val parameters = Util.parseParameters(tmpContext)
                        drops?.forEach { Util.appendDrops(rootElement, it) }
                        parameters?.forEach { Util.appendParameters(rootParameters, it) }
                    }
                }
            }
            parameterList = parseParameter(context) ?: hashMapOf<String, String>()
            val list = rootElement.elements("drop")
            for (drop in list) {
                if (drop is Element) {
                    val id = drop.attribute("id").text
                    val newConstructor = ClassConstructor()
                    newConstructor.scope = if (drop.attribute("scope").text == "singleton") Scope.SINGLETON else Scope.INSTANCE
                    val classContext = Class.forName(drop.attribute("class").text)
                    //先使用相应的函数给构造出来，然后再重新赋值
                    val constructor: Element? = drop.element("constructor")
                    var parameterNumber: Int //= constructor?.elements("constructor-arg")?.size
                    if (constructor != null) {
                        parameterNumber = constructor.elements("constructor-arg").size
                        for (init in classContext.constructors) {
                            if (init.parameterTypes.size == parameterNumber) {//就是这个构造器
                                val callTypes = kotlin.arrayOfNulls<Class<*>>(parameterNumber)
                                val callParameters = kotlin.arrayOfNulls<Any>(parameterNumber)
                                for ((index, type) in init.parameterTypes.withIndex()) {
                                    //println("parameter name:" + init.parameters[index].name)
                                    //println("anno:" + init.parameters[index].declaredAnnotations)
                                    //println()
                                    callTypes[index] = type
                                    if (type.isInterface) {
                                        callParameters[index] = null
                                    } else {
                                        callParameters[index] = Any()
                                    }
                                }
                                val finalConstructor = classContext.getConstructor(callTypes.component1())
                                newConstructor.constructor = finalConstructor
                                newConstructor.callParameters = callParameters
                            }
                        }
                    } else {
                        newConstructor.constructor = classContext.getConstructor()
                        newConstructor.callParameters = null
                    }
                    var index = 0
                    var finalProperty: List<Any?> = arrayListOf(ClassConstructor::fieldParameters)
                    if (constructor != null) {
                        finalProperty += constructor.elements("constructor-arg")
                    }
                    if (drop.elements("property") != null) {
                        finalProperty += drop.elements("property")
                    }
                    for (parameter in finalProperty) {
                        if (parameter is Element) {
                            val name = parameter.attribute("name").value
                            var value: Any? = null
                            if (parameter.attribute("value") != null) {
                                val tmpValue = parameterList[parameter.attribute("value").value]
                                value = tmpValue ?: parameter.attribute("value").value
                                //value =
                            } else if (parameter.attribute("ref") != null) {
                                value = Pair(parameter.attribute("ref").value, Container.drop(parameter.attribute("ref").value))
                            }
                            for (field in classContext.declaredFields) {
                                if (field.name == name) {
                                    newConstructor.fieldParameters[index] = Pair<Field, Any?>(field, value)
                                    index += 1
                                }
                            }
                        }
                    }
                    valueContainer[id] = newConstructor
                }
            }
        }
        return valueContainer
    }
    /* override fun parse(): HashMap<String,ClassConstructor>?{
         val valueContainer = hashMapOf<String,ClassConstructor>()//<Pair<String,ClassConstructor>>()
         val reader = SAXReader()
         val document: Document
         val stream: InputStream? = this.javaClass.getResourceAsStream(configFile)
         if (stream != null) {
             document = reader.read(stream)
         } else {
             println("配置文件读取失败")
             return null
         }
         parseParameter(document)
         val list = document.rootElement.elements("drop")
         for (drop in list) {
             if (drop is Element) {
                 val id = drop.attribute("id").text
                 val newConstructor = ClassConstructor()
                 newConstructor.scope = if (drop.attribute("scope").text == "singleton") Scope.SINGLETON else Scope.INSTANCE
                 val classContext = Class.forName(drop.attribute("class").text)
                 //先使用相应的函数给构造出来，然后再重新赋值
                 val constructor: Element? = drop.element("constructor")
                 var parameterNumber: Int //= constructor?.elements("constructor-arg")?.size
                 if (constructor != null) {
                     parameterNumber = constructor.elements("constructor-arg").size
                     for (init in classContext.constructors) {
                         if (init.parameterTypes.size == parameterNumber) {//就是这个构造器
                             val callTypes = kotlin.arrayOfNulls<Class<*>>(parameterNumber)
                             val callParameters = kotlin.arrayOfNulls<Any>(parameterNumber)
                             for ((index, type) in init.parameterTypes.withIndex()) {
                                 //println("parameter name:" + init.parameters[index].name)
                                 //println("anno:" + init.parameters[index].declaredAnnotations)
                                 //println()
                                 callTypes[index] = type
                                 if(type.isInterface){
                                     callParameters[index] = null
                                 } else {
                                     callParameters[index] = Any()
                                 }
                             }
                             val finalConstructor = classContext.getConstructor(callTypes.component1())
                             newConstructor.constructor = finalConstructor
                             newConstructor.callParameters = callParameters
                         }
                     }
                 } else {
                     newConstructor.constructor = classContext.getConstructor()
                     newConstructor.callParameters = null
                 }
                 var index = 0
                 var finalProperty: List<Any?> = arrayListOf(ClassConstructor::fieldParameters)
                 if(constructor != null ){
                     finalProperty += constructor.elements("constructor-arg")
                 }
                 if(drop.elements("property") != null){
                     finalProperty += drop.elements("property")
                 }
                 for(parameter in finalProperty){
                     if (parameter is Element) {
                         val name = parameter.attribute("name").value
                         var value: Any? = null
                         if (parameter.attribute("value") != null) {
                             val tmpValue = parameterList[parameter.attribute("value").value]
                             value = tmpValue ?: parameter.attribute("value").value
                             //value =
                         } else if (parameter.attribute("ref") != null) {
                             value = Pair(parameter.attribute("ref").value, Container.drop(parameter.attribute("ref").value))
                         }
                         for (field in classContext.declaredFields) {
                             if (field.name == name) {
                                 newConstructor.fieldParameters[index] = Pair<Field, Any?>(field, value)
                                 index += 1
                             }
                         }
                     }
                 }
                 valueContainer[id] = newConstructor
                 //Container.add(id, newConstructor)
             }
         }
         return valueContainer
     }*/
    //override fun setConfigFile(filePath:String){
    //    configFile = filePath
    //}
}