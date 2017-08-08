package crossref

import io.kotlintest.matchers.beEmpty
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldBe
import org.junit.Before
import org.junit.Test

class NotNullOneToManyRelationTest {

    object ParentChildRelation : NotNullOneToManyRelation<Parent, Child>()

    class Parent {
        val children: Set<Child> by ParentChildRelation.children()
    }

    class Child(parent: Parent) {
        var parent: Parent by ParentChildRelation.parent(this, parent)
    }

    lateinit var parent: Parent
    lateinit var child: Child

    @Before
    fun setUp() {

        // given
        parent = Parent()
        child = Child(parent)
    }

    @Test
    fun `the parent should contains the child`() {

        // then
        parent.children shouldBe setOf(child)
    }

    @Test
    fun `changing the parent of the child should remove it reference from the old parent as well`() {

        // when
        child.parent = Parent()

        // then
        parent.children should beEmpty()
    }

    @Test
    fun `assigning a parent to the child should add the child to the new parent as well`() {

        // given
        val newParent = Parent()

        // when
        child.parent = newParent

        // then
        newParent.children shouldBe setOf(child)
    }
}
