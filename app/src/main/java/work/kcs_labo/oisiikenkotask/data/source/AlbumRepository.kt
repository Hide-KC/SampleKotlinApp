package work.kcs_labo.oisiikenkotask.data.source

import work.kcs_labo.oisiikenkotask.data.Pagination

const val LIMIT = 10
class AlbumRepository(private val albumDataSource: AlbumDataSource):
    AlbumDataSource {

    override fun getCookingRecords(offset: Int, limit: Int, callback: AlbumDataSource.LoadRecordsCallback) {
        albumDataSource.getCookingRecords(offset, limit, callback)
    }

    override fun getCookingRecord(offset: Int, limit: Int, recordId: Int, callback: AlbumDataSource.GetRecordCallback){
        albumDataSource.getCookingRecord(offset, limit, recordId, callback)
    }

    override fun getAdditionalRecords(offset: Int, limit: Int, callback: AlbumDataSource.LoadAdditionalRecordCallback){
        albumDataSource.getAdditionalRecords(offset, limit, callback)
    }
}