/**
 * Created by ivan on 16/10/5.
 * Org.Raindrop.Core
 */
package Org.Raindrop.Core

import Org.Raindrop.Parser.ParserImpl.XmlParser
import java.util.*

class RainContext(val drops: HashMap<String, ClassConstructor>?) {
    fun drop(id: String): Any? {
        val constructor = drops?.get(id)
        return constructor?.construct()
    }
}