package cn.edu.pku.pkuhole.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.edu.pku.pkuhole.ui.hole.AttentionFragment
import cn.edu.pku.pkuhole.ui.hole.HoleListFragment
import cn.edu.pku.pkuhole.ui.settings.SettingsFragment
import java.lang.IndexOutOfBoundsException

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/21
 * @Desc:
 * @Version:        1.0
 */
const val HOLE_LIST_INDEX = 0
const val HOLE_MY_ATTENTION_INDEX = 1

class HolePaperAdapter(fragment:Fragment): FragmentStateAdapter(fragment) {
    private val tabFragmentCreators: Map<Int, ()-> Fragment> = mapOf(
        HOLE_LIST_INDEX to { HoleListFragment() },
        HOLE_MY_ATTENTION_INDEX to { AttentionFragment() }
    )

    override fun getItemCount(): Int  = tabFragmentCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentCreators[position]?.invoke()?:throw IndexOutOfBoundsException()
    }

}