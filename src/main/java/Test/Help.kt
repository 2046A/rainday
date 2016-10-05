/**
 * Created by ivan on 16/9/29.
 * Test
 */
package Test

/**
 * 意外发现两种赋值方式，不错不错...
 */
class Help{
    var message:String? = null
    val action: Action? = null
    fun act(){
        action?.act()
    }
}