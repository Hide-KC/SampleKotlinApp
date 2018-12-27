package work.kcs_labo.oisiikenkotask.main

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.main_act.*
import work.kcs_labo.oisiikenkotask.R
import work.kcs_labo.oisiikenkotask.databinding.MainActBinding
import work.kcs_labo.oisiikenkotask.databinding.NavigationHeaderBinding
import work.kcs_labo.oisiikenkotask.util.RecipeTypeEnum
import work.kcs_labo.oisiikenkotask.util.ViewModelFactory
import work.kcs_labo.oisiikenkotask.util.obtainViewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: MainActBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_act)

        //HeaderView inflate
        val _bind =
            NavigationHeaderBinding.inflate(layoutInflater, binding.navView, false).also {
                it.viewmodel = obtainViewModel()
                it.setLifecycleOwner(this)
            }
        binding.navView.addHeaderView(_bind.root)

        //VectorリソースからDrawable生成
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        binding.viewmodel = obtainViewModel()
        binding.setLifecycleOwner(this)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.contentView, MainFragment.newInstance()).commit()
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

        val searchView = item.actionView as android.support.v7.widget.SearchView
        searchView.queryHint = "コメント検索"
        searchView.maxWidth = Int.MAX_VALUE
        searchView.setOnQueryTextListener(object : android.support.v7.widget.SearchView.OnQueryTextListener{
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

    override fun onStop() {
        super.onStop()
        binding.viewmodel?.cancelRequest()
    }

    override fun onDestroy() {
        super.onDestroy()
        ViewModelFactory.destroyInstance()
    }

    fun obtainViewModel() = obtainViewModel(MainViewModel::class.java)
}
