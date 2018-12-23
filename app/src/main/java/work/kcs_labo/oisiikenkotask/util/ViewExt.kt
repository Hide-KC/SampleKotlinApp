package work.kcs_labo.oisiikenkotask.util

import android.databinding.BindingAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.ListView
import com.bumptech.glide.Glide
import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.list.RecordAdapter

//拡張関数でImageViewを拡張
//BindingAdapterアノテーションでxmlに要素を追加できる
@BindingAdapter("android:image_url")
fun ImageView.setImageUrl(url: String) {
    Glide.with(context).load(url).into(this)
}

//ListViewを拡張
@BindingAdapter("android:record_list")
fun ListView.setCookingRecords(records: List<CookingRecord>?) {
    when {
        records == null -> return
        adapter == null -> {
            adapter = RecordAdapter(context).also {
                it.addAll(records)
            }
        }
        adapter != null -> {
            (adapter as RecordAdapter).apply {
                clear()
                addAll(records)
            }
        }
    }
}

@BindingAdapter("android:record_list")
fun GridView.setCookingRecords(records: List<CookingRecord>?) {
    when {
        records == null -> return
        adapter == null -> {
            adapter = RecordAdapter(context).also {
                it.addAll(records)
            }
        }
        adapter != null -> {
            (adapter as RecordAdapter).apply {
                clear()
                addAll(records)
            }
        }
    }
}
