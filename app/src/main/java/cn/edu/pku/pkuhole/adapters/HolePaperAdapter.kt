package cn.edu.pku.pkuhole.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.edu.pku.pkuhole.ui.settings.SettingsFragment

/**
 *
 * @Author:         HuidongQin
 * @e-mail:         hdqin@pku.edu.cn
 * @Time:           2021/12/21
 * @Desc:
 * @Version:        1.0
 */
const val HOLE_ALL_LIST_INDEX = 0
const val HOLE_MY_ATTENTION_INDEX = 1

class HolePaperAdapter(fragment:Fragment): FragmentStateAdapter(fragment) {
    private val tabFragmentCreators: Map<Int, ()-> Fragment> = mapOf(
        HOLE_ALL_LIST_INDEX to { SettingsFragment() },
        HOLE_MY_ATTENTION_INDEX to { SettingsFragment() }
    )

    override fun getItemCount(): Int  = tabFragmentCreators.size

    override fun createFragment(position: Int): Fragment {
        return
    }

}