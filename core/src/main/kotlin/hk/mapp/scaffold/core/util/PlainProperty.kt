package hk.mapp.scaffold.core.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PlainProperty<T>: ReadWriteProperty<Any, T> {

    fun getValue(): T

    fun setValue(value: T)

    override fun getValue(thisRef: Any, property: KProperty<*>) = getValue()

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = setValue(value)

}
