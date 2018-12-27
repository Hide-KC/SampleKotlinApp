package work.kcs_labo.oisiikenkotask.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import work.kcs_labo.oisiikenkotask.R
import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.data.source.LIMIT
import work.kcs_labo.oisiikenkotask.data.source.AlbumDataSource
import work.kcs_labo.oisiikenkotask.data.source.AlbumRepository
import work.kcs_labo.oisiikenkotask.list.RecyclerRecordModel
import work.kcs_labo.oisiikenkotask.util.RecipeTypeEnum

class MainViewModel(
    application: Application,
    private val albumRepository: AlbumRepository
) : AndroidViewModel(application) {

    private val filtering = Filtering()
    val scrollPosition = MutableLiveData<Int>()
    val headerDrawableId = MutableLiveData<Int>()
    val recordModels = MutableLiveData<List<RecyclerRecordModel>>()
    private val orgRecordModels: List<RecyclerRecordModel> = listOf()

    fun startSync(limit: Int = LIMIT, callback: AlbumDataSource.LoadRecordsCallback) {
        albumRepository.getCookingRecords(0, limit, callback)
    }

    fun setRecords(records: List<CookingRecord>){
        val list = mutableListOf<RecyclerRecordModel>()
        for (record in records){
            val viewModel = RecyclerRecordModel(record)
            list.add(viewModel)
        }
        recordModels.value = list
    }

    fun addRecords(records: List<CookingRecord>){
        val list = recordModels.value
        if (list != null){
            val mutableList = list.toMutableList()
            for (record in records){
                val viewModel = RecyclerRecordModel(record)
                mutableList.add(viewModel)
            }
            recordModels.value = mutableList
        }
    }

    fun addSync(offset: Int = 0, limit: Int = 10, callback: AlbumDataSource.LoadAdditionalRecordCallback){
        albumRepository.getAdditionalRecords(offset, limit, callback)
    }

    fun setScrollPosition(position: Int){
        scrollPosition.value = position
    }

    fun cancelRequest(){
        albumRepository.cancelRequest()
    }

    /**
     * 現在のフィルタを実行
     */
    private fun obtainFiltering(){
//        val list = orgRecords
//            .filter { record ->
//                if (filtering.recipeTypeEnum == RecipeTypeEnum.ALL_DISH){
//                    true
//                } else {
//                    record.recipeType == filtering.recipeTypeEnum.recipeType
//                }
//            }.filter { record ->
//                if (filtering.searchWord == null || filtering.searchWord!!.isEmpty()){
//                    true
//                } else {
//                    record.comment.contains(filtering.searchWord!!)
//                }
//            }
//        filteredRecords.value = list
    }

    /**
     * レシピ種別のフィルタを設定
     */
    fun setRecipeType(enum: RecipeTypeEnum){
        filtering.recipeTypeEnum = enum
        val resId = when (enum){
            RecipeTypeEnum.ALL_DISH -> R.drawable.ic_round_restaurant
            RecipeTypeEnum.MAIN_DISH -> R.drawable.ic_fish
            RecipeTypeEnum.SIDE_DISH -> R.drawable.ic_salad
            RecipeTypeEnum.SOUP -> R.drawable.ic_soup
        }
        headerDrawableId.value = resId
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
        var recipeTypeEnum: RecipeTypeEnum = RecipeTypeEnum.ALL_DISH
    }
}