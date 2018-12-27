package work.kcs_labo.oisiikenkotask.list

import work.kcs_labo.oisiikenkotask.data.CookingRecord

class RecyclerRecordModel(val record: CookingRecord) {
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(record: CookingRecord)
    }

    fun itemClick(record: CookingRecord) {
        listener?.onItemClick(record)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}