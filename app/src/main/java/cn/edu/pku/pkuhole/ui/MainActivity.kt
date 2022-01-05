package cn.edu.pku.pkuhole.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.edu.pku.pkuhole.R
import cn.edu.pku.pkuhole.databinding.ActivityMainBinding
import cn.edu.pku.pkuhole.databinding.NavHeaderMainBinding
import cn.edu.pku.pkuhole.viewmodels.UserViewModel
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // 管理应用显示区域左上角导航按钮行为，单个顶层和多个顶层，传入导航图
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHeaderBinding: NavHeaderMainBinding

//    private lateinit var viewModel: MainViewModel
    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        navHeaderBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))

//        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

//        navHeaderBinding.userViewModel = viewModel

//        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
//        设置toolbar
        setSupportActionBar(binding.toolbar)
//        setSupportActionBar(binding.appBarMain.toolbar)
//        drawerLayout侧边栏组件
        val drawerLayout= binding.drawerLayout
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

//        设置每个界面的toolbar是否存在导航抽屉是否可用
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_login -> {
                    binding.toolbar.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
                else -> {
                    binding.toolbar.visibility = View.VISIBLE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
            }
        }


        viewModel.userInfo.observe(this, Observer { userInfo ->
            Timber.e("userInfo %s %s", userInfo.name, userInfo.department)
            navHeaderBinding.navHeaderUserName.text = userInfo.name
            navHeaderBinding.navHeaderUserDepartment.text = userInfo.department
//            run {
//                Timber.e("userInfo %s %s", userInfo.name, userInfo.department)
//                if (userInfo.name == "Test") {
//                    navController.navigate(R.id.nav_hole)
//                }else{
//                    Timber.e("userInfo %s %s", userInfo.name, userInfo.department)
//                }
//            }
        })

        viewModel.loginStatus.observe(this, Observer { isLogin ->
            Timber.e("isLogin %s", isLogin)
            if (!isLogin) {
                navController.navigate(R.id.nav_login)
                viewModel.onNavigateToLoginFinish()
            }

        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkLoginStatus()
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