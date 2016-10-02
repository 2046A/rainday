/**
 * Created by ivan on 16/9/29.
 * Org.Raindrop.Core
 */
package Org.Raindrop.Core
import java.lang.reflect.Constructor

/**
 *
 */
class ClassConstructor(val id: String) {
    val parameterNumber = 10 //假设最大参数个数为10
    var constructor: Constructor<Any>? = null
    var callParameters = kotlin.arrayOfNulls<Any>(parameterNumber)

}