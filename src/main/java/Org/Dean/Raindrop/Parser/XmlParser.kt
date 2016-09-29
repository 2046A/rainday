/**
 * Created by ivan on 16/9/28.
 * Org.Dean.Raindrop.Core.Parser
 */
package Org.Dean.Raindrop.Parser
import org.dom4j.Attribute
import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.io.SAXReader
import java.io.InputStream
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import kotlin.reflect.*


object XmlParser {
    fun test(){
        val reader = SAXReader()
        val document: Document
        //为什么getResourceAsStream就是不行呢，我操啊
        val stream: InputStream? = this.javaClass.getResourceAsStream("/config.xml")//FileInputStream(File("/Users/ivan/Repositories/raindrop/src/main/java/Org.Dean.Raindrop.Core.Parser/config.xml"))
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
                val className = drop.attribute("class").text
                val classMenu = Class.forName(className)
                for(constructor in classMenu.constructors){
                    //val parameterTypes = constructor.parameterTypes
                    for(type in constructor.parameterTypes){
                        type.javaClass
                    }
                }
                //val constructor: Constructor<out Any>? = classMenu.constructors
                //classMenu.typeParameters
                //if(constructor != null) {
                    //for(init in constructor.){

                    //}
                //}
                val instance = classMenu.newInstance()
                val scope: String? = drop.attribute("scope").text
                val properties: MutableList<Any?>? = drop.elements("property")
                if(properties != null) {
                    for(property in properties) {
                        if(property is Element) {
                            val name = property.attribute("name").text
                            val value = property.attribute("value").text
                            val field: Field? = classMenu.getDeclaredField(name)
                            field?.set(instance, value)
                            if(instance is Test.Help) {
                                println(instance.finalMsg)
                            }
                            //if(field != null){
//                                field.set(instance, value)
//                            }
                        }
                    }
                } else {

                }
            }
        }
    }
}