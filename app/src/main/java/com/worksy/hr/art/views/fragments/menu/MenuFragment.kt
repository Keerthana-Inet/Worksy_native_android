package com.worksy.hr.art.views.fragments.menu

import ExitConfirmationDialog
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentMenuBinding
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.utils.SnackbarUtils
import com.worksy.hr.art.utils.UIResult
import com.worksy.hr.art.viewmodels.LoginViewModel
import com.worksy.hr.art.views.activity.Homeactivity
import com.worksy.hr.art.views.activity.Homeactivity.Companion.hideLoading
import com.worksy.hr.art.views.activity.Homeactivity.Companion.showLoading
import com.worksy.hr.art.views.fragments.LogOutDialogDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : Fragment(R.layout.fragment_menu) {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var appPref: SharedPrefManager
    private val viewModel: LoginViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val androidId = Settings.Secure.getString(requireActivity().contentResolver, Settings.Secure.ANDROID_ID)
        Log.d("DeviceID", "Android ID: $androidId")

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showExitConfirmationDialog()
        }

        binding.apply {
            backImg.setOnClickListener {
                showExitConfirmationDialog()
            }
            myrequestTxt.setOnClickListener {
                findNavController().navigate(R.id.navigation_request)
            }
            myapprovalTxt.setOnClickListener {
                findNavController().navigate(R.id.navigation_approval)
            }
            logout.setOnClickListener {
                logOut(androidId)
            }
        }
    }

    private fun logOut(deviceID: String) {
        LogOutDialogDialog.newInstance()
            .also {
                it.isCancelable = true
                it.show(childFragmentManager, it.tag)
                it.addSuccessListerner {
                    logoutApi(deviceID)
                }
            }
    }

    private fun showExitConfirmationDialog() {
        val dialog = ExitConfirmationDialog()
        dialog.onExitConfirmed = { requireActivity().finishAffinity() }
        dialog.show(parentFragmentManager, "ExitConfirmationDialog")
    }

    private fun logoutApi(deviceID: String) {
        viewModel.logoutApi(deviceID)
        viewModel.logoutResponse.observe(viewLifecycleOwner) {
            when (it) {
                is UIResult.Success -> {
                    hideLoading()
                    SnackbarUtils.showLongSnackbar(requireView(), it.data?.message.toString())
                    (activity as? Homeactivity)?.setBottomNavigationVisibility(View.GONE)
                    appPref.clearPreferences()
                    var bundle=Bundle()
                    bundle.putString("clear_preferences","clear_pref")
                    findNavController().navigate(R.id.navi_lets_get_started,bundle)
                }
                is UIResult.Error -> {
                    hideLoading()
                    if (it.message == "Unauthenticated") {
                        appPref.clearPreferences()
                        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.GONE)
                        findNavController().navigate(R.id.navi_lets_get_started)
                    } else {
                        SnackbarUtils.showLongSnackbar(requireView(), it.message.toString())
                    }
                }
                is UIResult.Loading -> {
                    showLoading()
                }
            }
        }
    }
}