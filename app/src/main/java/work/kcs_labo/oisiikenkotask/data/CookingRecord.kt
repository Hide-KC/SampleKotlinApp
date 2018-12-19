package work.kcs_labo.oisiikenkotask.data

data class CookingRecord(
    val imageUrl: String,
    val comment: String,
    val recipeType: RecipeTypeEnum,
    val recordedTime: String
)