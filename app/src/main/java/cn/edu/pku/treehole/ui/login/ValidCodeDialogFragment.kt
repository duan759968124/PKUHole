package cn.edu.pku.treehole.ui.login

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import cn.edu.pku.treehole.databinding.DialogValidCodeBinding
import cn.edu.pku.treehole.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class ValidCodeDialogFragment : DialogFragment() {
    private lateinit var binding: DialogValidCodeBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        Timber.e("onCancel dialog")
//        viewModel.clearContent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DialogValidCodeBinding.inflate(inflater, container, false)
        context ?: return binding.root
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
//        binding.btnHolePostSend.setOnClickListener(DialogInterface.OnClickListener { dialog, id ->
//            Timber.e("click send btn %d", id)
//            dialog.dismiss()
//        })
//        binding.btnHolePostImage.setOnClickListener()
//        return inflater.inflate(R.layout.dialog_hole_post, container, false)

        return binding.root
    }

}