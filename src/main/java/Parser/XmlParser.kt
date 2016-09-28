/**
 * Created by ivan on 16/9/28.
 * Parser
 */
package Parser
import org.dom4j.Attribute
import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.io.SAXReader
import java.io.File
import java.io.InputStream
import java.io.FileInputStream
//import kotlin.collections.List

object XmlParser {
    fun test(){
        val reader = SAXReader()
        val document: Document
        //为什么getResourceAsStream就是不行呢，我操啊
        val stream: InputStream? = FileInputStream(File("/Users/ivan/Repositories/raindrop/src/main/java/Parser/config.xml"))
        if(stream != null){
            document = reader.read(stream)
        } else {
            println("配置文件读取失败")
            return
        }
        val list = document.rootElement.elements("drop")
        for(drop in list){
            if(drop is Element){
                val attrs = drop.attributes()
                for(attr in attrs){
                    if(attr is Attribute){
                        println("name:" + attr.name+ "\tvalue:" + attr.value)
                    }
                }
            }
        }
    }
}