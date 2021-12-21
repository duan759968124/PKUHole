package cn.edu.pku.pkuhole

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import cn.edu.pku.pkuhole.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        setContentView(binding.root)
//        设置toolbar
        setSupportActionBar(binding.appBarMain.toolbar)
//        drawerLayout侧边栏组件
        val drawerLayout: DrawerLayout = binding.drawerLayout
//        navView 侧边栏菜单组件
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment)
//      设置根fragment，区别向上还是三横
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_hole
            ), drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    override fun onSupportNavigateUp(): Boolean {
        //点击侧边栏按钮
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}