/**
 * Created by ivan on 16/9/28.
 * Org.Raindrop.Core.Parser
 */
package Org.Raindrop.Parser

//import Test.Help
import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.io.SAXReader
import java.io.InputStream
import Org.Raindrop.Core.ClassConstructor
import Org.Raindrop.Core.Container
//import Org.Raindrop.Core.Container
import Org.Raindrop.Core.Scope

import java.lang.reflect.Field


object XmlParser {
    fun parse(): Unit {
        val reader = SAXReader()
        var document: Document
        val stream: InputStream? = this.javaClass.getResourceAsStream("/config.xml")
        if (stream != null) {
            document = reader.read(stream)
        } else {
            println("配置文件读取失败")
            return
        }
        val list = document.rootElement.elements("drop")
        for (drop in list) {
            if (drop is Element) {
                val id = drop.attribute("id").text
                val newConstructor = ClassConstructor()
                newConstructor.scope = if (drop.attribute("scope").text == "singleton") Scope.SINGLETON else Scope.INSTANCE
                val classContext = Class.forName(drop.attribute("class").text)
                //先使用相应的函数给构造出来，然后再重新赋值
                val constructor: Element? = drop.element("constructor")
                var parameterNumber: Int = 0
                if (constructor != null) {
                    parameterNumber = constructor.elements("constructor-arg").size
                    for (init in classContext.constructors) {
                        if (init.parameterTypes.size == parameterNumber) {//就是这个构造器
                            val callTypes = kotlin.arrayOfNulls<Class<*>>(parameterNumber)
                            val callParameters = kotlin.arrayOfNulls<Any>(parameterNumber)
                            for ((index, type) in init.parameterTypes.withIndex()) {
                                callTypes[index] = type
                                callParameters[index] = Class.forName(type.name).newInstance()
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
                var index=0
                if(drop.elements("property")!=null){
                    for(parameter in  drop.elements("property")){
                        if(parameter is Element){
                            val name = parameter.attribute("name").value
                            var value: Any? = null
                            if (parameter.attribute("value") != null) {
                                value = parameter.attribute("value").value
                            } else if (parameter.attribute("ref") != null) {
                                value = Container.drop(parameter.attribute("ref").value)
                            }
                            if(value!=null){
                                for (field in classContext.declaredFields) {
                                    if (field.name == name) {
                                        newConstructor.fieldParameters[index] = Pair<Field, Any>(field, value)
                                        index += 1
                                    }
                                }
                            }
                        }
                    }

                }
                if (constructor != null) { //使用构造函数初始化
                    val userParameters = constructor.elements("constructor-arg")
                    if (userParameters != null) {
                        for (parameter in userParameters) {
                            if (parameter is Element) {
                                val name = parameter.attribute("name").value
                                var value: Any? = null
                                if (parameter.attribute("value") != null) {
                                    value = parameter.attribute("value").value
                                } else if (parameter.attribute("ref") != null) {
                                    value = Container.drop(parameter.attribute("ref").value)
                                }
                                if (value != null) {
                                    for (field in classContext.declaredFields) {
                                        if (field.name == name) {
                                            newConstructor.fieldParameters[index] = Pair<Field, Any>(field, value)
                                            index += 1
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
                Container.add(id, newConstructor)
            }
        }
    }
}