package Org.Rainday.Config.XmlImpl

import Org.Rainday.Config.IXml
import Org.Rainday.Core.ClassConstructor
import java.util.*

/**
 * 默认实现
 */
class SimpleXml: IXml {
    override fun parseImport(){

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