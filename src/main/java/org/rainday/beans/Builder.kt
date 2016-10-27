package org.rainday.beans

import org.rainday.parser.IXml
import org.dom4j.Element
import java.lang.reflect.Field
import java.util.*


/**
 * 构建ClassConstructor
 */
class Builder(val xmlParser: IXml) {
    fun build(): HashMap<String, ClassConstructor> {
        val container = hashMapOf<String, ClassConstructor>()
        xmlParser.parse()
        val parameters = xmlParser.getParameters()
        val beans = xmlParser.getBeans()
        if(beans != null) {
            for(bean in beans){
                val constructor = ClassConstructor()
                constructor.scope = if(bean.attribute("scope")?.text=="singleton") Scope.SINGLETON else Scope.INSTANCE
                val classContext = Class.forName(bean.attribute("class").text)
                val init = bean.element("constructor")
                if(init != null){ //有构造器
                    val paramNum = init.elements("constructor-arg").size
                    for(builder in classContext.constructors){
                        if(builder.parameterTypes.size == paramNum) {
                            //应该就是这个构造器了
                            //这是个粗糙的判断，有缺陷，但可用
                            //todo 优化
                            val callTypes = arrayOfNulls<Class<*>>(paramNum)
                            val callParameters = arrayOfNulls<Any>(paramNum)
                            for((index,type) in builder.parameterTypes.withIndex()){
                                callTypes[index] = type
                                if(type.isInterface) callParameters[index] = null
                                else callParameters[index] = Any()
                            }
                            constructor.constructor = classContext.getConstructor(callTypes.component1())
                            constructor.callParameters = callParameters
                        }
                    }
                } else { //没有构造器，获取默认构造器
                    constructor.constructor = classContext.getConstructor()
                    constructor.callParameters = null
                }
                var properties: List<Any?> = arrayListOf(ClassConstructor::fieldParameters)
                if (init != null) properties += init.elements("constructor-arg")
                if(bean.elements("property") != null) properties += bean.elements("property")
                var index = 0
                for(parameter in properties){ //最终构建过程
                    if(parameter is Element){
                        val name = parameter.attributeValue("name")
                        var value: Any? = null
                        if(parameter.attributeValue("value") != null){
                            value = parameters?.get(parameter.attributeValue("value")) ?: parameter.attributeValue("value")
                        } else if(parameter.attribute("ref") != null){
                            value = Pair(parameter.attributeValue("ref"), null)
                        }
                        for(field in classContext.declaredFields){ //搜寻符合name的属性
                            if(field.name == name){
                                constructor.fieldParameters[index] = Pair<Field,Any?>(field, value)
                                index += 1
                            }
                        }
                    }
                }
                container[bean.attribute("id").text] = constructor
            }
        }
        return container
    }
}