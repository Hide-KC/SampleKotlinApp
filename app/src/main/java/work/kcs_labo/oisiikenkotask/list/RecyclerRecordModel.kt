package work.kcs_labo.oisiikenkotask.list

import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.main.MainNavigator

class RecyclerRecordModel(val record: CookingRecord) {
    private var navigator: MainNavigator? = null

    fun itemClick(record: CookingRecord) {
        navigator?.onOpenImage(record)
    }

    fun itemLongClick(record: CookingRecord){
        navigator?.onOpenRecipe(record)
    }

    fun startNewActivity(){
        navigator?.onStartNewActivity()
    }

    fun setNavigator(navigator: MainNavigator?){
        this.navigator = navigator
    }
}