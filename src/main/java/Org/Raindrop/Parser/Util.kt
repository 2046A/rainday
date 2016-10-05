package Org.Raindrop.Parser
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
    fun appendParameters(destination: Element, source: Element):Unit{
        //destination.addElement(source.name).appendContent(source)
        //source.de
        val tmp = source.clone()
        if(tmp is Element)
            destination.add(tmp)
    }

    /**
     * 同理...
     */
    fun appendDrops(destination: Element, source: Element):Unit{
//        destination.addElement(source.name).appendContent(source)
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
        val drops = source.rootElement.elements("drop")
        if(drops != null){
            for(drop in drops){
                if(drop is Element)
                    ret.add(drop)
            }
        }
        return ret
    }
}