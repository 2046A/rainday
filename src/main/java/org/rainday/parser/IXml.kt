package org.rainday.parser

import org.dom4j.Element
import java.util.*

/**
 * Xml自身的接口
 */
interface IXml {
    // 循环解析import标签
    fun parse()
    //获取beans
    fun getBeans(): List<Element>?
    //获取参数
    fun getParameters(): HashMap<String, String>?
}