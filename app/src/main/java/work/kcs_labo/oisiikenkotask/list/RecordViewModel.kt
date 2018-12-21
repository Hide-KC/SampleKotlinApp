package work.kcs_labo.oisiikenkotask.list

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import work.kcs_labo.oisiikenkotask.data.source.AlbumRepository

class RecordViewModel(application: Application, private val albumRepository: AlbumRepository) : AndroidViewModel(application) {
    val imageUrl = MutableLiveData<String>()
    val comment = MutableLiveData<String>()
    val recipeType = MutableLiveData<String>()
    val recordedTime = MutableLiveData<String>()

    fun setImageUrl(url: String) {
        imageUrl.value = url
    }

    fun setComment(comment: String) {
        this.comment.value = comment
    }

    fun setRecipeType(recipeType: String) {
        this.recipeType.value = recipeType
    }

    fun setRecordedTime(recordedTime: String) {
        this.recordedTime.value = recordedTime
    }

}