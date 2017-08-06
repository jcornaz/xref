package crossref

interface ManyToManyRelation<Left : Any, Right : Any> {
    fun add(left: Left, right: Right)
    fun remove(left: Left, right: Right)

    fun getRights(left: Left): Collection<Right>
    fun getLefts(right: Right): Collection<Left>
}
