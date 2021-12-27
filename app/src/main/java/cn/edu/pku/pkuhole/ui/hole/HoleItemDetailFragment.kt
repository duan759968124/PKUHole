package cn.edu.pku.pkuhole.ui.hole

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cn.edu.pku.pkuhole.R
import cn.edu.pku.pkuhole.databinding.FragmentHoleItemDetailBinding
import cn.edu.pku.pkuhole.viewmodels.hole.HoleItemDetailViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [HoleItemDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HoleItemDetailFragment : Fragment() {

    private lateinit var binding: FragmentHoleItemDetailBinding
    private val viewModel : HoleItemDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHoleItemDetailBinding.inflate(inflater, container, false)
//        val arguments = HoleItemDetailFragmentArgs.fromBundle(requireArguments())
//        val dataSource =
//            AppDatabase.getInstance(requireNotNull(this.activity).application).holeAllListDao()
//        val viewModelFactory = HoleItemDetailViewModelFactory(arguments.pid, dataSource)
//        val holeItemDetailViewModel =
//            ViewModelProvider(this, viewModelFactory)[HoleItemDetailViewModel::class.java]
//        binding.holeItemDetailViewModel = holeItemDetailViewModel
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // 设置导航状态监听是否有必要
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_hole_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val navController = findNavController()
//        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.button_attention -> {
                Toast.makeText(activity, "hole attention", Toast.LENGTH_LONG).show()
                // 多次点击要拿到状态是否已经关注过了，
                // 如果关注过，就取消关注，否则就关注
                // 【pid是否在关注列表中？或者创建一个表格，根据字段判断？更新实体类】
                item.setIcon(R.drawable.ic_star_fill_24)
                // Todo: 返回true和返回false之前的区别
                false
            }
            R.id.button_report -> {
                Toast.makeText(activity, "hole report", Toast.LENGTH_LONG).show()
                // 发网络请求到服务器投诉举报，并处理结果。
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}