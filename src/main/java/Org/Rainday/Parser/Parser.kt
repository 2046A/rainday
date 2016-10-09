/**
 * Created by ivan on 16/10/5.
 * Org.Raindrop.Parser
 */
package Org.Rainday.Parser

import Org.Rainday.Core.ClassConstructor
import java.util.*

/**
 * 解析接口
 */
interface Parser {
    fun parse(): HashMap<String,ClassConstructor>?
}