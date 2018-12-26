package work.kcs_labo.oisiikenkotask.data.source.remote

import android.util.Log
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import work.kcs_labo.oisiikenkotask.data.Pagination
import work.kcs_labo.oisiikenkotask.data.UserRecords
import work.kcs_labo.oisiikenkotask.data.source.LIMIT
import work.kcs_labo.oisiikenkotask.data.source.AlbumDataSource
import java.io.IOException
import java.util.concurrent.atomic.AtomicBoolean

private const val URL = "https://cooking-records.herokuapp.com/cooking_records"

class OkHttp3AlbumDataSource: AlbumDataSource, CoroutineScope {
    private var job = Job()
    override val coroutineContext = Dispatchers.Main + job
    private val atomicBoolean: AtomicBoolean = AtomicBoolean(false)

    private val client = OkHttpClient()
    val cache = linkedMapOf<String, UserRecords>()
    private var lastPagination: Pagination? = null
    private val handler = CoroutineExceptionHandler { _, exception ->
        Log.d(this.javaClass.simpleName, exception.message)
    }

    override fun getCookingRecords(offset: Int, limit: Int, callback: AlbumDataSource.LoadRecordsCallback) {
        if (!atomicBoolean.get()){
            atomicBoolean.set(true)
            job = launch(handler) {
                val content = getContentDeferred(offset, limit).await()
                if (content != null) {
                    try {
                        val userRecords = UserRecords(JSONObject(content))
                        lastPagination = userRecords.pagination
                        callback.onRecordsLoaded(userRecords)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        callback.onDataNotAvailable()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        callback.onDataNotAvailable()
                    } catch (e: TimeoutCancellationException) {
                        e.printStackTrace()
                        callback.onDataNotAvailable()
                    } finally {
                        atomicBoolean.set(false)
                    }
                }
            }
        }
    }

    override fun getCookingRecord(offset: Int, limit: Int, recordId: Int, callback: AlbumDataSource.GetRecordCallback) {
        when {
            recordId > limit -> throw IndexOutOfBoundsException("recordIdはゼロ以上limit未満の値にしてください")
            recordId < 0 -> throw IllegalArgumentException("recordIdは負の値を取れません")
        }

        if (!atomicBoolean.get()){
            atomicBoolean.set(true)
            job = launch(handler) {
                val content = getContentDeferred(offset, limit).await()
                if (content != null) {
                    try {
                        val userRecords = UserRecords(JSONObject(content))
                        callback.onRecordLoaded(userRecords.cookingRecords[recordId])
                        atomicBoolean.set(false)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        callback.onDataNotAvailable()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        callback.onDataNotAvailable()
                    } catch (e: TimeoutCancellationException) {
                        e.printStackTrace()
                        callback.onDataNotAvailable()
                    } finally {
                        atomicBoolean.set(false)
                    }
                }
            }
        }
    }

    override fun getAdditionalRecords(offset: Int, limit: Int, callback: AlbumDataSource.LoadAdditionalRecordCallback) {
        val lastOffset = lastPagination?.offset
        val total = lastPagination?.total
        val _limit = LIMIT

        val _offset = if (lastOffset != null && total != null){
            if (lastOffset + LIMIT < total){
                lastOffset + LIMIT
            } else {
                return
            }
        } else {
            return
        }

        Log.d(OkHttp3AlbumDataSource::class.java.simpleName, atomicBoolean.get().toString())

        if (!atomicBoolean.get()){
            atomicBoolean.set(true)
            job = launch(handler) {
                val content = getContentDeferred(_offset, _limit).await()
                if (content != null) {
                    try {
                        val userRecords = UserRecords(JSONObject(content))
                        Log.d(OkHttp3AlbumDataSource::class.java.simpleName, content)
                        lastPagination = userRecords.pagination
                        callback.onAdditionalRecordLoaded(userRecords)
                        atomicBoolean.set(false)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        callback.onDataNotAvailable()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        callback.onDataNotAvailable()
                    } catch (e: TimeoutCancellationException) {
                        e.printStackTrace()
                        callback.onDataNotAvailable()
                    } finally {
                        atomicBoolean.set(false)
                    }
                }
            }
        }
    }

    /**
     * コルーチンの中断
     */
    override fun cancelRequest() {
        if (job.isActive) {
            job.cancel()
            atomicBoolean.set(false)
        }
    }

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
                        buffer.append("?offset=$offset&limit=&limit=$limit")
                    }

                    val request = Request.Builder().url(buffer.toString()).build()
                    val response = client.newCall(request).execute()
                    response.body()?.string()
                }
            }
        }
}