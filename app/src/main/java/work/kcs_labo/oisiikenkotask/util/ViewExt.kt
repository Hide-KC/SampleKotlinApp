package work.kcs_labo.oisiikenkotask.util

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.databinding.BindingAdapter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import work.kcs_labo.oisiikenkotask.R
import work.kcs_labo.oisiikenkotask.list.RecordAdapter
import work.kcs_labo.oisiikenkotask.list.RecordModel
import kotlin.math.roundToInt

//拡張関数でImageViewを拡張
//BindingAdapterアノテーションでxmlに要素を追加できる
@BindingAdapter("bind:image_as_rect")
fun ImageView.setImageAsRect(url: String?) {
    val requestOptions = RequestOptions()
        .transforms(
            RoundedCorners(context.resources.getDimension(R.dimen.image_corner_radius).roundToInt()),
            CenterCrop()
        )

    Glide.with(context)
        .load(url)
        .apply(requestOptions)
        .into(this)
    this.scaleX = 1.0f
    this.scaleY = 1.0f
}

@BindingAdapter("bind:image_as_circle")
fun ImageView.setImageAsCircle(url: String?) {
    val requestOptions = RequestOptions()
        .transforms(
            CircleCrop(),
            CenterCrop()
        )

    Glide.with(context)
        .load(url)
        .apply(requestOptions)
        .into(this)
    this.scaleX = 1.0f
    this.scaleY = 1.0f
}

@BindingAdapter("bind:vector_src")
fun ImageView.setVectorSrc(resId: Int) {
    this.setImageResource(resId)
}

@BindingAdapter("bind:recordModels")
fun RecyclerView.setViewModels(recordModels: List<RecordModel>?) {
    if (recordModels != null) {
        val adapter = this.adapter as RecordAdapter
        val diff = DiffUtil.calculateDiff(RecordAdapter.Callback(adapter.recordModels, recordModels), true)
        adapter.recordModels.let {
            it.clear()
            it.addAll(recordModels)
        }
        diff.dispatchUpdatesTo(adapter)
    }
}

@BindingAdapter("bind:ripple")
fun ViewGroup.setRippleEffect(recipeType: String) {
    val rippleDrawable = RippleDrawableSelector.select(context, recipeType)
    this.background = rippleDrawable
}

@BindingAdapter("bind:animate_background_color")
fun ViewGroup.setBackgroundColor(colorTo: Int) {
    val backgroundDrawable = background
    val colorFrom = if (backgroundDrawable != null) {
        (background as ColorDrawable).color
    } else {
        ContextCompat.getColor(context, R.color.all_dish_color)
    }

    val colorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo).also {
        it.setTarget(this)
        it.duration = 500
        it.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Int
            setBackgroundColor(
                Color.argb(
                    Color.alpha(animatedValue),
                    Color.red(animatedValue),
                    Color.green(animatedValue),
                    Color.blue(animatedValue)
                )
            )
        }
    }
    colorAnimator.start()
}