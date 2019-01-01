package work.kcs_labo.oisiikenkotask.util

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import work.kcs_labo.oisiikenkotask.data.source.AlbumRepository
import work.kcs_labo.oisiikenkotask.data.source.remote.OkHttp3AlbumDataSource
import work.kcs_labo.oisiikenkotask.main.MainViewModel

class ViewModelFactory private constructor(
    private val application: Application,
    private val albumRepository: AlbumRepository
) : ViewModelProvider.NewInstanceFactory(){

    /**
     * AlbumRepositoryを注入
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(application, albumRepository)
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        } as T

    /**
     * 簡単のためInjectionクラスは省略
     */
    companion object {
        var INSTANCE: ViewModelFactory? = null
        //RemoteDataSourceの注入
        fun getInstance(application: Application) =
                INSTANCE ?: ViewModelFactory(application, AlbumRepository(OkHttp3AlbumDataSource()))
                    .also { INSTANCE = it }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}