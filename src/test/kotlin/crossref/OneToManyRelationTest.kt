package crossref

import io.kotlintest.matchers.beEmpty
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

class OneToManyRelationTest {

    object ParentChildRelation : OneToManyRelation<Parent, Child>()

    class Parent {
        var children by ParentChildRelation.children()
    }

    class Child {
        var parent by ParentChildRelation.parent()
    }

    lateinit var parent: Parent
    lateinit var child1: Child
    lateinit var child2: Child

    @Before
    fun setUp() {

        // given
        parent = Parent()
        child1 = Child()
        child2 = Child()
    }

    @Test
    fun `instantiation of the classes shouldn't assign any relation`() {

        // then
        parent.children should beEmpty()
        child1.parent shouldBe null
        child2.parent shouldBe null
    }

    @Test
    fun `assigning a parent to a child should add the child to the parent as well`() {

        // when
        child1.parent = parent
        child2.parent = parent

        // then
        parent.children shouldBe setOf(child1, child2)
    }

    @Test
    fun `adding a child to a parent should assign the parent to the child as well`() {

        // when
        parent.children += child1
        parent.children += child2

        // then
        child1.parent shouldBe parent
        child2.parent shouldBe parent
    }

    @Test
    fun `removing a parent from a child should remove the child from the parent as well`() {

        // given
        parent.children = setOf(child1, child2)

        // when
        child1.parent = null

        // then
        parent.children shouldBe setOf(child2)
    }

    @Test
    fun `removing a child from a parent should remove the parent from the child as well`() {

        // given
        parent.children = setOf(child1, child2)

        // when
        parent.children -= child1

        // then
        child1.parent shouldBe null
    }

    @Test
    fun `changing the parent of a child should remove the child from the old parent as well`() {

        // given
        parent.children = setOf(child1, child2)

        // when
        child1.parent = Parent()

        // then
        parent.children shouldBe setOf(child2)
    }

    @Test
    fun `adding a child to an other parent should remove the child from the old parent as well`() {

        // given
        parent.children = setOf(child1, child2)

        // when
        Parent().children += child1

        // then
        parent.children shouldBe setOf(child2)
    }
}

