package com.worksy.hr.art.views.fragments.companyselection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentCompanySelectionBinding
import com.worksy.hr.art.models.companies.ListCompaniesResponse
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.utils.SnackbarUtils
import com.worksy.hr.art.utils.UIResult
import com.worksy.hr.art.viewmodels.LoginViewModel
import com.worksy.hr.art.views.activity.Homeactivity
import com.worksy.hr.art.views.activity.SplashHomeActivity.Companion.hideLoading
import com.worksy.hr.art.views.activity.SplashHomeActivity.Companion.showLoading
import com.worksy.hr.art.views.adapter.company.CompanyListAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CompanySelectionFragment : Fragment(R.layout.fragment_company_selection) {

    private var _binding: FragmentCompanySelectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var context: Context
    private var rvAdapter: CompanyListAdapter?=null
    private val viewModel: LoginViewModel by activityViewModels()
    @Inject
    lateinit var appPrefManager: SharedPrefManager
    private var companyListSize: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompanySelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = requireContext()
        companyApi()
        binding.apply {
            backArrow.setOnClickListener {
                findNavController().popBackStack()
            }
            continueBtn.setOnClickListener {
                if (companyListSize == 1) {
                    // If company list size is 1, navigate directly to Homeactivity
                    val intent = Intent(requireActivity(), Homeactivity::class.java)
                    startActivity(intent)
                } else if (rvAdapter != null) {
                    if (rvAdapter?.selectedPosition == -1) {
                        SnackbarUtils.showLongSnackbar(requireView(), "Please select a company")
                    } else {
                        val intent = Intent(requireActivity(), Homeactivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    SnackbarUtils.showLongSnackbar(requireView(), "Companies not found")
                }
            }
        }
    }

    private fun companyApi() {
        viewModel.listCompany()
        viewModel.listCompanyResponse.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is UIResult.Success -> {
                        hideLoading()
                        it.data?.let {
                            var companyList: List<ListCompaniesResponse.CompanyData>
                            companyList = it.data
                            companyListSize = companyList.size
                            if (!companyList.isNullOrEmpty()) {
                                if (companyListSize == 1) {
                                    // Automatically select the company
                                    val selectedCompany = companyList[0]
                                    appPrefManager.setCompanyAccountId(selectedCompany.account_id)
                                    Log.d(
                                        "CompanyAccountId", "CompanyAccountId: ${selectedCompany.account_id}"
                                    )
                                } else {
                                    rvAdapter = CompanyListAdapter(
                                        context,
                                        companyList,
                                        onViewDetailClick = { position, accountId ->
                                            appPrefManager.setCompanyAccountId(accountId)
                                            Log.d(
                                                "CompanyAccountId", "CompanyAccountId: $accountId"
                                            )
                                        })
                                    binding.companyRecyclerview.layoutManager =
                                        LinearLayoutManager(context)
                                    binding.companyRecyclerview.adapter = rvAdapter
                                }
                            }
                        }
                    }

                    is UIResult.Error -> {
                        hideLoading()
                        SnackbarUtils.showLongSnackbar(
                            requireView(),
                            "Something went wrong, Please try again later",
                        )
                    }
                    is UIResult.Loading -> {
                        showLoading()
                    }
                }
            }
        }
    }
}

