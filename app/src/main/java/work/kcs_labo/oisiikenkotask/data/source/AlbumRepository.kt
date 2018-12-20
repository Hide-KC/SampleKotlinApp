package work.kcs_labo.oisiikenkotask.data.source

class AlbumRepository(private val albumDataSource: AlbumDataSource):
    AlbumDataSource {
    override fun getCookingRecords(offset: Int, limit: Int, callback: AlbumDataSource.LoadRecordsCallback) {
        albumDataSource.getCookingRecords(offset, limit, callback)
    }

    override fun getCookingRecord(offset: Int, limit: Int, recordId: Int, callback: AlbumDataSource.GetRecordCallback) {
        albumDataSource.getCookingRecord(offset, limit, recordId, callback)
    }
}