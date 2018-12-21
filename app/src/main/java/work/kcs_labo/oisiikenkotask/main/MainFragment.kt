package work.kcs_labo.oisiikenkotask.main

import android.arch.lifecycle.Observer
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ListView
import kotlinx.android.synthetic.main.main_act.*
import work.kcs_labo.oisiikenkotask.R
import work.kcs_labo.oisiikenkotask.data.UserRecords
import work.kcs_labo.oisiikenkotask.data.source.AlbumDataSource
import work.kcs_labo.oisiikenkotask.databinding.MainFragBinding
import work.kcs_labo.oisiikenkotask.list.RecordAdapter

class MainFragment : Fragment() {
    private lateinit var binding: MainFragBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupToolbar()
        setupDrawer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_frag, container, false)

        binding.viewmodel = (activity as MainActivity).obtainViewModel()


        setupListView()

        return binding.root
    }

    //UIのアクションはFragmentでセット
    private fun setupDrawer(){
        val activity = this.activity as MainActivity
        val drawer = activity.drawer

        drawer.addDrawerListener(
            ActionBarDrawerToggle(
                activity,
                drawer,
                activity.toolbar,
                R.string.drawer_open,
                R.string.drawer_close)
                .apply { syncState() })

    }

    private fun setupToolbar() {
        val activity = this.activity as MainActivity
        val toolbar = activity.toolbar

        binding.viewmodel?.records?.observe(this, Observer {
            val listView = when (resources.configuration.orientation){
                Configuration.ORIENTATION_PORTRAIT -> binding.recordList as GridView
                else -> binding.recordList as ListView
            }

            if (it != null) {
                when (listView.adapter) {
                    null -> {
                        val adapter = RecordAdapter(context!!)
                        adapter.addAll(it)
                        listView.adapter = adapter

                        for(i in 0 until adapter.count){
                            Log.d(this.javaClass.simpleName, adapter.getItem(i)?.comment)
                        }
                    }
                    else -> {
                        val adapter = listView.adapter as RecordAdapter
                        adapter.apply {
                            clear()
                            addAll(it)
                        }
                    }
                }
            }
        })

        toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.sync_toolbar_menu_item -> {
                    binding.viewmodel?.startSync(0,50, object : AlbumDataSource.LoadRecordsCallback {
                        override fun onRecordsLoaded(userRecords: UserRecords) {
                            binding.viewmodel?.setRecords(userRecords.cookingRecords)

                            for (item in userRecords.cookingRecords) {
                                Log.d(this@MainFragment.javaClass.simpleName, item.comment)
                            }
                        }

                        override fun onDataNotAvailable() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
                }
                R.id.search_toolbar_menu_item -> {}
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun setupListView(){
        //TODO ListViewのラッパ作ってEmptyView実装？
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}