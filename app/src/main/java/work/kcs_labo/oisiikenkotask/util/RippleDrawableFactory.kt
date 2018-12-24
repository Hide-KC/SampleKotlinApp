package work.kcs_labo.oisiikenkotask.util

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.support.v4.content.ContextCompat
import work.kcs_labo.oisiikenkotask.R

object RippleDrawableFactory {
    fun create(context: Context, recipeType: String): RippleDrawable {
        val colorStateList = setColorStateList(context, recipeType)
        return RippleDrawable(colorStateList, ColorDrawable(getResolvedColor(context, recipeType)), ColorDrawable(Color.LTGRAY))
    }

    private fun getResolvedColor(context:Context, recipeType: String): Int{
        return when(recipeType){
            RecipeTypeEnum.MAIN_DISH.recipeType -> ContextCompat.getColor(context, R.color.main_dish_color)
            RecipeTypeEnum.SIDE_DISH.recipeType -> ContextCompat.getColor(context, R.color.side_dish_color)
            RecipeTypeEnum.SOUP.recipeType -> ContextCompat.getColor(context, R.color.soup_color)
            else -> throw IllegalArgumentException("unknown recipeType")
        }
    }

    private fun setColorStateList(context:Context, recipeType: String): ColorStateList{
        val resolvedColor = getResolvedColor(context, recipeType)
        return when(recipeType) {
            RecipeTypeEnum.MAIN_DISH.recipeType -> {
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
            RecipeTypeEnum.SIDE_DISH.recipeType -> {
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
            RecipeTypeEnum.SOUP.recipeType -> {
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