package Org.Rainday.Parser
import org.dom4j.Document
import org.dom4j.Element
import org.dom4j.io.SAXReader
import org.dom4j.dom.DOMElement
//import java.util.*

object Util {
    val xmlReader = SAXReader()
    /**
     * 根据xml文件串读取内容,返回一个Document对象
     */
    fun read(file: String):Document?{
        val stream = this.javaClass.getResourceAsStream(file)
        if(stream != null){
            return xmlReader.read(stream)
        } else {
            return null
        }
    }

    /**
     * 复制源element，添加新生成的element到目的element
     */
    fun append(destination: Element, source: Element):Unit{
        val tmp = source.clone()
        if(tmp is Element)
            destination.add(tmp)
    }

    /**
     * 直接解析源Document里面的参数,打包为List返回
     * @note source应该为<parameters></parameters>元素
     */
    fun parseParameters(source: Document): List<Element>?{
        val list = arrayListOf<Element>()
        source.rootElement.element("parameters")?: return null
        //val parametersTag = source.rootElement.element("parameters")
        //if(parametersTag == null){
//            return null
//        }
        val parameters = source.rootElement.element("parameters").elements("parameter")
        if(parameters != null){
            for(element in parameters){
                if(element is Element){
                    list.add(element)
                    //val key = "%" + element.attribute("key").value + "%"
                    //list[key] = element.text
                }
            }
        }
        return list
    }

    /**
     *  解析source Document里面包含的import标签及resource参数信息，打包为list返回
     */
    fun parseImportTag(source: Document): List<String>?{
        val ret = arrayListOf<String>()
        source.rootElement.elements("import")?: return null
        val resources = source.rootElement.elements("import")
        if(resources != null){
            for(resource in resources){
                if(resource is Element){
                    ret.add(resource.attribute("resource").value)
                }
            }
            //resources.forEach { ret.add(it.attribute("resource").value) }
        }
        return ret
    }

    /**
     * 解析document里面的drop元素，打包为list返回
     */
    fun parseDropTag(source: Document): List<Element>?{
        val ret = arrayListOf<Element>()
        source.rootElement.elements("rain")?: return null
        val drops = source.rootElement.elements("rain")
        if(drops != null){
            for(drop in drops){
                if(drop is Element)
                    ret.add(drop)
            }
        }
        return ret
    }
}