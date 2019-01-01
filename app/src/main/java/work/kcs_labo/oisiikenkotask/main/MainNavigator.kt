package work.kcs_labo.oisiikenkotask.main

import work.kcs_labo.oisiikenkotask.data.CookingRecord

/**
 * MainActivityで処理するアクションのインターフェース
 */
interface MainNavigator {
    fun onStartNewActivity()

    fun onOpenImage(record: CookingRecord)

    fun onOpenRecipe(record: CookingRecord)
}