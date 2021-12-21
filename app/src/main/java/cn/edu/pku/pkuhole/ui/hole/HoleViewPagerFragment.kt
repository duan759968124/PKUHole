package cn.edu.pku.pkuhole.ui.hole

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.edu.pku.pkuhole.R
import cn.edu.pku.pkuhole.adapters.HOLE_ALL_LIST_INDEX
import cn.edu.pku.pkuhole.adapters.HOLE_MY_ATTENTION_INDEX
import cn.edu.pku.pkuhole.adapters.HolePaperAdapter
import cn.edu.pku.pkuhole.databinding.FragmentHoleViewPagerBinding
import com.google.android.material.tabs.TabLayoutMediator

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/21
 * @Desc:
 * @Version:        1.0
 */
class HoleViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHoleViewPagerBinding.inflate(inflater, container, false)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        viewPager.adapter = HolePaperAdapter(this)
        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            tab.text = getTabList(position)
        }.attach()


        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun getTabList(position: Int): String? {
        return when (position) {
            HOLE_ALL_LIST_INDEX -> getString(R.string.hole_all_list)
            HOLE_MY_ATTENTION_INDEX -> getString(R.string.my_attention)
            else -> null
        }
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