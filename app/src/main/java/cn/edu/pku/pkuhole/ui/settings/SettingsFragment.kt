package cn.edu.pku.pkuhole.ui.settings

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.edu.pku.pkuhole.R
import cn.edu.pku.pkuhole.databinding.FragmentSettingsBinding
import cn.edu.pku.pkuhole.viewmodels.SettingsViewModel

class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =  ViewModelProvider(this).get(SettingsViewModel::class.java)
        val binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

}