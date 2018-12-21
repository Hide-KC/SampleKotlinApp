package work.kcs_labo.oisiikenkotask.data.source.remote

import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import work.kcs_labo.oisiikenkotask.data.UserRecords
import work.kcs_labo.oisiikenkotask.data.source.AlbumDataSource
import java.io.IOException

const val URL = "https://cooking-records.herokuapp.com/cooking_records"

class OkHttp3AlbumDataSource: AlbumDataSource, CoroutineScope {
    private val client = OkHttpClient()
    val cache = linkedMapOf<String, UserRecords>()

    private val job = Job()
    override val coroutineContext = Dispatchers.Main + job

    override fun getCookingRecords(offset: Int, limit: Int, callback: AlbumDataSource.LoadRecordsCallback) {
        launch {
            val content = getContentDeferred(offset, limit).await()
            if (content != null) {
                try {
                    val userRecords = UserRecords(JSONObject(content))
                    callback.onRecordsLoaded(userRecords)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    callback.onDataNotAvailable()
                } catch (e: IOException) {
                    e.printStackTrace()
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun getCookingRecord(offset: Int, limit: Int, recordId: Int, callback: AlbumDataSource.GetRecordCallback) {
        when {
            recordId > limit -> throw IndexOutOfBoundsException("recordIdはゼロ以上limit未満の値にしてください")
            recordId < 0 -> throw IllegalArgumentException("recordIdは負の値を取れません")
        }

        launch {
            val content = getContentDeferred(offset, limit).await()
            if (content != null) {
                try {
                    val userRecords = UserRecords(JSONObject(content))
                    callback.onRecordLoaded(userRecords.cookingRecords[recordId])
                } catch (e: JSONException) {
                    e.printStackTrace()
                    callback.onDataNotAvailable()
                } catch (e: IOException) {
                    e.printStackTrace()
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun getAdditionalRecords(offset: Int, limit: Int, callback: AlbumDataSource.LoadAdditionalRecordCallback) {
        launch {
            val content = getContentDeferred(offset, limit).await()
            if (content != null) {
                try {
                    val userRecords = UserRecords(JSONObject(content))
                    callback.onAdditionalRecordLoaded(userRecords)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    callback.onDataNotAvailable()
                } catch (e: IOException) {
                    e.printStackTrace()
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    private suspend fun getContentDeferred(offset: Int = 0, limit: Int = 10): Deferred<String?> =
        async {
            withContext(Dispatchers.IO) {
                val buffer = StringBuffer().append(URL)

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