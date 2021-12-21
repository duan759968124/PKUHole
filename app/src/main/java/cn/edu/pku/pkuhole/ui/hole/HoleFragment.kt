package cn.edu.pku.pkuhole.ui.hole

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.edu.pku.pkuhole.R
import cn.edu.pku.pkuhole.databinding.FragmentHoleBinding
import cn.edu.pku.pkuhole.viewmodels.HoleViewModel

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/21
 * @Desc:
 * @Version:        1.0
 */
class HoleFragment : Fragment() {
    private lateinit var viewModel: HoleViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHoleBinding.inflate(inflater, container, false)
        viewModel =  ViewModelProvider(this).get(HoleViewModel::class.java)

        val sectionsPaperAdapter = SectionsPagerApapter(this, supp)

        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_hole, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.app_bar_search ->{
                Toast.makeText(activity, "search hole", Toast.LENGTH_LONG).show()
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}