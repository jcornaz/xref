package crossref.impl

import crossref.OneToOneRelation

open class SimpleOneToOneRelation<Left : Any, Right : Any> : OneToOneRelation<Left, Right> {

    private val rightByLeft = HashMap<Left, Right>()
    private val leftByRight = HashMap<Right, Left>()

    @Synchronized
    override final fun set(left: Left?, right: Right?) {
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

    override final fun getLeft(right: Right): Left? = synchronized(this) {
        leftByRight[right]
    }

    override final fun getRight(left: Left): Right? = synchronized(this) {
        rightByLeft[left]
    }
}
