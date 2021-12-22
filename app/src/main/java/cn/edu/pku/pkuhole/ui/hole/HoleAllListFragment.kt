package cn.edu.pku.pkuhole.ui.hole

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.edu.pku.pkuhole.databinding.FragmentHoleAllListBinding
import cn.edu.pku.pkuhole.viewmodels.hole.HoleAllListViewModel

class HoleAllListFragment : Fragment() {

    private lateinit var viewModel: HoleAllListViewModel
    private lateinit var binding: FragmentHoleAllListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHoleAllListBinding.inflate(inflater, container, false)
        context ?: return binding.root


        viewModel = ViewModelProvider(this).get(HoleAllListViewModel::class.java)
        return binding.root
    }


}