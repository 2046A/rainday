package org.rainday.util
import org.dom4j.Document
import org.dom4j.io.SAXReader

object stream {
    val xmlReader = SAXReader()
    /**
     * 根据xml文件串读取内容,返回一个Document对象
     */
    fun read(file: String):Document?{
        val stream = this.javaClass.getResourceAsStream(file)
        if(stream != null){
            return xmlReader.read(stream)
        } else {
            return null
        }
    }
}