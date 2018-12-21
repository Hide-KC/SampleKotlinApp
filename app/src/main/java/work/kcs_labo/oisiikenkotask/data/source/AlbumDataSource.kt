package work.kcs_labo.oisiikenkotask.data.source

import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.data.UserRecords

interface AlbumDataSource {

    interface LoadRecordsCallback {
        fun onRecordsLoaded(userRecords: UserRecords)
        fun onDataNotAvailable()
    }

    interface GetRecordCallback {
        fun onRecordLoaded(cookingRecord: CookingRecord)
        fun onDataNotAvailable()
    }

    interface LoadAdditionalRecordCallback {
        fun onAdditionalRecordLoaded(userRecords: UserRecords)
        fun onDataNotAvailable()
    }

    fun getCookingRecords(offset: Int, limit: Int, callback: LoadRecordsCallback)

    fun getCookingRecord(offset: Int, limit: Int, recordId: Int, callback: GetRecordCallback)

    fun getAdditionalRecords(offset: Int, limit: Int, callback: LoadAdditionalRecordCallback)
}