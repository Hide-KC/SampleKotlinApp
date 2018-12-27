package work.kcs_labo.oisiikenkotask.data

import org.json.JSONObject

data class CookingRecord(val recordJSON: JSONObject) {
    val imageUrl: String = recordJSON.getString("image_url")
    val comment: String = recordJSON.getString("comment")
    val recipeType: String = recordJSON.getString("recipe_type")
    val recordedTime: String = recordJSON.getString("recorded_at")
}