package work.kcs_labo.oisiikenkotask.main

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.data.source.LIMIT
import work.kcs_labo.oisiikenkotask.data.source.AlbumDataSource
import work.kcs_labo.oisiikenkotask.data.source.AlbumRepository

class MainViewModel(
    application: Application,
    private val albumRepository: AlbumRepository
) : AndroidViewModel(application) {

    val records = MutableLiveData<List<CookingRecord>>()
    val scrollPosition = MutableLiveData<Int>()

    fun startSync(limit: Int = LIMIT, callback: AlbumDataSource.LoadRecordsCallback) {
        albumRepository.getCookingRecords(0, limit, callback)
    }

    fun setRecords(records: List<CookingRecord>){
        this.records.value = records
    }

    fun addRecords(records: List<CookingRecord>){
        val list = this.records.value
        if (list != null){
            this.records.value = list.plus(records)
        }
    }

    fun updateSync(offset: Int = 0, limit: Int = 10, callback: AlbumDataSource.LoadAdditionalRecordCallback){
        albumRepository.getAdditionalRecords(offset, limit, callback)
    }

    fun setScrollPosition(position: Int){
        scrollPosition.value = position
    }
}