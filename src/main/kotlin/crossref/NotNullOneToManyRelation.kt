package crossref

import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class NotNullOneToManyRelation<Left : Any, Right : Any> {

    private val nullableRelation = OneToManyRelation<Left, Right>()

    fun set(right: Right, left: Left) = nullableRelation.set(right, left)

    fun getLeft(right: Right) = nullableRelation.getLeft(right) ?: throw Exception("unknown right reference : $right")
    fun getRight(left: Left) = nullableRelation.getRight(left)

    fun right() = object : ReadOnlyProperty<Left, Set<Right>> {
        override fun getValue(thisRef: Left, property: KProperty<*>) = getRight(thisRef)
    }

    fun left(ref: Right, initialValue: Left) = object : ReadWriteProperty<Right, Left> {

        init {
            set(ref, initialValue)
        }

        override fun getValue(thisRef: Right, property: KProperty<*>) = getLeft(thisRef)
        override fun setValue(thisRef: Right, property: KProperty<*>, value: Left) {
            set(thisRef, value)
        }
    }

    fun children() = right()
    fun parent(ref: Right, initialValue: Left) = left(ref, initialValue)
}
