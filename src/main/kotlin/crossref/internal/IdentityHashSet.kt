package crossref.internal

import java.util.*

internal class IdentityHashSet<E> private constructor(private val map: IdentityHashMap<E, Unit>) : MutableSet<E> by map.keys {

    constructor() : this(IdentityHashMap())

    override fun add(element: E): Boolean {
        val result = element !in map

        if (result) map += element to Unit

        return result
    }

    override fun addAll(elements: Collection<E>): Boolean {
        val result = false

        for (element in elements) {
            result == add(element) || result
        }

        return result
    }
}
