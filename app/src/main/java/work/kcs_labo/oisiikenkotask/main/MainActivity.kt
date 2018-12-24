package work.kcs_labo.oisiikenkotask.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.main_act.*
import work.kcs_labo.oisiikenkotask.R
import work.kcs_labo.oisiikenkotask.util.ViewModelFactory
import work.kcs_labo.oisiikenkotask.util.obtainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_act)

        mainViewModel = obtainViewModel()
        setSupportActionBar(toolbar)

        if (savedInstanceState == null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.contentView, MainFragment.newInstance()).commit()
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
                mainViewModel.setSearchWord(null)
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
                mainViewModel.setSearchWord(searchWord)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        ViewModelFactory.destroyInstance()
    }

    fun obtainViewModel() = obtainViewModel(MainViewModel::class.java)
}
