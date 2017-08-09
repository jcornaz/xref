package crossref

import crossref.internal.IdentityHashSet
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


open class OneToManyRelation<Left : Any, Right : Any> {

    private val rightsByLeft = IdentityHashMap<Left, IdentityHashSet<Right>>()
    private val leftByRight = IdentityHashMap<Right, Left>()

    fun set(right: Right, left: Left?) = synchronized(this) {

        leftByRight[right]?.let {
            rightsByLeft[it]?.remove(right)
        }

        if (left == null) {
            leftByRight -= right
        } else {
            leftByRight[right] = left
            rightsByLeft.computeIfAbsent(left) { IdentityHashSet() }.add(right)
        }
    }

    fun getLeft(right: Right) = synchronized(this) {
        leftByRight[right]
    }

    fun getRight(left: Left) = synchronized(this) {
        rightsByLeft[left]?.toSet().orEmpty()
    }

    fun right() = object : ReadWriteProperty<Left, Set<Right>> {
        override fun getValue(thisRef: Left, property: KProperty<*>) = getRight(thisRef)
        override fun setValue(thisRef: Left, property: KProperty<*>, value: Set<Right>) {
            val current = getValue(thisRef, property)

            current.filter { it !in value }.forEach { set(it, null) }
            value.filter { it !in current }.forEach { set(it, thisRef) }
        }
    }

    fun left() = object : ReadWriteProperty<Right, Left?> {
        override fun getValue(thisRef: Right, property: KProperty<*>) = getLeft(thisRef)
        override fun setValue(thisRef: Right, property: KProperty<*>, value: Left?) {
            set(thisRef, value)
        }
    }

    fun children() = right()
    fun parent() = left()
}
