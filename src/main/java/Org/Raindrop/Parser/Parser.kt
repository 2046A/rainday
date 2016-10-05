/**
 * Created by ivan on 16/10/5.
 * Org.Raindrop.Parser
 */
package Org.Raindrop.Parser

import Org.Raindrop.Core.ClassConstructor
import java.util.*


interface Parser {
    var configFile: String?
    fun parse(): HashMap<String,ClassConstructor>?
    //fun setConfigFile(filePath:String)
}