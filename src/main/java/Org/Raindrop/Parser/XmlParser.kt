/**
 * Created by ivan on 16/9/28.
 * Org.Raindrop.Core.Parser
 */
package Org.Raindrop.Parser

import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.io.SAXReader
import java.io.InputStream
import Org.Raindrop.Core.ClassConstructor
import Org.Raindrop.Core.Container
import Org.Raindrop.Core.Scope

import java.lang.reflect.Field


object XmlParser {
    fun parse(): Unit {
        val reader = SAXReader()
        val document: Document
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
                var parameterNumber: Int //= constructor?.elements("constructor-arg")?.size
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
                            value = parameter.attribute("value").value
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
                Container.add(id, newConstructor)
            }
        }
    }
}