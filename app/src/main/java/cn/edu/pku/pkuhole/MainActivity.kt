package cn.edu.pku.pkuhole

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.edu.pku.pkuhole.databinding.ActivityMainBinding
import cn.edu.pku.pkuhole.databinding.NavHeaderMainBinding
import cn.edu.pku.pkuhole.viewmodels.MainViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainActivity : AppCompatActivity() {

    // 管理应用显示区域左上角导航按钮行为，单个顶层和多个顶层，传入导航图
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHeaderBinding: NavHeaderMainBinding

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        navHeaderBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        navHeaderBinding.mainViewModel = viewModel

        setContentView(binding.root)
//        设置toolbar
        setSupportActionBar(binding.appBarMain.toolbar)
//        drawerLayout侧边栏组件
        val drawerLayout: DrawerLayout = binding.drawerLayout
//        navView 侧边栏菜单组件
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment)
//      设置根fragment，处于顶层：DrawLayout显示抽屉导航图标，没有DrawLayout，则不显示。其他目的地，显示向上按钮
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_hole
            ), drawerLayout
        )

//      向默认操作栏添加导航支持,使用setupActionBarWithNavController,并替换onSupportNavigateUp
        setupActionBarWithNavController(navController, appBarConfiguration)
//       选择侧边栏导航的时候导航到响应的fragment,并边缘滑动可以显示导航栏
        navView.setupWithNavController(navController)
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.main, menu)
//        return true
//    }

    override fun onSupportNavigateUp(): Boolean {
        //点击侧边栏按钮
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}