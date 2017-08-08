package crossref

import io.kotlintest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

class OneToOneRelationTest {

    object A2B : OneToOneRelation<A, B>()

    class A {
        var b: B? by A2B.right()
    }

    class B {
        var a: A? by A2B.left()
    }

    lateinit var a: A
    lateinit var b: B

    @Before
    fun setUp() {

        // given
        a = A()
        b = B()
    }

    @Test
    fun `instantiation of the classes shouldn't assign any relation`() {

        // then
        a.b shouldBe null
        b.a shouldBe null
    }

    @Test
    fun `assigning a to b should assign b to as well`() {

        // when
        a.b = b

        // then
        b.a shouldBe a
    }

    @Test
    fun `assigning b to a should assign a to b as well`() {

        // when
        b.a = a

        // then
        a.b = b
    }

    @Test
    fun `removing a from b should remove b from a as well`() {

        // given
        a.b = b

        // when
        b.a = null

        // then
        a.b shouldBe null
    }

    @Test
    fun `removing b from a should remove a from b as well`() {

        // given
        b.a = a

        // when
        a.b = null

        // then
        b.a shouldBe null
    }

    @Test
    fun `changing the reference of a should remove it reference from b as well`() {

        // given
        b.a = a

        // when
        a.b = B()

        // then
        b.a shouldBe null
    }

    @Test
    fun `changing the reference of b shold remove it reference from a as well`() {

        // given
        a.b = b

        // when
        b.a = A()

        // then
        a.b shouldBe null
    }
}
