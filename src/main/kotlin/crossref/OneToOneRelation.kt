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

import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

open class OneToOneRelation<Left : Any, Right : Any> {

    private val rightByLeft = IdentityHashMap<Left, Right>()
    private val leftByRight = IdentityHashMap<Right, Left>()

    fun set(left: Left?, right: Right?) = synchronized(this) {
        if (left != null) {
            rightByLeft[left]?.let {
                leftByRight -= it
            }

            if (right != null)
                rightByLeft[left] = right
            else
                rightByLeft -= left
        }

        if (right != null) {
            leftByRight[right]?.let {
                rightByLeft -= it
            }

            if (left != null)
                leftByRight[right] = left
            else
                leftByRight -= right
        }
    }

    fun getLeft(right: Right): Left? = synchronized(this) {
        leftByRight[right]
    }

    fun getRight(left: Left): Right? = synchronized(this) {
        rightByLeft[left]
    }

    fun right() = object : ReadWriteProperty<Left, Right?> {
        override fun getValue(thisRef: Left, property: KProperty<*>) = getRight(thisRef)
        override fun setValue(thisRef: Left, property: KProperty<*>, value: Right?) = set(thisRef, value)
    }

    fun left() = object : ReadWriteProperty<Right, Left?> {
        override fun getValue(thisRef: Right, property: KProperty<*>) = getLeft(thisRef)
        override fun setValue(thisRef: Right, property: KProperty<*>, value: Left?) = set(value, thisRef)
    }
}
