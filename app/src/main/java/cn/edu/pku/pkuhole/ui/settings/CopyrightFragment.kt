package cn.edu.pku.pkuhole.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.edu.pku.pkuhole.R

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class CopyrightFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_copyright, container, false)
    }
}