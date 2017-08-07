package crossref

import crossref.impl.*
import io.kotlintest.matchers.beEmpty
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldBe
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

private object ParentChildRelation : SimpleOneToManyRelation<Parent, Child>()

private class Parent {
    var children by ParentChildRelation.children()
}

private class Child {
    var parent by ParentChildRelation.parent()
}

object OneToManyRelationSpec : Spek({

    given("The classes 'Parent' and 'Child, with a OneToMany relation (in that order)") {
        // See declarations above

        given("a parent instance of Parent and two children instances of Child") {
            var parent = Parent()
            var child1 = Child()
            var child2 = Child()

            beforeEachTest {
                parent = Parent()
                child1 = Child()
                child2 = Child()
            }

            it("should give no children to the parent") {
                parent.children should beEmpty()
            }

            it("should give no parent to children") {
                child1.parent shouldBe null
                child2.parent shouldBe null
            }

            on("assign a parent to children") {
                child1.parent = parent
                child2.parent = parent

                it("should effectively be assigned") {
                    child1.parent shouldBe parent
                    child2.parent shouldBe parent
                }

                it("should add the children to the parent as well") {
                    parent.children shouldBe setOf(child1, child2)
                }
            }

            on("add a child to a parent") {
                parent.children += child1
                parent.children += child2

                it("should effectively add the children") {
                    parent.children shouldBe setOf(child1, child2)
                }

                it("should assign the parent to the children as well") {
                    child1.parent shouldBe parent
                    child2.parent shouldBe parent
                }
            }

            given("the children assigned to the parent") {
                beforeEachTest { parent.children = setOf(child1, child2) }

                on("unset the parent to a child") {
                    child1.parent = null

                    it("should effectively unset the parent of the shild") {
                        child1.parent shouldBe null
                    }

                    it("should remove the child from the parent") {
                        parent.children shouldBe setOf(child2)
                    }
                }

                on("remove a child from the parent") {
                    parent.children -= child1

                    it("should effectively remove the child from the parent") {
                        parent.children shouldBe setOf(child2)
                    }

                    it("should unset the parent of the child") {
                        child1.parent shouldBe null
                    }
                }

                on("set a new parent to a child") {
                    child1.parent = Parent()

                    it("should remove the child from the parent") {
                        parent.children shouldBe setOf(child2)
                    }
                }

                on("add a child to an other parent") {
                    Parent().children += child1

                    it("should remove the child from the previous parent") {
                        parent.children shouldBe setOf(child2)
                    }
                }
            }
        }
    }
})
