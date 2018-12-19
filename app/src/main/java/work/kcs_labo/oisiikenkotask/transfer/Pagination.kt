package work.kcs_labo.oisiikenkotask.transfer

data class Pagination(
    val total: Int,
    val offset: Int,
    val limit: Int
)