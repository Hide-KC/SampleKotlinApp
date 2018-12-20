package work.kcs_labo.oisiikenkotask.main

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.main_act.*
import work.kcs_labo.oisiikenkotask.R
import work.kcs_labo.oisiikenkotask.databinding.MainFragBinding
import work.kcs_labo.oisiikenkotask.obtainViewModel

class MainFragment : Fragment() {
    private lateinit var binding: MainFragBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_frag, container, false)
        setupDrawer()

        val viewModel = (activity as MainActivity).obtainViewModel()
        binding.viewmodel = viewModel

        return binding.root
    }

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

    companion object {
        fun newInstance(): MainFragment {
            val fragment = MainFragment()
            return fragment
        }
    }
}