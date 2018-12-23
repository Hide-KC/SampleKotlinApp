package work.kcs_labo.oisiikenkotask.util

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import work.kcs_labo.oisiikenkotask.R

const val MAIN_DISH = "main_dish"
const val SIDE_DISH = "side_dish"
const val SOUP = "soup"
object RippleDrawableFactory {
    fun create(recipeType: String): RippleDrawable {
        val colorStateList = setColorStateList(recipeType)
        return RippleDrawable(colorStateList, ColorDrawable(getResolvedColor(recipeType)), ColorDrawable(Color.LTGRAY))
    }

    private fun getResolvedColor(recipeType: String): Int{
        return when(recipeType){
            MAIN_DISH -> Color.parseColor("#eddf9a")
            SIDE_DISH -> Color.parseColor("#70bfb0")
            SOUP -> Color.parseColor("#796ebf")
            else -> throw IllegalArgumentException("unknown recipeType")
        }
    }

    private fun setColorStateList(recipeType: String): ColorStateList{
        val resolvedColor = getResolvedColor(recipeType)
        return when(recipeType) {
            MAIN_DISH -> {
                ColorStateList(
                    //States
                    arrayOf(
                        intArrayOf(-android.R.attr.state_pressed),
                        intArrayOf(android.R.attr.state_pressed)
                    ),
                    //Colors
                    intArrayOf(resolvedColor, Color.DKGRAY)
                )
            }
            SIDE_DISH -> {
                ColorStateList(
                    //States
                    arrayOf(
                        intArrayOf(-android.R.attr.state_pressed),
                        intArrayOf(android.R.attr.state_pressed)
                    ),
                    //Colors
                    intArrayOf(resolvedColor, Color.DKGRAY)
                )
            }
            SOUP -> {
                ColorStateList(
                    //States
                    arrayOf(
                        intArrayOf(-android.R.attr.state_pressed),
                        intArrayOf(android.R.attr.state_pressed)
                    ),
                    //Colors
                    intArrayOf(resolvedColor, Color.DKGRAY)
                )
            }
            else -> throw IllegalArgumentException("unknown recipeType")
        }
    }
}