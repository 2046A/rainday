package org.rainday.parser.XmlImpl

import org.rainday.parser.IXml
import org.rainday.util.stream
import org.rainday.parser.Tag
import org.dom4j.Element
import java.util.*

/**
 * 默认实现
 */
class SimpleXml(val path:String): IXml {
    private var parameters: java.util.ArrayList<Element> //最终参数存储列表
    private var beans: java.util.ArrayList<Element> //最终bean存储列表
    init {
        //document = Util.read(path)
        parameters = ArrayList<Element>()
        beans = ArrayList<Element>()
    }
    private fun innerImportParse(importList: List<Any?>){
        for(import in importList){
            if(import is Element){
                val toImportDocument = stream.read(import.attributeValue("resource"))
                if(toImportDocument != null) {
                    toImportDocument.rootElement.element(Tag.ParametersName)?.elements("parameter")?.forEach {
                        if(it is Element) parameters.add(it)
                    }
                    toImportDocument.rootElement.elements(Tag.BeanName)?.forEach {
                        if(it is Element) beans.add(it)
                    }
                    innerImportParse(toImportDocument.rootElement.elements("import"))
                    //val toImportList = toImportDocument.rootElement.elements("import")
                    //if(toImportList.size > 0 ) innerImportParse(toImportList)
                }
            }
        }
    }
    override fun parse(){
        val document = stream.read(path)
        if (document != null) {
            document.rootElement.elements(Tag.BeanName)?.forEach {
                if(it is Element) beans.add(it)
            }
            document.rootElement.element(Tag.ParametersName)?.elements("parameter")?.forEach {
                if(it is Element) parameters.add(it)
            }
            val importList = document.rootElement.elements("import")
            if(importList.size > 0) innerImportParse(importList)
        }
    }
    override fun getBeans(): List<Element>?{
        return beans
    }
    override fun getParameters(): HashMap<String, String>?{
        val list = hashMapOf<String, String>()
        for(parameter in parameters){
            val key = "%" + parameter.attribute("key").value + "%"
            list[key] = parameter.text
        }
        return list
    }
}