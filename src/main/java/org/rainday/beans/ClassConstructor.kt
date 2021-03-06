/**
 * Created by ivan on 16/9/29.
 * Org.Raindrop.Core
 */
package org.rainday.beans
import java.lang.reflect.Constructor
import java.lang.reflect.Field
//import java.util.*

/**
 *
 */
class ClassConstructor {
    val parameterNumber = 10 //假设最大参数个数为10
    val fieldNumber = 40 //40个属性，不能太多了
    var constructor: Constructor<out Any>? = null
    var scope = Scope.INSTANCE
    //var callTypes = kotlin.arrayOfNulls<Class<*>>(parameterNumber)
    var callParameters: Array<Any?>? = kotlin.arrayOfNulls<Any>(parameterNumber)
    val fieldParameters = kotlin.arrayOfNulls<Pair<Field, Any?>>(fieldNumber)
    var instance: Any? = null
    fun construct(): Any?{
        if(scope == Scope.SINGLETON) {
            if(instance != null){
                return instance
            } else {
                return generateInstance()
            }
        } else {
            return generateInstance()
        }
    }
    private fun generateInstance(): Any?{
        if(callParameters !=null && callParameters is Array<Any?>){
            instance = constructor?.newInstance(callParameters?.component1())
        } else {
            instance = constructor?.newInstance()
        }
        for(pair in fieldParameters){
            val field = pair?.first
            val value = pair?.second
            var finalValue: Any? = value
            if(value is  Pair<*,*>){//如果是pair，那就说明是延迟绑定
                val serviceId = value.first //container内id
                val service = value.second //实例
                if(service != null){
                    finalValue = service
                } else {
                    if(serviceId is String)
                        finalValue = Container.context?.get(serviceId)?.construct()//container[serviceId]?.construct(container)//解耦合做的很烂啊，还得持续优化
                }
            }
            field?.isAccessible = true
            when(field?.genericType?.typeName){
                //todo 添加其他类型
                "java.lang.Integer" -> { if(finalValue!="") field?.set(instance, finalValue.toString().toInt()) else field?.set(instance, 0)}
                "java.lang.String" -> field?.set(instance, finalValue)
                else -> field?.set(instance, finalValue)
            }
            //println("typeName:" + field?.genericType?.typeName+"\tvalue:" + finalValue)

            //field?.set(instance, finalValue)
            if(field?.modifiers.toString() == "private"){
                field?.isAccessible = false
            } else{
                field?.isAccessible = true
            }
        }
        return instance
    }
}