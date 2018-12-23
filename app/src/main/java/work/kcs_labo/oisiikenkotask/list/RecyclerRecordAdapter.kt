package work.kcs_labo.oisiikenkotask.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.databinding.RecordItemBinding

class RecyclerRecordAdapter(var records: List<CookingRecord>) : RecyclerView.Adapter<RecyclerRecordAdapter.BindingHolder>() {
    private var listener: OnItemClickListener? = null

    // Viewの生成 (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecordItemBinding.inflate(inflater, parent,false)
        return BindingHolder(binding)
    }

    override fun getItemCount(): Int {
        return records.count()
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val record = records[position]
        holder.binding.record = record
        holder.binding.parentLayout.setOnClickListener{

            listener?.onItemClick(record)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(record: CookingRecord)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    class BindingHolder(var binding: RecordItemBinding): RecyclerView.ViewHolder(binding.root)
}