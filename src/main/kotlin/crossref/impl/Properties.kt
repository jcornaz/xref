package crossref.impl

import crossref.OneToOneRelation
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <Left : Any, Right : Any> OneToOneRelation<Left, Right>.right() = object : ReadWriteProperty<Left, Right?> {
    override fun getValue(thisRef: Left, property: KProperty<*>) = getRight(thisRef)
    override fun setValue(thisRef: Left, property: KProperty<*>, value: Right?) = set(thisRef, value)
}

fun <Left : Any, Right : Any> OneToOneRelation<Left, Right>.left() = object : ReadWriteProperty<Right, Left?> {
    override fun getValue(thisRef: Right, property: KProperty<*>) = getLeft(thisRef)
    override fun setValue(thisRef: Right, property: KProperty<*>, value: Left?) = set(value, thisRef)
}
