package work.kcs_labo.oisiikenkotask.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.support.v4.content.ContextCompat
import android.view.View
import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.data.source.LIMIT
import work.kcs_labo.oisiikenkotask.data.source.AlbumDataSource
import work.kcs_labo.oisiikenkotask.data.source.AlbumRepository
import work.kcs_labo.oisiikenkotask.list.RecordModel
import work.kcs_labo.oisiikenkotask.util.RecipeTypeEnum
import work.kcs_labo.oisiikenkotask.SingleLiveEvent

class MainViewModel(
    application: Application,
    private val albumRepository: AlbumRepository
) : AndroidViewModel(application) {

    val scrollPosition = MutableLiveData<Int>()
    val headerDrawableId = MutableLiveData<Int>()
    val headerColorTo = MutableLiveData<Int>()
    val selectedRecipeType = MutableLiveData<String>()
    val recordModels = MutableLiveData<List<RecordModel>>()
    val displayRecord = MutableLiveData<CookingRecord>()
    val emptyTextVisibility = MutableLiveData<Int>()
    val openRecipeDialogOK = SingleLiveEvent<Unit>()
    val openRecipeDialogCancel = SingleLiveEvent<Unit>()

    private val filtering = Filtering()
    private var orgModels: List<RecordModel> = listOf()
    private var navigator: MainNavigator? = null

    /**
     * 料理記録の初期読み込み
     *
     * @param limit
     * @param callback
     */
    fun startSync(limit: Int = LIMIT, callback: AlbumDataSource.LoadRecordsCallback) {
        albumRepository.getCookingRecords(0, limit, callback)
    }

    /**
     * 初期読み込み用のメソッド
     * リストを丸ごと入れ替え
     */
    fun setRecords(records: List<CookingRecord>){
        val list = mutableListOf<RecordModel>()
        for (record in records){
            val model = createModel(record)
            list.add(model)
        }
        orgModels = list

        val filteredList = getFilteredModels(list)
        recordModels.value = filteredList
    }

    /**
     * 追加読み込み用のメソッド
     * 既存リストに結合
     */
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

    /**
     * 料理記録の追加読み込み
     *
     * @param offset
     * @param limit
     * @param callback
     */
    fun addSync(offset: Int = 0, limit: Int = 10, callback: AlbumDataSource.LoadAdditionalRecordCallback){
        albumRepository.getAdditionalRecords(offset, limit, callback)
    }

    /**
     * RecyclerViewのスクロール位置制御
     */
    fun setScrollPosition(position: Int){
        scrollPosition.value = position
    }

    /**
     * EmptyTextViewの表示制御
     *
     * @param visibility View.GONE, View.INVISIBLE or View.Visible
     */
    fun setVisibility(visibility: Int){
        if (visibility == View.GONE || visibility == View.INVISIBLE || visibility == View.VISIBLE){
            emptyTextVisibility.value = visibility
        }
    }

    /**
     * コルーチンキャンセル
     */
    fun cancelRequest(){
        albumRepository.cancelRequest()
    }

    /**
     * Navigatorセット
     *
     * @param navigator 実体はActivityとする
     */
    fun setNavigator(navigator: MainNavigator?){
        this.navigator = navigator
    }

    /**
     * 横向き時、ImageViewに表示するRecordの制御
     *
     * @param record
     */
    fun displayRecord(record: CookingRecord){
        displayRecord.value = record
    }

    /**
     * RecyclerViewに表示するモデルの生成
     *
     * @param record
     */
    private fun createModel(record: CookingRecord): RecordModel =
        RecordModel(record).apply {
            setNavigator(navigator)
        }

    /**
     * 現在のフィルタを実行
     *
     * @param models
     */
    private fun getFilteredModels(models: List<RecordModel>): List<RecordModel>{
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
     *
     * @param enum RecipeTypeEnum
     */
    fun setRecipeType(enum: RecipeTypeEnum): Boolean{
        val isSameTypeSelected = enum == filtering.recipeTypeEnum
        filtering.recipeTypeEnum = enum

        headerDrawableId.value = enum.symbolIconId
        headerColorTo.value = ContextCompat.getColor(getApplication(), enum.symbolColorId)
        recordModels.value = getFilteredModels(orgModels)
        selectedRecipeType.value = getApplication<Application>().resources.getString(enum.recipeStringId)
        return isSameTypeSelected
    }

    /**
     * コメントのフィルタを設定
     *
     * @param searchWord 部分一致でコメント検索
     */
    fun setSearchWord(searchWord: String?){
        filtering.searchWord = searchWord
        recordModels.value = getFilteredModels(orgModels)
    }

    /**
     * フィルタ管理用クラス
     */
    private class Filtering {
        var searchWord: String? = null
        var recipeTypeEnum: RecipeTypeEnum = RecipeTypeEnum.ALL_DISH
    }
}