/**
 * Created by ivan on 16/9/28.
 * Org.Raindrop.Core
 */
package Org.Raindrop.Core

import Org.Raindrop.Parser.Parser
import java.util.*
import Org.Raindrop.Parser.ParserImpl.XmlParser

/**
 * 包含所有的实例化类
 */
object Container {
    private var drops: HashMap<String, ClassConstructor> ?
    private var parser: Parser? = null
    init{
        drops = null
        //parser = XmlParser()
    }
    fun construct(file:String){
        parser = XmlParser(file)
        //parser.configFile = file
        drops = parser?.parse()
        //drops.putAll(valueList)
        //valueList?.forEach { drops[it.first] = it.second }
        //XmlParser.parse()
    }
    fun drop(id: String): Any?{
        val cons = drops?.get(id)//[id]
        return cons?.construct()
    }
    //fun add(id:String, constructor: ClassConstructor):Unit{
      //  drops[id] = constructor
    //}
}