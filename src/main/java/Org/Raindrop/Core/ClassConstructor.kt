/**
 * Created by ivan on 16/9/29.
 * Org.Raindrop.Core
 */
package Org.Raindrop.Core
import java.lang.reflect.Constructor
import java.lang.reflect.Field

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
                        finalValue = Container.drop(serviceId)
                }
            }
            field?.isAccessible = true
            field?.set(instance, finalValue)
            if(field?.modifiers.toString() == "private"){
                field?.isAccessible = false
            } else{
                field?.isAccessible = true
            }
        }
        return instance
    }
}