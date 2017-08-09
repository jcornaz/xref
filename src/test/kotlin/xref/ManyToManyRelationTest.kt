package xref

import io.kotlintest.matchers.beEmpty
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

class ManyToManyRelationTest {

    object A2B : ManyToManyRelation<A, B>()

    class A {
        var set: Set<B> by A2B.right()
    }

    class B {
        var set: Set<A> by A2B.left()
    }

    lateinit var a1: A
    lateinit var a2: A
    lateinit var b1: B
    lateinit var b2: B

    @Before
    fun setUp() {

        // given
        a1 = A()
        a2 = A()
        b1 = B()
        b2 = B()
    }

    @Test
    fun `instantiation of the classes shouldn't assign any relation`() {

        // then
        a1.set should beEmpty()
        a2.set should beEmpty()
        b1.set should beEmpty()
        b2.set should beEmpty()
    }

    @Test
    fun `adding a A to a B should add the B to the A as well`() {

        // when
        a1.set += setOf(b1, b2)

        // then
        b1.set shouldBe setOf(a1)
        b2.set shouldBe setOf(a1)
    }

    @Test
    fun `adding a B to a A should add the A to the B as well`() {

        // when
        b1.set += setOf(a1, a2)

        // then
        a1.set shouldBe setOf(b1)
        a2.set shouldBe setOf(b1)
    }

    @Test
    fun `removing a A from a B should remove the B from the A as well`() {

        // given
        b1.set = setOf(a1, a2)

        // when
        a1.set -= b1

        // then
        b1.set shouldBe setOf(a2)
    }

    @Test
    fun `removing a B from a A should remove the A from the B as well`() {

        // given
        a1.set = setOf(b1, b2)

        // when
        b1.set -= a1

        // then
        a1.set shouldBe setOf(b2)
    }
}

