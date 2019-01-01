package work.kcs_labo.oisiikenkotask.data.source

import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.data.UserRecords

/**
 * Repository操作用インターフェース定義
 */
interface AlbumDataSource {

    interface RecordError{
        fun onDataNotAvailable(e: Throwable)
    }

    interface LoadRecordsCallback: RecordError {
        fun onRecordsLoaded(userRecords: UserRecords)
    }

    interface GetRecordCallback: RecordError {
        fun onRecordLoaded(cookingRecord: CookingRecord)
    }

    interface LoadAdditionalRecordCallback: RecordError {
        fun onAdditionalRecordLoaded(userRecords: UserRecords)
    }

    fun getCookingRecords(offset: Int, limit: Int, callback: LoadRecordsCallback)

    fun getCookingRecord(offset: Int, limit: Int, recordId: Int, callback: GetRecordCallback)

    fun getAdditionalRecords(offset: Int, limit: Int, callback: LoadAdditionalRecordCallback)

    fun cancelRequest()
}