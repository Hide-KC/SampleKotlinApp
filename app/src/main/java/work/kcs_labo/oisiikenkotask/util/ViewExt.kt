package work.kcs_labo.oisiikenkotask.util

import android.content.res.Configuration
import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import work.kcs_labo.oisiikenkotask.R
import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.list.RecyclerRecordAdapter
import kotlin.math.roundToInt

//拡張関数でImageViewを拡張
//BindingAdapterアノテーションでxmlに要素を追加できる
@BindingAdapter("android:image_url")
fun ImageView.setImageUrl(url: String) {
    val requestOptions = when (context.resources.configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            RequestOptions()
                .transforms(
                    RoundedCorners(context.resources.getDimension(R.dimen.image_corner_radius).roundToInt()),
                    CenterCrop()
                )
        }
        Configuration.ORIENTATION_LANDSCAPE -> {
            RequestOptions()
                .transforms(
                    CircleCrop(),
                    CenterCrop()
                )
        }
        else -> throw IllegalStateException()
    }

    Glide.with(context)
        .load(url)
        .apply(requestOptions)
        .into(this)
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
    val rippleDrawable = RippleDrawableSelector.select(context, recipeType)
    this.background = rippleDrawable
}