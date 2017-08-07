package crossref.impl

import crossref.OneToManyRelation
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

fun <Left : Any, Right : Any> OneToManyRelation<Left, Right>.right() = object : ReadWriteProperty<Left, Set<Right>> {
    override fun getValue(thisRef: Left, property: KProperty<*>) = getRight(thisRef)
    override fun setValue(thisRef: Left, property: KProperty<*>, value: Set<Right>) {
        val current = getValue(thisRef, property)

        current.filter { it !in value }.forEach { set(it, null) }
        value.filter { it !in current }.forEach { set(it, thisRef) }
    }
}

fun <Left : Any, Right : Any> OneToManyRelation<Left, Right>.left() = object : ReadWriteProperty<Right, Left?> {
    override fun getValue(thisRef: Right, property: KProperty<*>) = getLeft(thisRef)
    override fun setValue(thisRef: Right, property: KProperty<*>, value: Left?) = set(thisRef, value)
}

fun <Parent : Any, Child : Any> OneToManyRelation<Parent, Child>.children() = right()
fun <Parent : Any, Child : Any> OneToManyRelation<Parent, Child>.parent() = left()
