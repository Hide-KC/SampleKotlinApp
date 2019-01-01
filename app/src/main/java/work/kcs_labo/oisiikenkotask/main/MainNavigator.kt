package work.kcs_labo.oisiikenkotask.main

import work.kcs_labo.oisiikenkotask.data.CookingRecord

interface MainNavigator {
    fun onStartNewActivity()

    fun onOpenImage(record: CookingRecord)

    fun onOpenRecipe(record: CookingRecord)
}