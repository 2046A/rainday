/**
 * Created by ivan on 16/9/28.
 * Org.Raindrop.Core
 */
package Org.Raindrop.Core

import java.util.*
import Org.Raindrop.Parser.XmlParser

/**
 * 包含所有的实例化类
 */
object Container {
    private val drops: HashMap<String, ClassConstructor>
    init{
        drops = hashMapOf()
    }
    fun construct(){
        XmlParser.parse()
        //val list =
        //if(list != null && list is List<ClassConstructor>){
//            list.forEach { drops[it.id] = it }
//        }
    }
    fun drop(id: String): Any?{
        val cons = drops[id]
        return cons?.construct()
    }

    fun add(id:String, constructor: ClassConstructor):Unit{
        drops[id] = constructor
    }
}