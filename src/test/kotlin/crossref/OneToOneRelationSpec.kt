package crossref

import crossref.impl.SimpleOneToOneRelation
import crossref.impl.left
import crossref.impl.right
import io.kotlintest.matchers.shouldBe
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

// Declare a relation
private object A2B : SimpleOneToOneRelation<A, B>()

// Declare class A which has a relation to B
private class A {
    var b: B? by A2B.right()
}

// Declare class B which has a relation to A
private class B {
    var a: A? by A2B.left()
}

object OneToOneRelationTest : Spek({

    given("A and B two classes with a OneToOne relation") {
        // See declaration above

        given("a and b instances of respectively A and B") {
            var a = A()
            var b = B()

            beforeEachTest {
                a = A()
                b = B()
            }

            it("should be initialized with no reference") {
                a.b shouldBe null
                b.a shouldBe null
            }

            on("set a referencing b") {
                a.b = b

                it("should effectively makes a referencing b") {
                    a.b shouldBe b
                }

                it("should makes b referencing a as well") {
                    b.a shouldBe a
                }
            }

            on("set b referencing a") {
                b.a = a

                it("should effectively makes b referencing a") {
                    b.a shouldBe a
                }

                it("should makes a referencing b as well") {
                    a.b = b
                }
            }

            given("a and b cross referenced") {
                beforeEachTest { a.b = b }

                on("set a referencing null") {
                    a.b = null

                    it("should effectively unset the referance of a") {
                        a.b shouldBe null
                    }

                    it("should makes b referencing null as well") {
                        b.a shouldBe null
                    }
                }

                on("set b referencing null") {
                    b.a = null

                    it("should effectively unset the referance of b") {
                        b.a shouldBe null
                    }

                    it("should makes a referencing null as well") {
                        a.b shouldBe null
                    }
                }

                on("set a referencing an other instance") {
                    a.b = B()

                    it("should makes b referencing null") {
                        b.a shouldBe null
                    }
                }

                on("set b referencing an other instance") {
                    b.a = A()

                    it("should makes b referencing null") {
                        a.b shouldBe null
                    }
                }
            }
        }
    }
})
