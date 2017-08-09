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
            val currentValues: IdentityHashSet<Right> = rightsByLeft[thisRef] ?: IdentityHashSet()
            val newValues = IdentityHashSet(value)

            currentValues.filter { it !in newValues }.forEach { remove(thisRef, it) }
            newValues.filter { it !in currentValues }.forEach { add(thisRef, it) }
        }
    }
}
