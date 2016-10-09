package org.rainday.core

import java.util.*

/**
 * 全局唯一容器，包含所有ClassConstructor及id
 */
object Container {
    var context: HashMap<String, ClassConstructor>? = null
}