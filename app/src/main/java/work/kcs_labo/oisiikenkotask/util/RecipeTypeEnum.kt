package work.kcs_labo.oisiikenkotask.util

import work.kcs_labo.oisiikenkotask.R

enum class RecipeTypeEnum(
    val recipeType: String,
    val recipeStringId: Int,
    val symbolIconId: Int,
    val symbolColorId: Int
    ) {
    ALL_DISH(
        "all_dish",
        R.string.all_dish,
        R.drawable.ic_round_restaurant,
        R.color.all_dish_color
    ),
    MAIN_DISH(
        "main_dish",
        R.string.main_dish,
        R.drawable.ic_fish,
        R.color.main_dish_color
    ),
    SIDE_DISH(
        "side_dish",
        R.string.side_dish,
        R.drawable.ic_salad,
        R.color.side_dish_color
    ),
    SOUP(
        "soup",
        R.string.soup,
        R.drawable.ic_soup,
        R.color.soup_color
    )
}