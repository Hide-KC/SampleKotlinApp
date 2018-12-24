package work.kcs_labo.oisiikenkotask.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.data.source.LIMIT
import work.kcs_labo.oisiikenkotask.data.source.AlbumDataSource
import work.kcs_labo.oisiikenkotask.data.source.AlbumRepository
import work.kcs_labo.oisiikenkotask.util.RecipeTypeEnum

class MainViewModel(
    application: Application,
    private val albumRepository: AlbumRepository
) : AndroidViewModel(application) {

    private var orgRecords = listOf<CookingRecord>()
    private val filtering = Filtering()
    val filteredRecords = MutableLiveData<List<CookingRecord>>()
    val scrollPosition = MutableLiveData<Int>()
    val headerDrawable = MutableLiveData<Int>()

    fun startSync(limit: Int = LIMIT, callback: AlbumDataSource.LoadRecordsCallback) {
        albumRepository.getCookingRecords(0, limit, callback)
    }

    fun setRecords(records: List<CookingRecord>){
        orgRecords = records
        filteredRecords.value = orgRecords
        obtainFiltering()
    }

    fun addRecords(records: List<CookingRecord>){
        orgRecords = orgRecords.plus(records)
        filteredRecords.value = orgRecords
        obtainFiltering()
    }

    fun addSync(offset: Int = 0, limit: Int = 10, callback: AlbumDataSource.LoadAdditionalRecordCallback){
        albumRepository.getAdditionalRecords(offset, limit, callback)
    }

    fun setScrollPosition(position: Int){
        scrollPosition.value = position
    }

    fun setHeaderDrawable(resId: Int){
        headerDrawable.value = resId
    }

    /**
     * 現在のフィルタを実行
     */
    private fun obtainFiltering(){
        val list = orgRecords
            .filter { record ->
                if (filtering.recipeTypeEnum == null){
                    true
                } else {
                    record.recipeType == filtering.recipeTypeEnum!!.recipeType
                }
            }.filter { record ->
                if (filtering.searchWord == null || filtering.searchWord!!.isEmpty()){
                    true
                } else {
                    record.comment.contains(filtering.searchWord!!)
                }
            }
        filteredRecords.value = list
    }

    /**
     * レシピ種別のフィルタを設定
     */
    fun setRecipeType(enum: RecipeTypeEnum?){
        filtering.recipeTypeEnum = enum
        obtainFiltering()
    }

    /**
     * コメントのフィルタを設定
     * @param searchWord 部分一致でコメント検索
     */
    fun setSearchWord(searchWord: String?){
        filtering.searchWord = searchWord
        obtainFiltering()
    }

    /**
     * フィルタ管理用
     */
    private class Filtering {
        var searchWord: String? = null
        var recipeTypeEnum: RecipeTypeEnum? = null
    }
}