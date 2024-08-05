    package com.worksy.hr.art.views.fragments.permissionsetup

    import android.content.Context
    import android.graphics.Color
    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.fragment.app.Fragment
    import androidx.fragment.app.FragmentManager
    import androidx.fragment.app.FragmentPagerAdapter
    import androidx.navigation.fragment.findNavController
    import androidx.viewpager.widget.ViewPager
    import com.worksy.hr.art.R
    import com.worksy.hr.art.databinding.FragmentHomePermissionSetupBinding
    import dagger.hilt.android.AndroidEntryPoint

    @AndroidEntryPoint
    class HomePermissionSetupFragment : Fragment(R.layout.fragment_home_permission_setup) {

        private var _binding: FragmentHomePermissionSetupBinding? = null
        private val binding get() = _binding!!
        private var selectedTabPosition = 0
        var setUpView: List<View>? = null

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            _binding = FragmentHomePermissionSetupBinding.inflate(inflater, container, false)
            return binding.root

        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            setUpView = listOf(
                binding.tabView1,
                binding.tabView2,
                binding.tabView3
            )

            val adapter = SetUpViewAdapter(childFragmentManager)
            binding.viewPager.adapter = adapter

            binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                }

                override fun onPageSelected(position: Int) {
                    selectedTabPosition = position
                    adapter.updateOnboardViewBackground(position,setUpView)
                    adapter.notifyDataSetChanged()
                    if(position==0){
                        binding.back.visibility=View.VISIBLE
                        binding.skip.visibility=View.VISIBLE
                        binding.btn.text=resources.getString(R.string.continue_txt)
                    }
                    else if(position==1){
                        binding.back.visibility=View.GONE
                        binding.skip.visibility=View.VISIBLE
                        binding.btn.text=resources.getString(R.string.set_it_up_txt)
                    }
                    else if(position==2){
                        binding.back.visibility=View.GONE
                        binding.skip.visibility=View.GONE
                        binding.btn.text=resources.getString(R.string.done)
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    // No need to handle this for your case
                }
            })
            binding.btn.setOnClickListener {
                if(binding.btn.text.equals("Done")){
                    findNavController().navigate(R.id.navi_company_selection)
                }else {
                    handleTabSelection(selectedTabPosition + 1)
                }
            }
        }

        private fun handleTabSelection(tabPosition: Int) {
            if (tabPosition < binding.viewPager.adapter?.count ?: 0) {
                binding.viewPager.setCurrentItem(tabPosition, true)
            }
        }


        class SetUpViewAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> PermissionSetUpFragment()
                    1 -> ClockDeviceSetupFragment()
                    2 -> SuccessfulSetupFragment()
                    else -> PermissionSetUpFragment()
                }
            }

            override fun getCount(): Int {
                return 3 // Number of fragments
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return "" // No titles for tabs
            }
            fun updateOnboardViewBackground(position: Int, setUpViews: List<View>?) {
                setUpViews?.forEachIndexed { index, view ->
                    if (index <= position) {
                        view.setBackgroundResource(R.drawable.blue_background_border)
                    } else {
                        view.setBackgroundResource(R.drawable.rounded_bottom_corners)
                    }
                }
            }
        }
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }
