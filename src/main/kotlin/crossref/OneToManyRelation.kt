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
            val currentValues: IdentityHashSet<Right> = rightsByLeft[thisRef] ?: IdentityHashSet()
            val newValues = IdentityHashSet(value)

            currentValues.filter { it !in newValues }.forEach { set(it, null) }
            newValues.filter { it !in currentValues }.forEach { set(it, thisRef) }
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
