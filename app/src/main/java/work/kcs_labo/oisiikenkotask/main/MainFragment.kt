package work.kcs_labo.oisiikenkotask.main

import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_act.*
import work.kcs_labo.oisiikenkotask.R
import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.data.UserRecords
import work.kcs_labo.oisiikenkotask.data.source.AlbumDataSource
import work.kcs_labo.oisiikenkotask.databinding.MainFragBinding
import work.kcs_labo.oisiikenkotask.list.RecyclerRecordAdapter

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

        setupRecyclerView()

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

        binding.setLifecycleOwner(this)

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
                R.id.search_toolbar_menu_item -> {
                    TODO("コメント検索の実装")
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun setupRecyclerView(){
        val layoutManager = when (resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                GridLayoutManager(context, 2)
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                LinearLayoutManager(context)
            } else -> throw NotImplementedError()
        }

        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = RecyclerRecordAdapter(listOf()).apply {
            setOnItemClickListener(object : RecyclerRecordAdapter.OnItemClickListener {
                override fun onItemClick(record: CookingRecord) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        }

        binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {

        })
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}