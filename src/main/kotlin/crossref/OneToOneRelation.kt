package crossref

interface OneToOneRelation<Left : Any, Right : Any> {
    operator fun set(left: Left?, right: Right?)

    fun getLeft(right: Right): Left?
    fun getRight(left: Left): Right?
}
