package crossref.impl

import crossref.OneToManyRelation

open class SimpleOneToManyRelation<Left : Any, Right : Any> : OneToManyRelation<Left, Right> {

    private val rightsByLeft = HashMap<Left, Set<Right>>()
    private val leftByRight = HashMap<Right, Left>()

    @Synchronized
    override fun set(right: Right, left: Left?) {

        leftByRight[right]?.let {
            rightsByLeft[it] = rightsByLeft[it].orEmpty() - right
        }

        if (left == null) {
            leftByRight -= right
        } else {
            leftByRight[right] = left
            rightsByLeft[left] = rightsByLeft[left].orEmpty() + right
        }
    }

    @Synchronized
    override fun getLeft(right: Right) = leftByRight[right]

    @Synchronized
    override fun getRight(left: Left) = rightsByLeft[left]?.let { HashSet(it) }.orEmpty()
}
