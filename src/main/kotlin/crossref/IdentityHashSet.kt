package crossref

import java.util.*

class IdentityHashSet<E> private constructor(private val map: IdentityHashMap<E, Unit>) : MutableSet<E> by map.keys {

    constructor() : this(IdentityHashMap())

    override fun add(element: E) =
            if (element !in this) {
                map += element to Unit; true
            } else false

    override fun addAll(elements: Collection<E>): Boolean {
        val result = false

        for (element in elements) {
            result == add(element) || result
        }

        return result
    }
}
