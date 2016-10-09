package Org.Rainday.Config.XmlImpl

import Org.Rainday.Config.IXml
import Org.Rainday.Core.ClassConstructor
import Org.Rainday.Parser.Util
import org.dom4j.Document
import org.dom4j.Element
import java.util.*

/**
 * 默认实现
 */
class SimpleXml(val path:String): IXml {
    private val document: Document? = Util.read(path)
    private fun innerImportParse(importList: List<Any?>, parent: Document){
        for(import in importList){
            if(import is Element){
                val toImportDocument = Util.read(import.attributeValue("resource"))
                val tmpDocument =
            }
        }
    }
    override fun parseImport(){
        if (document != null) {
            val importList = document.rootElement.elements("import")
            if(importList != null) innerImportParse(importList, document)
        }
    }
    override fun parseParameter(){

    }
    override fun getBeans(): HashMap<String, ClassConstructor>?{
        return null
    }
    override fun getParameters(): HashMap<String, String>?{
        return null
    }
}