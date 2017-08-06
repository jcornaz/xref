package crossref

interface OneToManyRelation<Left : Any, Right : Any> {
    operator fun set(right: Right, left: Left?)

    fun getLeft(right: Right): Left?
    fun getRights(left: Left): Collection<Right>
}
