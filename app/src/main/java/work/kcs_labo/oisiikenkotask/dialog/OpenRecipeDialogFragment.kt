package work.kcs_labo.oisiikenkotask.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import work.kcs_labo.oisiikenkotask.R
import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.main.MainActivity

class OpenRecipeDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.recipe_dialog_frag, null)

        val mainViewModel = (activity as MainActivity).obtainViewModel()
        val builder = AlertDialog.Builder(activity).apply {
            setView(view)
            setPositiveButton("開く") { _, _ ->
                //MainActivity#setupDialogActionで実装
                mainViewModel.openRecipeDialogOK.call()
            }
            setNegativeButton(android.R.string.cancel) {_,_ ->
                //MainActivity#setupDialogActionで実装
                mainViewModel.openRecipeDialogCancel.call()
            }
        }

        return builder.create()
    }

    companion object {
        fun newInstance(record: CookingRecord): OpenRecipeDialogFragment {
            val dialog = OpenRecipeDialogFragment()
            val args = Bundle().apply {
                putSerializable("record", record)
            }

            dialog.arguments = args
            return dialog
        }
    }
}