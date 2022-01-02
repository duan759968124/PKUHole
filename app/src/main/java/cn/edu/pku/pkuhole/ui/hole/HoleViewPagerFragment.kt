package cn.edu.pku.pkuhole.ui.hole

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import cn.edu.pku.pkuhole.R
import cn.edu.pku.pkuhole.adapters.HOLE_LIST_INDEX
import cn.edu.pku.pkuhole.adapters.HOLE_MY_ATTENTION_INDEX
import cn.edu.pku.pkuhole.adapters.HolePaperAdapter
import cn.edu.pku.pkuhole.base.BaseFragment
import cn.edu.pku.pkuhole.databinding.FragmentHoleViewPagerBinding
import cn.edu.pku.pkuhole.viewmodels.hole.HoleItemDetailViewModel
import cn.edu.pku.pkuhole.viewmodels.hole.HoleViewPagerViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/21
 * @Desc:
 * @Version:        1.0
 */
@AndroidEntryPoint
class HoleViewPagerFragment : BaseFragment() {

    private lateinit var binding: FragmentHoleViewPagerBinding
    private val viewModel : HoleViewPagerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHoleViewPagerBinding.inflate(inflater, container, false)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        viewPager.adapter = HolePaperAdapter(this)
        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            tab.text = getTabList(position)
        }.attach()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun initData() {

    }

    private fun getTabList(position: Int): String? {
        return when (position) {
            HOLE_LIST_INDEX -> getString(R.string.hole_list)
            HOLE_MY_ATTENTION_INDEX -> getString(R.string.my_attention)
            else -> null
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_hole, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController()
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
//        return when(item.itemId){
//            R.id.app_bar_search ->{
//                Toast.makeText(activity, "search hole", Toast.LENGTH_LONG).show()
//                false
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
    }

}