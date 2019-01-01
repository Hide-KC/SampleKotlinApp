package work.kcs_labo.oisiikenkotask.data.source.cache

import android.support.v4.util.LruCache
import work.kcs_labo.oisiikenkotask.data.CookingRecord

class CookingRecordCache {
    private val recordCache = LruCache<String, CookingRecord>(50)

    fun getRecord(imageUrl: String): CookingRecord? = recordCache.get(imageUrl)

    fun putRecord(record: CookingRecord){
        if (recordCache.get(record.imageUrl) == null) {
            recordCache.put(record.imageUrl, record)
        }
    }

    fun putRecords(records: List<CookingRecord>){
        for (record in records){
            putRecord(record)
        }
    }
}