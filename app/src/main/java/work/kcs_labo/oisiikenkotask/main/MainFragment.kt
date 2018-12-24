package work.kcs_labo.oisiikenkotask.main

import android.arch.lifecycle.Observer
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
import kotlinx.android.synthetic.main.main_act.view.*
import work.kcs_labo.oisiikenkotask.R
import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.data.UserRecords
import work.kcs_labo.oisiikenkotask.data.source.AlbumDataSource
import work.kcs_labo.oisiikenkotask.databinding.MainFragBinding
import work.kcs_labo.oisiikenkotask.list.RecyclerRecordAdapter
import work.kcs_labo.oisiikenkotask.util.RecipeTypeEnum

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

        drawer.nav_view.setNavigationItemSelectedListener{ menuItem ->
            when (menuItem.itemId){
                R.id.all_navigation_menu_item ->{ binding.viewmodel?.setRecipeType(null) }
                R.id.main_dish_navigation_item ->{ binding.viewmodel?.setRecipeType(RecipeTypeEnum.MAIN_DISH) }
                R.id.side_dish_navigation_item ->{ binding.viewmodel?.setRecipeType(RecipeTypeEnum.SIDE_DISH) }
                R.id.soup_navigation_item ->{ binding.viewmodel?.setRecipeType(RecipeTypeEnum.SOUP)}
                else ->{ throw IllegalArgumentException() }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    private fun setupToolbar() {
        val activity = this.activity as MainActivity
        val toolbar = activity.toolbar

        binding.setLifecycleOwner(this)

        toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.sync_toolbar_menu_item -> {
                    binding.viewmodel?.startSync(callback =  object : AlbumDataSource.LoadRecordsCallback {
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
        binding.viewmodel?.scrollPosition?.observe(this, Observer {
            //android:selectedItemPositionが取得できないためやむなく。
            val positionIndex = layoutManager.findFirstVisibleItemPosition()
            val startView = layoutManager.getChildAt(0)
            val positionOffset = if (startView == null){
                0
            } else {
                startView.top - binding.recycler.paddingTop
            }
            layoutManager.scrollToPositionWithOffset(positionIndex, positionOffset)
        })

        binding.recycler.adapter = RecyclerRecordAdapter(listOf()).apply {
            setOnItemClickListener(object : RecyclerRecordAdapter.OnItemClickListener {
                override fun onItemClick(record: CookingRecord) {
                    //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
        }

        binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //更新時のスクロール位置復元
                super.onScrolled(recyclerView, dx, dy)

                val totalCount = binding.recycler.adapter?.itemCount
                val childCount = binding.recycler.childCount
                val _layoutManager = binding.recycler.layoutManager

                val firstItemPosition = when (_layoutManager) {
                    is GridLayoutManager -> {
                        _layoutManager.findFirstVisibleItemPosition()
                    }
                    is LinearLayoutManager -> {
                        _layoutManager.findFirstVisibleItemPosition()
                    }
                    else -> throw NotImplementedError()
                }

                if (totalCount == childCount + firstItemPosition){
                    binding.viewmodel?.addSync(callback = object : AlbumDataSource.LoadAdditionalRecordCallback{
                        override fun onAdditionalRecordLoaded(userRecords: UserRecords) {
                            binding.viewmodel?.addRecords(userRecords.cookingRecords)
                            binding.viewmodel?.setScrollPosition(firstItemPosition)
                        }

                        override fun onDataNotAvailable() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }
                    })
                }
            }
        })
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}