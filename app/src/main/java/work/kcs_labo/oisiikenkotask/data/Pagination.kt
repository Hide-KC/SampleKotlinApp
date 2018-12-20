package work.kcs_labo.oisiikenkotask.data

import org.json.JSONObject

data class Pagination(private val paginationJSON: JSONObject) {
    val total = paginationJSON.getInt("total")
    val offset = paginationJSON.getInt("offset")
    val limit = paginationJSON.getInt("limit")
}