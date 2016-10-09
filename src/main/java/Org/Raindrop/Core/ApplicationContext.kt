/**
 * Created by ivan on 16/9/28.
 * Org.Raindrop.Core
 */
package Org.Raindrop.Core

import java.util.*
import Org.Raindrop.Parser.ParserImpl.XmlParser


/**
 * 包含所有的实例化类
 */
object ApplicationContext {
    private var drops: HashMap<String, ClassConstructor> ?
    init{
        drops = null
    }
    fun context(file:String):RainContext{
        val parser = XmlParser(file)
        drops = parser.parse()
        return RainContext(drops)
    }

    /**
     * 这个还是需要的，重大的设计缺陷
     */
    fun rain(id: String): Any?{
        val cons = drops?.get(id)//[id]
        return cons?.construct()
    }
}