package work.kcs_labo.oisiikenkotask.util

import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.list.RecyclerRecordAdapter

//拡張関数でImageViewを拡張
//BindingAdapterアノテーションでxmlに要素を追加できる
@BindingAdapter("android:image_url")
fun ImageView.setImageUrl(url: String) {
    Glide.with(context).load(url).into(this)
    this.scaleX = 1.0f
    this.scaleY = 1.0f
}

//RecyclerViewにrecordsをバインド
@BindingAdapter("android:record_list")
fun RecyclerView.setCookingRecords(records: List<CookingRecord>?){
    when {
        records == null -> return
        adapter == null -> {
            this.adapter = RecyclerRecordAdapter(records)
        }
        adapter != null -> {
            this.adapter = (this.adapter as RecyclerRecordAdapter).also {
                it.records = records
            }
        }
    }
}

@BindingAdapter("android:ripple")
fun ViewGroup.setRippleEffect(recipeType: String){
    val rippleDrawable = RippleDrawableFactory.create(context, recipeType)
    this.background = rippleDrawable
}