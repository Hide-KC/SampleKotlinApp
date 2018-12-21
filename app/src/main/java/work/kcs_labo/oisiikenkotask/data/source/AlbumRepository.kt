package work.kcs_labo.oisiikenkotask.data.source

import work.kcs_labo.oisiikenkotask.data.Pagination

const val OFFSET_INCREMENT = 20
const val ADDITIONAL_LIMIT = 20
class AlbumRepository(private val albumDataSource: AlbumDataSource):
    AlbumDataSource {

    private val lastPagination: Pagination? = null

    override fun getCookingRecords(offset: Int, limit: Int, callback: AlbumDataSource.LoadRecordsCallback) {
        albumDataSource.getCookingRecords(offset, limit, callback)
    }

    override fun getCookingRecord(offset: Int, limit: Int, recordId: Int, callback: AlbumDataSource.GetRecordCallback) {
        albumDataSource.getCookingRecord(offset, limit, recordId, callback)
    }

    override fun getAdditionalRecords(offset: Int, limit: Int, callback: AlbumDataSource.LoadAdditionalRecordCallback) {
        var _offset = offset
        var _limit = limit

        if (lastPagination != null){
            _offset = lastPagination.offset + OFFSET_INCREMENT
            _limit = ADDITIONAL_LIMIT
        }

        albumDataSource.getAdditionalRecords(_offset, _limit, callback)
    }
}