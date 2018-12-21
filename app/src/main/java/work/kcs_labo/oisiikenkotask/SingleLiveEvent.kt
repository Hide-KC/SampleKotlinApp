package work.kcs_labo.oisiikenkotask

import android.arch.lifecycle.MutableLiveData

/**
 * ボタンクリック等の単発イベント用
 */
class SingleLiveEvent : MutableLiveData<Void>() {
    fun call() {
        value = null
    }
}