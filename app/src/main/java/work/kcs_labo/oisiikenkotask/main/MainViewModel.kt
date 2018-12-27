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
    private var orgModels: List<RecyclerRecordModel> = listOf()
    private var navigator: MainNavigator? = null

    fun startSync(limit: Int = LIMIT, callback: AlbumDataSource.LoadRecordsCallback) {
        albumRepository.getCookingRecords(0, limit, callback)
    }

    fun setRecords(records: List<CookingRecord>){
        val list = mutableListOf<RecyclerRecordModel>()
        for (record in records){
            val model = createModel(record)
            list.add(model)
        }
        orgModels = list

        val filteredList = getFilteredModels(list)
        recordModels.value = filteredList
    }

    fun addRecords(records: List<CookingRecord>){
        val mutableList = orgModels.toMutableList()
        for (record in records){
            val model = createModel(record)
            mutableList.add(model)
        }
        orgModels = mutableList

        val filteredList = getFilteredModels(mutableList)
        recordModels.value = filteredList
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

    fun setNavigator(navigator: MainNavigator?){
        this.navigator = navigator
    }

    private fun createModel(record: CookingRecord): RecyclerRecordModel =
            RecyclerRecordModel(record).apply {
                setNavigator(navigator)
            }

    /**
     * 現在のフィルタを実行
     */
    private fun getFilteredModels(models: List<RecyclerRecordModel>): List<RecyclerRecordModel>{
        return models
            .filter { model ->
                if (filtering.recipeTypeEnum == RecipeTypeEnum.ALL_DISH){
                    true
                } else {
                    model.record.recipeType == filtering.recipeTypeEnum.recipeType
                }
            }.filter { model ->
                if (filtering.searchWord == null || filtering.searchWord!!.isEmpty()){
                    true
                } else {
                    model.record.comment.contains(filtering.searchWord!!)
                }
            }
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
        recordModels.value = getFilteredModels(orgModels)
    }

    /**
     * コメントのフィルタを設定
     * @param searchWord 部分一致でコメント検索
     */
    fun setSearchWord(searchWord: String?){
        filtering.searchWord = searchWord
        recordModels.value = getFilteredModels(orgModels)
    }

    /**
     * フィルタ管理用
     */
    private class Filtering {
        var searchWord: String? = null
        var recipeTypeEnum: RecipeTypeEnum = RecipeTypeEnum.ALL_DISH
    }
}