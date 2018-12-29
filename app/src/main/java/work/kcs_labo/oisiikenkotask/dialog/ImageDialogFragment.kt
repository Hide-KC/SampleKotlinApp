package work.kcs_labo.oisiikenkotask.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import work.kcs_labo.oisiikenkotask.data.CookingRecord
import work.kcs_labo.oisiikenkotask.databinding.ImageDialogFragBinding

class ImageDialogFragment: DialogFragment() {
    private lateinit var binding: ImageDialogFragBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(activity)
        val record = arguments?.getSerializable("record") as CookingRecord

        binding = ImageDialogFragBinding.inflate(inflater).also {
            it.record = record
        }

        val builder = AlertDialog.Builder(activity).apply {
            setView(binding.root)
        }

        return builder.create()
    }

    companion object {
        fun newInstance(record: CookingRecord): ImageDialogFragment{
            val args = Bundle().apply {
                putSerializable("record", record)
            }

            return ImageDialogFragment().apply {
                arguments = args
            }
        }
    }
}