package work.kcs_labo.oisiikenkotask.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import work.kcs_labo.oisiikenkotask.SingleLiveEvent
import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.data.source.AlbumDataSource
import work.kcs_labo.oisiikenkotask.data.source.AlbumRepository
import work.kcs_labo.oisiikenkotask.list.RecordAdapter

class MainViewModel(
    application: Application,
    private val albumRepository: AlbumRepository
) : AndroidViewModel(application) {

    val records = MutableLiveData<List<CookingRecord>>()
    val syncCommand = SingleLiveEvent()

    fun startSync(offset: Int = 0, limit: Int = 10, callback: AlbumDataSource.LoadRecordsCallback) {
        albumRepository.getCookingRecords(offset, limit, callback)
    }

    fun setRecords(records: List<CookingRecord>){
        this.records.value = records
    }

}