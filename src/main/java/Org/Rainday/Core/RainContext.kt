/**
 * Created by ivan on 16/10/5.
 * Org.Raindrop.Core
 */
package Org.Rainday.Core

import Org.Rainday.Parser.ParserImpl.XmlParser
import java.util.*

class RainContext(val drops: HashMap<String, ClassConstructor>?) {
    fun rain(id: String): Any? {
        val constructor = drops?.get(id)
        return constructor?.construct()
    }
}