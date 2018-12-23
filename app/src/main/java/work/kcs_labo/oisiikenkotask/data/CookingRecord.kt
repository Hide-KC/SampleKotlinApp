package work.kcs_labo.oisiikenkotask.data

import org.json.JSONObject

class CookingRecord() {
    constructor(recordJSON: JSONObject): this(){
        imageUrl = recordJSON.getString("image_url")
        comment = recordJSON.getString("comment")
        recipeType = recordJSON.getString("recipe_type")
        recordedTime = recordJSON.getString("recorded_at")
    }

    var imageUrl: String = ""
    var comment: String = ""
    var recipeType: String = ""
    var recordedTime: String = ""
}