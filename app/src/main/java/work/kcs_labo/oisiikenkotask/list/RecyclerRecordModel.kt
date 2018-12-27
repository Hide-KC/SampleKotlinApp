package work.kcs_labo.oisiikenkotask.list

import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.main.MainNavigator

class RecyclerRecordModel(val record: CookingRecord) {
    private var listener: OnItemClickListener? = null
    private var navigator: MainNavigator? = null

    interface OnItemClickListener {
        fun onItemClick(record: CookingRecord)
    }

    fun itemClick(record: CookingRecord) {
        listener?.onItemClick(record)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    fun setNavigator(navigator: MainNavigator?){
        this.navigator = navigator
    }

    fun startNewActivity(){
        navigator?.onStartNewActivity()
    }

    fun openImage(record: CookingRecord){
        navigator?.onOpenImage(record)
    }
}