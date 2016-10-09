/**
 * Created by ivan on 16/9/28.
 * Org.Raindrop.Core
 */
package org.rainday.core
import org.rainday.parser.XmlImpl.SimpleXml

/**
 * 包含所有的实例化类
 */
class Context(val path:String) {
    private val builder: Builder
    init{
        builder = Builder(SimpleXml(path))
        Container.context = builder.build()
    }
    /**
     * 这个还是需要的，重大的设计缺陷
     */
    fun bean(id: String): Any?{
        val cons = Container.context?.get(id)
        return cons?.construct()
    }
}