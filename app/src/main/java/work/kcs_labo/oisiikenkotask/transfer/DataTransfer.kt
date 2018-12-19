package work.kcs_labo.oisiikenkotask.transfer

import work.kcs_labo.oisiikenkotask.data.CookingRecord

interface DataTransfer {
    fun getCookingRecords(): List<CookingRecord>
    fun getPagination(): Pagination
}