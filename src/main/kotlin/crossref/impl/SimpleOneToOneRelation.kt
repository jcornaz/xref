package crossref.impl

import crossref.OneToOneRelation

open class SimpleOneToOneRelation<Left : Any, Right : Any> : OneToOneRelation<Left, Right> {

    private val rightByLeft = HashMap<Int, Right>()
    private val leftByRight = HashMap<Int, Left>()

    override final fun set(left: Left?, right: Right?): Unit = synchronized(this) {
        if (left != null) {
            val id = idOfLeft(left)

            rightByLeft[id]?.let {
                leftByRight -= idOfRight(it)
            }

            if (right != null)
                rightByLeft[id] = right
            else
                rightByLeft -= id
        }

        if (right != null) {
            val id = idOfRight(right)

            leftByRight[id]?.let {
                rightByLeft -= idOfLeft(it)
            }

            if (left != null)
                leftByRight[id] = left
            else
                leftByRight -= id
        }
    }

    override final fun getLeft(right: Right): Left? = leftByRight[idOfRight(right)]
    override final fun getRight(left: Left): Right? = rightByLeft[idOfLeft(left)]

    open fun idOfLeft(left: Left): Int = System.identityHashCode(left)
    open fun idOfRight(right: Right): Int = System.identityHashCode(right)
}
