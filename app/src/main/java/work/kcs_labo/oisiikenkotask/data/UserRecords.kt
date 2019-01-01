package work.kcs_labo.oisiikenkotask.data

import org.json.JSONObject

private const val PAGINATION = "pagination"
private const val COOKING_RECORDS = "cooking_records"
class UserRecords(parentJSON: JSONObject) {
    val pagination = Pagination(parentJSON.getJSONObject(PAGINATION))
    val cookingRecords: List<CookingRecord>

    init {
        val tempRecords = mutableListOf<CookingRecord>()
        val recordsJSONArray = parentJSON.getJSONArray(COOKING_RECORDS)
        if (recordsJSONArray.length() > 0) {
            for (i in 0 until recordsJSONArray.length()) {
                val tempRecord = CookingRecord(recordsJSONArray[i] as JSONObject)
                tempRecords.add(tempRecord)
            }
        }
        cookingRecords = tempRecords
    }
}