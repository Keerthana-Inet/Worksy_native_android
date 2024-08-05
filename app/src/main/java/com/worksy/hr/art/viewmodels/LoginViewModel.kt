package com.worksy.hr.art.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.worksy.hr.art.listOfCompaniesModel
import com.worksy.hr.art.loginUserCheckModel
import com.worksy.hr.art.logoutModel
import com.worksy.hr.art.models.claimRequestResponse.AddClaimSettingsResponse
import com.worksy.hr.art.models.claimRequestResponse.ClaimRequestListResponse
import com.worksy.hr.art.models.claimRequestResponse.GetClaimFormResponse
import com.worksy.hr.art.models.companies.ListCompaniesResponse
import com.worksy.hr.art.models.login.LoginEmailRequest
import com.worksy.hr.art.models.login.LoginRequest
import com.worksy.hr.art.repository.MyRepo
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.utils.SingleLiveEvent
import com.worksy.hr.art.utils.UIResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val myRepo: MyRepo,
    private val appPref: SharedPrefManager,

): ViewModel(){
    companion object{
        const val errorMessage="Something went wrong.Please try again later"
        const val invalidInput="Invalid inputs"
    }
    lateinit var companyData: List<ListCompaniesResponse.CompanyData>
    lateinit var claimRequestData: ClaimRequestListResponse.ClaimRequestData
    lateinit var getClaimData: GetClaimFormResponse.ClaimFormData
    private val _userLogin = SingleLiveEvent<loginUserCheckModel?>()

    lateinit var addClaimData: AddClaimSettingsResponse.Data
    var claimGroupStringList = mutableListOf<String>()
    var editRequestGroupList = mutableListOf<String>()
    var jsonString:Any?=null
    var ApprovalJsonString:Any?=null
    var claimGroupId :String=""
    var claimGroupName :String=""
    var claimItemName :String=""
    var claimItemId :String=""
    var isListView =false
    var addAnotherBtnTrigger=false
    var claimAmount :String=""
    var claimTax :String=""
    var transactionDate:String=""
    var claimReasons:String=""
    var claimRemarks:String=""
    var fileSize= mutableListOf<String>()
    var fileImageUri= mutableListOf<String>()
    var filePath=mutableListOf<String>()
    var filePathString:String=""
    var fileSizeString:String=""

    val userLogin: SingleLiveEvent<loginUserCheckModel?> get() = _userLogin
    private val _userEmailLogin = SingleLiveEvent<loginUserCheckModel?>()
    val userEmailLogin: SingleLiveEvent<loginUserCheckModel?> get() = _userEmailLogin
    private val _listCompanyResponse = SingleLiveEvent<listOfCompaniesModel>()
    private val _logout = SingleLiveEvent<logoutModel>()

    fun userLogin(phone_number: String, password: String) {
        _userLogin.postValue(UIResult.Loading())
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Starting user login process")
                val response = withContext(Dispatchers.IO) {
                    Log.d("LoginViewModel", "Calling myRepo.checkUserLogin")
                    myRepo.checkUserLogin(LoginRequest(phone_number, password))
                }

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (response.body()?.status.equals("error")) {
                        val errorMessages = body.message ?: "Unknown error"
                        _userLogin.postValue(UIResult.Error(errorMessages))
                        Log.d("errorMessages", "errorMessages: $errorMessages")
                    } else {
                        Log.e("LoginResponse", body.toString())
                        _userLogin.postValue(UIResult.Success(body))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (errorBody.isNullOrEmpty()) {
                        "Unknown error"
                    } else {
                        parseErrorMessage(errorBody)
                    }
                    _userLogin.postValue(UIResult.Error(errorMessage))
                    Log.d("errorMessages", "errorMessages: $errorMessage")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception during user login", e)
                _userLogin.postValue(UIResult.Error(e.message ?: errorMessage))
            }
        }
    }
    private fun parseErrorMessage(errorBody: String): String {
        return try {
            val jsonObject = JSONObject(errorBody)
            jsonObject.getString("message") // Adjust according to your API's error response structure
        } catch (e: JSONException) {
            "Something went wrong.Please try again later"
        }
    }

    fun userEmailLogin(email: String, password: String) {
        _userEmailLogin.postValue(UIResult.Loading())
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Starting user login process")
                val response = withContext(Dispatchers.IO) {
                    Log.d("LoginViewModel", "Calling myRepo.checkUserLogin")
                    myRepo.checkEmailLogin(LoginEmailRequest(email, password))
                }

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (response.body()?.status.equals("error")) {
                        val errorMessages = response.body()?.error?.get(0)?.message ?: "Unknown error"
                        _userEmailLogin.postValue(UIResult.Error(errorMessages))
                        Log.d("errorMessages", "errorMessages: $errorMessages")
                    } else {
                        Log.e("LoginResponse", body.toString())
                        _userEmailLogin.postValue(UIResult.Success(body))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (errorBody.isNullOrEmpty()) {
                        "Unknown error"
                    } else {
                        parseErrorMessage(errorBody)
                    }
                    _userEmailLogin.postValue(UIResult.Error(errorMessage))
                    Log.d("errorMessages", "errorMessages: $errorMessage")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception during user login", e)
                _userEmailLogin.postValue(UIResult.Error(e.message ?: errorMessage))
            }
        }
    }
    val listCompanyResponse: SingleLiveEvent<listOfCompaniesModel> get() = _listCompanyResponse

    fun listCompany() = viewModelScope.launch {
        try {
            _listCompanyResponse.postValue(UIResult.Loading())
            val response = myRepo.listOfCompanies()
            withContext(Dispatchers.IO) {
                if (response.body()?.data != null) {
                    if (response.body()?.status== "success") {
                        companyData = response.body()!!.data
                        _listCompanyResponse.postValue(
                            UIResult.Success(response.body())
                        )
                    } else if(response.body()?.status == "error") {
                        _listCompanyResponse.postValue(
                            UIResult.Error(response.body()?.status)
                        )
                    }
                } else {
                    _listCompanyResponse.postValue(
                        UIResult.Error(
                            errorMessage
                        )
                    )
                }
            }
        } catch (e: Exception) {

            _listCompanyResponse.postValue(
                UIResult.Error(
                    errorMessage
                )
            )
        }
    }


    val logoutResponse: SingleLiveEvent<logoutModel> get() = _logout

    fun logoutApi(deviceId: String) = viewModelScope.launch {
        try {
            _logout.postValue(UIResult.Loading())
            val response = myRepo.logout(deviceId)
            withContext(Dispatchers.IO) {

                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.status == "success") {
                            _logout.postValue(UIResult.Success(responseBody))
                        } else if (responseBody.status == "error") {
                            handleErrorResponse(responseBody.message)
                        }
                    } else {
                        handleErrorResponse("Unauthenticated")
                    }
            }
        } catch (e: Exception) {
            handleErrorResponse("An error occurred: ${e.message}")
        }
    }


    private fun handleErrorResponse(errorMessage: String) {
        if (errorMessage == "Unauthenticated") {
            _logout.postValue(UIResult.Error(errorMessage))
        } else {
            _logout.postValue(UIResult.Error(errorMessage))
        }
    }
}