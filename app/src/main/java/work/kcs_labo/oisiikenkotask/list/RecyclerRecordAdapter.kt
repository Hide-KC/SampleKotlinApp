package work.kcs_labo.oisiikenkotask.list

import android.content.res.Configuration
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import work.kcs_labo.oisiikenkotask.R
import work.kcs_labo.oisiikenkotask.databinding.RecordItemBinding

class RecyclerRecordAdapter : RecyclerView.Adapter<RecyclerRecordAdapter.BindingHolder>() {
    var recordModels: List<RecyclerRecordModel> = listOf()

    // Viewの生成 (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecordItemBinding.inflate(inflater, parent,false)
        return BindingHolder(binding)
    }

    override fun getItemCount(): Int {
        return recordModels.count()
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: BindingHolder, position: Int) {
        val recordModel = recordModels[position]
        holder.binding.viewmodel = recordModel

        holder.binding.parentLayout.setOnClickListener{
            recordModel.itemClick(recordModel.record)
        }

        holder.binding.parentLayout.setOnLongClickListener {
            recordModel.itemLongClick(recordModel.record)
            return@setOnLongClickListener true
        }

        val animation = when (holder.binding.root.context.resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> AnimationUtils.loadAnimation(holder.binding.root.context, R.anim.scale_fade_in)
            Configuration.ORIENTATION_LANDSCAPE -> AnimationUtils.loadAnimation(holder.binding.root.context, R.anim.push_right_in)
            else -> throw IllegalStateException()
        }

        holder.binding.root.animation = animation
        holder.binding.executePendingBindings()
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    class BindingHolder(var binding: RecordItemBinding): RecyclerView.ViewHolder(binding.root)

    //差分検知
    class Callback(private val old: List<RecyclerRecordModel>,
                   private val aNew: List<RecyclerRecordModel>) : DiffUtil.Callback(){

        override fun getOldListSize(): Int = old.size

        override fun getNewListSize(): Int = aNew.size

        override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean =
            old[oldPosition].record == aNew[newPosition].record

        override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean =
            old[oldPosition] == aNew[newPosition]
    }
}