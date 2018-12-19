package work.kcs_labo.oisiikenkotask.transfer

import work.kcs_labo.oisiikenkotask.data.CookingRecord

class AlbumDataTransfer(private val dataTransfer: DataTransfer): DataTransfer {
    override fun getCookingRecords(): List<CookingRecord> {
        return dataTransfer.getCookingRecords()
    }

    override fun getPagination(): Pagination {
        return dataTransfer.getPagination()
    }
}