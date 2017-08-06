package crossref

import crossref.impl.SimpleOneToOneRelation
import crossref.impl.left
import crossref.impl.right
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec


class OneToOneRelationTest : StringSpec() {

    // Declare a relation
    object A2B : SimpleOneToOneRelation<A, B>()

    // Declare class A which has a relation to B
    class A {
        var b: B? by A2B.right()
    }

    // Declare class B which has a relation to A
    class B {
        var a: A? by A2B.left()
    }

    init {

        "The references should be null by default" {
            val a = A()
            val b = B()

            a.b shouldBe null
            b.a shouldBe null
        }

        "If a.b is set, b.a should be set as well" {
            val a = A()
            val b = B()

            a.b = b

            b.a shouldBe a
            a.b shouldBe b
        }

        "If a.b is unset, b.a should be unset as well" {
            val a = A()
            val b = B()

            a.b = b
            b.a = null

            b.a shouldBe null
            a.b shouldBe null
        }

        "If a.b is changed, b.a should be unset in the old reference and set in the new one" {
            val a = A()
            val b1 = B()
            val b2 = B()

            a.b = b1
            a.b = b2

            a.b shouldBe b2
            b1.a shouldBe null
            b2.a shouldBe a
        }
    }
}
