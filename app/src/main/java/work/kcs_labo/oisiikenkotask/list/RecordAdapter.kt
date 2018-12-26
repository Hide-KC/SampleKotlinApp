package work.kcs_labo.oisiikenkotask.list

import android.app.Activity
import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import work.kcs_labo.oisiikenkotask.R
import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.databinding.RecordItemBinding

/**
 * Linear/Grid ListView with DataBinding.
 * Replace ViewHolder Pattern
 */
class RecordAdapter(context: Context) : ArrayAdapter<CookingRecord>(context, android.R.layout.simple_list_item_1) {
    private val inflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private lateinit var binding: RecordItemBinding

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        when (convertView) {
            null -> {
                binding = DataBindingUtil.inflate(inflater, R.layout.record_item, parent, false)
                binding.root.tag = binding
            }
            else -> binding = convertView.tag as RecordItemBinding
        }

        val record = this.getItem(position)
        if (record != null){
            binding.record = record
        }

        return binding.root
    }
}