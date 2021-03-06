package work.kcs_labo.oisiikenkotask.data.source.remote

import android.util.Log
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import work.kcs_labo.oisiikenkotask.data.Pagination
import work.kcs_labo.oisiikenkotask.data.UserRecords
import work.kcs_labo.oisiikenkotask.data.source.LIMIT
import work.kcs_labo.oisiikenkotask.data.source.AlbumDataSource
import work.kcs_labo.oisiikenkotask.data.source.cache.CookingRecordCache
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

private const val URL = "https://cooking-records.herokuapp.com/cooking_records"

class OkHttp3AlbumDataSource: AlbumDataSource, CoroutineScope {
    private val atomicBoolean: AtomicBoolean = AtomicBoolean(false)
    private var job = Job()

    override val coroutineContext = Dispatchers.Main

    private val client = OkHttpClient()
    val cache = CookingRecordCache()
    private lateinit var lastPagination: Pagination

    /**
     * 料理記録リストの取得
     *
     * @param offset
     * @param limit
     * @param callback
     */
    override fun getCookingRecords(offset: Int, limit: Int, callback: AlbumDataSource.LoadRecordsCallback) {
        if (!atomicBoolean.get()){
            atomicBoolean.set(true)
            val handler = CoroutineExceptionHandler { _, e ->
                atomicBoolean.set(false)
                callback.onDataNotAvailable(e)
            }

            job = launch(coroutineContext + handler) {
                val content = getContentDeferred(offset, limit).await()
                if (content != null) {
                    try {
                        val userRecords = UserRecords(JSONObject(content))
                        cache.putRecords(userRecords.cookingRecords)
                        lastPagination = userRecords.pagination
                        callback.onRecordsLoaded(userRecords)
                    } finally {
                        atomicBoolean.set(false)
                    }
                }
            }
        }
    }

    /**
     * 料理記録の取得
     *
     * @param offset
     * @param limit
     * @param recordId
     * @param callback
     */
    override fun getCookingRecord(offset: Int, limit: Int, recordId: Int, callback: AlbumDataSource.GetRecordCallback) {
        when {
            recordId > limit -> throw IndexOutOfBoundsException("recordIdはゼロ以上limit未満の値にしてください")
            recordId < 0 -> throw IllegalArgumentException("recordIdは負の値を取れません")
        }

        if (!atomicBoolean.get()){
            atomicBoolean.set(true)
            val handler = CoroutineExceptionHandler { _, e ->
                atomicBoolean.set(false)
                callback.onDataNotAvailable(e)
            }

            job = launch(coroutineContext + handler) {
                val content = getContentDeferred(offset, limit).await()
                if (content != null) {
                    try {
                        val userRecords = UserRecords(JSONObject(content))
                        cache.putRecord(userRecords.cookingRecords[recordId])
                        callback.onRecordLoaded(userRecords.cookingRecords[recordId])
                    } finally {
                        atomicBoolean.set(false)
                    }
                }
            }
        }
    }

    /**
     * 料理記録の追加取得
     *
     * @param offset
     * @param limit
     * @param callback
     */
    override fun getAdditionalRecords(offset: Int, limit: Int, callback: AlbumDataSource.LoadAdditionalRecordCallback) {
        val lastOffset = lastPagination.offset
        val total = lastPagination.total
        val _limit = LIMIT

        val _offset = if (lastOffset + LIMIT < total){
            lastOffset + LIMIT
        } else {
            callback.onDataNotAvailable(EmptyStackException())
            return
        }

        Log.d(OkHttp3AlbumDataSource::class.java.simpleName, atomicBoolean.get().toString())

        if (!atomicBoolean.get()){
            atomicBoolean.set(true)
            val handler = CoroutineExceptionHandler { _, e ->
                atomicBoolean.set(false)
                callback.onDataNotAvailable(e)
            }

            job = launch(coroutineContext + handler) {
                val content = getContentDeferred(_offset, _limit).await()
                if (content != null) {
                    try {
                        val userRecords = UserRecords(JSONObject(content))
                        cache.putRecords(userRecords.cookingRecords)
                        Log.d(OkHttp3AlbumDataSource::class.java.simpleName, content)
                        lastPagination = userRecords.pagination
                        callback.onAdditionalRecordLoaded(userRecords)
                    } finally {
                        atomicBoolean.set(false)
                    }
                }
            }
        }
    }

    /**
     * コルーチンキャンセル
     */
    override fun cancelRequest() {
        job.cancel()
        atomicBoolean.set(false)
    }

    /**
     * サーバとの通信処理、料理記録のJSON取得
     *
     * @param offset
     * @param limit
     */
    private suspend fun getContentDeferred(offset: Int = 0, limit: Int = 10): Deferred<String?> =
        coroutineScope {
            async {
                withContext(Dispatchers.IO) {
                    val buffer = StringBuffer().append(URL)
                    Log.d(this.javaClass.simpleName, "getContentDeferred!")
                    if (offset < 0 || limit < 0) {
                        throw IllegalArgumentException("offsetまたはlimitに負の値は取れません")
                    } else {
                        //Urlにパラメータ追加
                        buffer.append("?offset=$offset&limit=$limit")
                    }

                    val request = Request.Builder().url(buffer.toString()).build()
                    val response = client.newCall(request).execute()
                    response.body()?.string()
                }
            }
        }
}