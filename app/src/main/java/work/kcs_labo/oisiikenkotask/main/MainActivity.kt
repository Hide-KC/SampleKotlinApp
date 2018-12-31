package work.kcs_labo.oisiikenkotask.main

import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.main_act.*
import work.kcs_labo.oisiikenkotask.R
import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.databinding.MainActBinding
import work.kcs_labo.oisiikenkotask.databinding.NavigationHeaderBinding
import work.kcs_labo.oisiikenkotask.dialog.ImageDialogFragment
import work.kcs_labo.oisiikenkotask.util.RecipeTypeEnum
import work.kcs_labo.oisiikenkotask.util.ViewModelFactory
import work.kcs_labo.oisiikenkotask.util.obtainViewModel

private const val IMAGE_DIALOG = "image_dialog"
class MainActivity : AppCompatActivity(), MainNavigator {

    lateinit var binding: MainActBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //View inflate
        binding = DataBindingUtil.setContentView<MainActBinding>(this, R.layout.main_act).also {
            it.viewmodel = obtainViewModel()
            it.setLifecycleOwner(this)
        }
        binding.viewmodel?.setNavigator(this)

        //HeaderView inflate
        val _bind =
            NavigationHeaderBinding.inflate(layoutInflater, binding.navView, false).also {
                it.viewmodel = obtainViewModel()
                it.setLifecycleOwner(this)
            }
        binding.navView.addHeaderView(_bind.root)

        //VectorリソースからDrawable生成
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        setSupportActionBar(toolbar)

        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.contentView, MainFragment.newInstance())
            }.commit()
            binding.viewmodel?.setRecipeType(RecipeTypeEnum.ALL_DISH)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_actions, menu)

        val item = toolbar.menu.findItem(R.id.search_toolbar_menu_item)
        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                binding.viewmodel?.setSearchWord(null)
                return true
            }
        })

        val searchView = item.actionView as SearchView
        searchView.queryHint = "コメント検索"
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(searchWord: String?): Boolean {
                //onQueryTextSubmitが2回呼ばれる現象の回避
                searchView.clearFocus()
                binding.viewmodel?.setSearchWord(searchWord)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onPause() {
        super.onPause()
        binding.viewmodel?.cancelRequest()
        val dialog = supportFragmentManager.findFragmentByTag(IMAGE_DIALOG)
        if (dialog != null){
            (dialog as DialogFragment).dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ViewModelFactory.destroyInstance()
    }

    override fun onStartNewActivity() {
        //今回は不要
    }

    override fun onOpenImage(record: CookingRecord) {
        //ダイアログの表示、ImageViewの差し替え
        when (resources.configuration.orientation){
            Configuration.ORIENTATION_PORTRAIT -> {
                try {
                    val dialog = ImageDialogFragment.newInstance(record)
                    dialog.show(supportFragmentManager, IMAGE_DIALOG)
                } catch (e: IllegalStateException){
                    //一度Dialog表示後、画面回転させると発報……？
                    e.printStackTrace()
                }
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                binding.viewmodel?.displayRecord(record)
            }
            else -> { IllegalStateException() }
        }
    }

    fun obtainViewModel() = obtainViewModel(MainViewModel::class.java)
}
