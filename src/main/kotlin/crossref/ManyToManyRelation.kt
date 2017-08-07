package crossref

interface ManyToManyRelation<Left : Any, Right : Any> {
    fun add(left: Left, right: Right)
    fun remove(left: Left, right: Right)

    fun getRight(left: Left): Set<Right>
    fun getLeft(right: Right): Set<Left>
}
