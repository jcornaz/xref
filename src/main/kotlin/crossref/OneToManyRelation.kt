package crossref

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


open class OneToManyRelation<Left : Any, Right : Any> {

    private val rightsByLeft = HashMap<Left, Set<Right>>()
    private val leftByRight = HashMap<Right, Left>()

    @Synchronized
    fun set(right: Right, left: Left?) {

        leftByRight[right]?.let {
            rightsByLeft[it] = rightsByLeft[it].orEmpty() - right
        }

        if (left == null) {
            leftByRight -= right
        } else {
            leftByRight[right] = left
            rightsByLeft[left] = rightsByLeft[left].orEmpty() + right
        }
    }

    @Synchronized
    fun getLeft(right: Right) = leftByRight[right]

    @Synchronized
    fun getRight(left: Left) = rightsByLeft[left]?.let { HashSet(it) }.orEmpty()

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
        override fun setValue(thisRef: Right, property: KProperty<*>, value: Left?) = set(thisRef, value)
    }

    fun children() = right()
    fun parent() = left()
}
