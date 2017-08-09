package crossref

import crossref.internal.IdentityHashSet
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class ManyToManyRelation<Left : Any, Right : Any> {

    private val rightsByLeft = IdentityHashMap<Left, IdentityHashSet<Right>>()
    private val leftsByRight = IdentityHashMap<Right, IdentityHashSet<Left>>()

    fun add(left: Left, right: Right) {
        rightsByLeft.computeIfAbsent(left) { IdentityHashSet() }.add(right)
        leftsByRight.computeIfAbsent(right) { IdentityHashSet() }.add(left)
    }

    fun remove(left: Left, right: Right) {
        rightsByLeft[left]?.remove(right)
        leftsByRight[right]?.remove(left)
    }

    fun getLeft(right: Right): Set<Left> = synchronized(this) {
        leftsByRight[right]?.toSet().orEmpty()
    }

    fun getRight(left: Left): Set<Right> = synchronized(this) {
        rightsByLeft[left]?.toSet().orEmpty()
    }

    fun left(): ReadWriteProperty<Right, Set<Left>> = object : ReadWriteProperty<Right, Set<Left>> {
        override fun getValue(thisRef: Right, property: KProperty<*>) = getLeft(thisRef)

        override fun setValue(thisRef: Right, property: KProperty<*>, value: Set<Left>) {
            val current = getValue(thisRef, property)

            current.filter { it !in value }.forEach { remove(it, thisRef) }
            value.filter { it !in current }.forEach { add(it, thisRef) }
        }
    }

    fun right(): ReadWriteProperty<Left, Set<Right>> = object : ReadWriteProperty<Left, Set<Right>> {
        override fun getValue(thisRef: Left, property: KProperty<*>) = getRight(thisRef)

        override fun setValue(thisRef: Left, property: KProperty<*>, value: Set<Right>) {
            val current = getValue(thisRef, property)

            current.filter { it !in value }.forEach { remove(thisRef, it) }
            value.filter { it !in current }.forEach { add(thisRef, it) }
        }
    }
}
