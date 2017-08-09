/*
 * MIT License
 *
 * Copyright (c) 2017 Jonathan Cornaz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package xref

import xref.internal.IdentityHashSet
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * A one-to-many relation
 *
 * Unlike [NotNullOneToManyRelation], this relation allows instances of the right side to have no reference to the left side
 */
open class OneToManyRelation<Left : Any, Right : Any> {

    private val rightsByLeft = IdentityHashMap<Left, IdentityHashSet<Right>>()
    private val leftByRight = IdentityHashMap<Right, Left>()

    /** Set a relation between two instances */
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

    /** Return the element on the left side of the relation */
    fun getLeft(right: Right): Left? = synchronized(this) {
        leftByRight[right]
    }

    /** Return the elements on the right side of the relation */
    fun getRight(left: Left): Set<Right> = synchronized(this) {
        rightsByLeft[left]?.toSet().orEmpty()
    }

    /** Return a property bound to the right side of the relation */
    fun right() = object : ReadWriteProperty<Left, Set<Right>> {
        override fun getValue(thisRef: Left, property: KProperty<*>) = getRight(thisRef)
        override fun setValue(thisRef: Left, property: KProperty<*>, value: Set<Right>) {
            val currentValues: IdentityHashSet<Right> = rightsByLeft[thisRef] ?: IdentityHashSet()
            val newValues = IdentityHashSet(value)

            currentValues.filter { it !in newValues }.forEach { set(it, null) }
            newValues.filter { it !in currentValues }.forEach { set(it, thisRef) }
        }
    }

    /** Return a property bound to the left side of the relation */
    fun left() = object : ReadWriteProperty<Right, Left?> {
        override fun getValue(thisRef: Right, property: KProperty<*>) = getLeft(thisRef)
        override fun setValue(thisRef: Right, property: KProperty<*>, value: Left?) {
            set(thisRef, value)
        }
    }

    /**
     * Return a property bound to the right side of the relation
     *
     * It is only an alias of [right]
     *
     * It is provided to allow better readability when needed as the right side of a one-to-many relation often means 'children'
     */
    fun children() = right()

    /**
     * Return a property bound to the left side of the relation
     *
     * It is only an alias of [right]
     *
     * It is provided to allow better readability when needed as the left side of a one-to-many relation often means 'parent'
     */
    fun parent() = left()
}
