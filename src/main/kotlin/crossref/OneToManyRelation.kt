package crossref

interface OneToManyRelation<Left : Any, Right : Any> {
    operator fun set(right: Right, left: Left?)

    fun getLeft(right: Right): Left?
    fun getRight(left: Left): Set<Right>
}
