package Org.Rainday.Config

import Org.Rainday.Core.ClassConstructor
import java.util.*

/**
 * Xml自身的接口
 */
interface IXml {
    // 循环解析import标签
    fun parseImport()
    //解析parameters参数
    fun parseParameter()
    //获取beans
    fun getBeans(): HashMap<String, ClassConstructor>?
    //获取参数
    fun getParameters(): HashMap<String, String>?
}