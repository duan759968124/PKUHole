package cn.edu.pku.treehole.utilities

import android.content.Context
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.util.AttributeSet
import android.view.View
import com.google.android.material.appbar.AppBarLayout

/**
 * @Author: HuidongQin
 * @e-mail: hdqin@pku.edu.cn
 * @Time: 2022/1/2
 * @Desc:  暂时没有用到
 * @Version: 1.0
 */
class ScrollingFabBehavior(paramContext: Context, paramAttributeSet: AttributeSet?) :
    CoordinatorLayout.Behavior<FloatingActionButton>(paramContext, paramAttributeSet) {
    private val toolbarHeight: Int
    private fun getToolbarHeight(paramContext: Context): Int {
        val typedArray = paramContext.theme.obtainStyledAttributes(intArrayOf(2130772069))
        val i = typedArray.getDimension(0, 0.0f).toInt()
        typedArray.recycle()
        return i
    }

    override fun layoutDependsOn(
        paramCoordinatorLayout: CoordinatorLayout,
        paramFloatingActionButton: FloatingActionButton,
        paramView: View
    ): Boolean {
        return paramView is AppBarLayout
    }

    override fun onDependentViewChanged(
        paramCoordinatorLayout: CoordinatorLayout,
        paramFloatingActionButton: FloatingActionButton,
        paramView: View
    ): Boolean {
        if (paramView is AppBarLayout) {
            val i =
                (paramFloatingActionButton.layoutParams as CoordinatorLayout.LayoutParams).bottomMargin
            val j = paramFloatingActionButton.height
            val f = paramView.getY() / toolbarHeight
            paramFloatingActionButton.translationY = -(j + i) * f
        }
        return true
    }

    init {
        toolbarHeight = getToolbarHeight(paramContext)
    }
}