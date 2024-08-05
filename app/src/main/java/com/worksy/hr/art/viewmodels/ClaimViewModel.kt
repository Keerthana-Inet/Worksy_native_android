package com.worksy.hr.art.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.worksy.hr.art.actionApproveModel
import com.worksy.hr.art.addListClaimSettings
import com.worksy.hr.art.getClaimApprovalCompleteModel
import com.worksy.hr.art.getClaimApprovalDetailModel
import com.worksy.hr.art.getClaimApprovalProgressModel
import com.worksy.hr.art.getClaimFormModel
import com.worksy.hr.art.listOfclaimRequestModel
import com.worksy.hr.art.models.claimApproval.ActionRequest
import com.worksy.hr.art.models.claimRequestResponse.AddClaimSettingsResponse
import com.worksy.hr.art.models.claimRequestResponse.AddItemRequest
import com.worksy.hr.art.models.claimRequestResponse.ClaimRequestListResponse
import com.worksy.hr.art.models.claimRequestResponse.GetClaimFormResponse
import com.worksy.hr.art.models.claimRequestResponse.SubmitClaimRequest
import com.worksy.hr.art.models.claimRequestResponse.UploadClaimFileResponse
import com.worksy.hr.art.repository.MyRepo
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.submitClaimRequestModel
import com.worksy.hr.art.utils.SingleLiveEvent
import com.worksy.hr.art.utils.UIResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ClaimViewModel @Inject constructor(
    private val myRepo: MyRepo,
    private val appPref: SharedPrefManager,
    ):ViewModel(){
    companion object{
        const val errorMessage="Something went wrong.Please try again later"
        const val invalidInput="Invalid inputs"
    }
    lateinit var claimRequestData: ClaimRequestListResponse.ClaimRequestData
    lateinit var getClaimData: GetClaimFormResponse.ClaimFormData
    var claimGroupStringList = mutableListOf<String>()
    var editRequestGroupList = mutableListOf<String>()
    lateinit var addClaimData: AddClaimSettingsResponse.Data
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
    var paxStaff:String=""
    var paxClientExisting:String=""
    var paxClientPotential:String=""
    var paxPrincipal:String=""
    val claimGroups : List<String> = listOf()
    val claimItems : List<String> = listOf()

    private val _selectedIds = MutableLiveData<Set<String>>()
    val selectedIds: MutableLiveData<Set<String>> get() = _selectedIds
    private val selectedIdSet = mutableSetOf<String>()
    fun toggleSelection(id: String) {
        if (selectedIdSet.contains(id)) {
            selectedIdSet.remove(id)
        } else {
            selectedIdSet.add(id)
        }
        _selectedIds.value = selectedIdSet
    }

    fun clearSelections() {
        selectedIdSet.clear()
        _selectedIds.value = selectedIdSet
    }

    //This is for counting the total request amount value in CreateFormClaimAdapter
    private val _itemTotalReqAmount = MutableLiveData<Double>()
    val itemTotalReqAmount : LiveData<Double> get() = _itemTotalReqAmount
    fun updateItemTotalAmount(amount: Double){
        _itemTotalReqAmount.value=amount
    }

    //This is for counting the item value in CreateFormClaimAdapter
    private val _itemTotalCount = MutableLiveData<Int>()
    val itemTotalCount: LiveData<Int> get() = _itemTotalCount
    fun updateItemTotalCount(count: Int) {
        _itemTotalCount.value = count
    }

    //Pending count value in Progress Claim Request Tab layout
    private val _pendingCount = MutableLiveData<String>()
    val pendingCount: LiveData<String> get() = _pendingCount
    fun updatePendingCount(count: String) {
        _pendingCount.value = count
    }
    //Completed Count value in Completed Claim Request Tab Layout
    private val _completedCount = MutableLiveData<String>()
    val completedCount: LiveData<String> get() = _completedCount
    fun updateCompletedCount(count: String) {
        _completedCount.value = count
    }

    //Pending count value in Progress Claim Approval Tab layout
    private val _pendingApprovalCount = MutableLiveData<String>()
    val pendingApprovalCount: LiveData<String> get() = _pendingApprovalCount
    fun updatePendingApprovalCount(count: String) {
        _pendingApprovalCount.value = count
    }
    //Completed Count value in Completed Claim Approval Tab Layout
    private val _completedApprovalCount = MutableLiveData<String>()
    val completedApprovalCount: LiveData<String> get() = _completedApprovalCount
    fun updateCompletedApprovalCount(count: String) {
        _completedApprovalCount.value = count
    }

    //claimRequest api used in InProgressClaimRequestFragment
    private val _listClaimRequest = MutableLiveData<listOfclaimRequestModel>()
    val listClaimRequest: MutableLiveData<listOfclaimRequestModel> get() = _listClaimRequest
    fun claimRequest(type: String) = viewModelScope.launch {
        try {
            _listClaimRequest.postValue(UIResult.Loading())
            val response = myRepo.claimRequestList(type)
            withContext(Dispatchers.IO) {
                if (response.body()?.data != null) {
                    if (response.body()?.status== "success") {
                        claimRequestData = response.body()!!.data
                        _listClaimRequest.postValue(
                            UIResult.Success(response.body())
                        )
                    } else if(response.body()?.status == "error") {
                        _listClaimRequest.postValue(
                            UIResult.Error(response.body()?.status)
                        )
                    }
                } else {
                    _listClaimRequest.postValue(
                        UIResult.Error(
                            errorMessage
                        )
                    )
                }
            }
        } catch (e: Exception) {

            _listClaimRequest.postValue(
                UIResult.Error(
                    errorMessage
                )
            )
        }
    }

    //claimCompleteRequest api used in CompletedClaimRequestFragment
    private val _listCompleteClaimRequest = SingleLiveEvent<listOfclaimRequestModel>()
    val listCompleteClaimRequest: SingleLiveEvent<listOfclaimRequestModel> get() = _listCompleteClaimRequest
    fun claimCompleteRequest() = viewModelScope.launch {
        try {
            _listCompleteClaimRequest.postValue(UIResult.Loading())
            val response = myRepo.claimCompleteRequestList()
            withContext(Dispatchers.IO) {
                if (response.body()?.data != null) {
                    if (response.body()?.status== "success") {
                        claimRequestData = response.body()!!.data
                        _listCompleteClaimRequest.postValue(
                            UIResult.Success(response.body())
                        )
                    } else if(response.body()?.status == "error") {
                        _listCompleteClaimRequest.postValue(
                            UIResult.Error(response.body()?.status)
                        )
                    }
                } else {
                    _listCompleteClaimRequest.postValue(
                        UIResult.Error(
                            errorMessage
                        )
                    )
                }
            }
        } catch (e: Exception) {

            _listCompleteClaimRequest.postValue(
                UIResult.Error(
                    errorMessage
                )
            )
        }
    }

    //Claim Request Detail
    //getClaimForm api used in ClaimRequestTabDetailFragment
    val _getClaimForm= MutableLiveData<getClaimFormModel?>()
    val getClaimForm: MutableLiveData<getClaimFormModel?> get() = _getClaimForm
    fun getClaimFormDetailList(id: String) = viewModelScope.launch {
        try {
            _getClaimForm.postValue(UIResult.Loading())
            val response = myRepo.getClaimFormDetails(id)
            withContext(Dispatchers.IO) {
                if (response.body()?.data != null) {
                    if (response.body()?.status== "success") {
                        getClaimData = response.body()!!.data
                        _getClaimForm.postValue(
                            UIResult.Success(response.body())
                        )
                    } else if(response.body()?.status == "error") {
                        _getClaimForm.postValue(
                            UIResult.Error(response.body()?.status)
                        )
                    }
                } else {
                    _getClaimForm.postValue(
                        UIResult.Error(
                            errorMessage
                        )
                    )
                }
            }
        } catch (e: Exception) {

            _getClaimForm.postValue(
                UIResult.Error(
                    errorMessage
                )
            )
        }
    }

    //submit claim api used in CreateFormBottomSheet
    val _submitClaimRequest = MutableLiveData<submitClaimRequestModel?>()
    val submitClaim: MutableLiveData<submitClaimRequestModel?> get() = _submitClaimRequest
    fun submitClaim(submitClaimRequest: SubmitClaimRequest) = viewModelScope.launch {
        try {
            _submitClaimRequest.postValue(UIResult.Loading())
            val response = myRepo.submitClaimRequest(submitClaimRequest)
            withContext(Dispatchers.IO) {
                if (response.body()?.status != null) {
                    if (response.body()?.status == "success") {
                        _submitClaimRequest.postValue(
                            UIResult.Success(response.body()),
                        )
                    } else {
                        _submitClaimRequest.postValue(
                            UIResult.Error(response.body()?.message),
                        )
                    }
                } else {
                    _submitClaimRequest.postValue(
                        UIResult.Error(
                            errorMessage

                        ),
                    )
                }
            }

        } catch (e: Exception) {
            _submitClaimRequest.postValue(UIResult.Error(e.message.toString()))
        }
    }
    // addClaimSettingList api used in CustomBottomSheetDialog
    private val _addClaimSettingList = MutableLiveData<addListClaimSettings>()
    val addClaimSettingList: MutableLiveData<addListClaimSettings> get() = _addClaimSettingList
    fun addClaimSettingList() = viewModelScope.launch {
        try {
            _addClaimSettingList.postValue(UIResult.Loading())
            val response = myRepo.addClaimSettingList()
            withContext(Dispatchers.IO) {
                if (response.body()?.data != null) {
                    if (response.body()?.status== "success") {
                        addClaimData = response.body()?.data!!
                        _addClaimSettingList.postValue(
                            UIResult.Success(response.body())
                        )
                    } else if(response.body()?.status == "error") {
                        _addClaimSettingList.postValue(
                            UIResult.Error(response.body()?.status)
                        )
                    }
                } else {
                    _addClaimSettingList.postValue(
                        UIResult.Error(
                            errorMessage
                        )
                    )
                }
            }
        } catch (e: Exception) {

            _addClaimSettingList.postValue(
                UIResult.Error(
                    errorMessage
                )
            )
        }
    }

    //uploadFile claim api used in ImageFileHandler
    private val _uploadFileClaim = SingleLiveEvent<UIResult<UploadClaimFileResponse>>()
    val uploadFileClaim: SingleLiveEvent<UIResult<UploadClaimFileResponse>> get() = _uploadFileClaim

    fun uploadFileClaim(file: MultipartBody.Part, progressListener: (Int) -> Unit) {
        viewModelScope.launch {
            try {
                _uploadFileClaim.postValue(UIResult.Loading())
                val response = myRepo.uploadClaimFile(file)
                withContext(Dispatchers.IO) {
                    if (response.body()?.data != null) {
                        if (response.body()?.status== "success") {
                            _uploadFileClaim.postValue(
                                UIResult.Success(response.body())
                            )
                        } else if(response.body()?.status == "error") {
                            _uploadFileClaim.postValue(
                                UIResult.Error(response.body()?.message.toString())
                            )
                        }
                    } else {
                        _uploadFileClaim.postValue(
                            UIResult.Error(
                                errorMessage
                            )
                        )
                    }
                }
            } catch (e: Exception) {

                _uploadFileClaim.postValue(
                    UIResult.Error(
                        errorMessage
                    )
                )
            }
        }
    }

    // LiveData to hold the updated list of items
    private val _itemsLiveData = MutableLiveData<MutableList<AddItemRequest.PendingItem>>()
    val itemsLiveData: MutableLiveData< MutableList<AddItemRequest.PendingItem>> = _itemsLiveData

    private val gson = Gson()
    fun updateAdapter() {
        val savedClaimRequestJson = appPref.getClaimRequest()
        if (!savedClaimRequestJson.isNullOrEmpty()) {
            val savedClaimRequest: AddItemRequest = gson.fromJson(savedClaimRequestJson, object : TypeToken<AddItemRequest>() {}.type)
            _itemsLiveData.value = savedClaimRequest.pendingItems
        } else {
            // Handle case where savedClaimRequestJson is null or empty
            _itemsLiveData.value = mutableListOf() // Or any default value
        }
    }

    //Claim Request Detail page group name and item data value stored in viewmodel
    private val _clickedItemRequest = MutableLiveData<GetClaimFormResponse.ClaimFormData.ClaimForm.Item>()
    val clickedItemRequest: LiveData<GetClaimFormResponse.ClaimFormData.ClaimForm.Item> get() = _clickedItemRequest
    private val _groupName = MutableLiveData<String>()
    val groupName: LiveData<String> get() = _groupName

    fun setClickedItemRequest(item: GetClaimFormResponse.ClaimFormData.ClaimForm.Item) {
        _clickedItemRequest.value = item
    }

    fun setGroupName(name: String) {
        _groupName.value = name
    }

    //ClaimApproval
    //getClaimForm api used in ClaimApprovalTabDetailFragment
    val _getClaimApprovalDetailForm= MutableLiveData<getClaimApprovalDetailModel?>()
    val getClaimApprovalDetailForm: MutableLiveData<getClaimApprovalDetailModel?> get() = _getClaimApprovalDetailForm
    fun getClaimApprovalDetailForm(id: String) = viewModelScope.launch {
        try {
            _getClaimApprovalDetailForm.postValue(UIResult.Loading())
            val response = myRepo.getClaimApprovalDetail(id)
            withContext(Dispatchers.IO) {
                if (response.body()?.data != null) {
                    if (response.body()?.status== "success") {
                        _getClaimApprovalDetailForm.postValue(
                            UIResult.Success(response.body())
                        )
                    } else if(response.body()?.status == "error") {
                        _getClaimApprovalDetailForm.postValue(
                            UIResult.Error(response.body()?.status)
                        )
                    }
                } else {
                    _getClaimApprovalDetailForm.postValue(
                        UIResult.Error(
                            errorMessage
                        )
                    )
                }
            }
        } catch (e: Exception) {

            _getClaimApprovalDetailForm.postValue(
                UIResult.Error(
                    errorMessage
                )
            )
        }
    }

    //getclaimApprovalProgress api used in InProgressClaimApprovalFragment
    val _getClaimApprovalProgress = MutableLiveData<getClaimApprovalProgressModel?>()
    val getClaimApprovalProgress: MutableLiveData<getClaimApprovalProgressModel?> get() = _getClaimApprovalProgress
    fun getClaimApprovalProgress(type: String) = viewModelScope.launch {
        try {
            _getClaimApprovalProgress.postValue(UIResult.Loading())
            val response = myRepo.claimApprovalProgressList(type)
            withContext(Dispatchers.IO) {
                if (response.body()?.status != null) {
                    if (response.body()?.status == "success") {
                        _getClaimApprovalProgress.postValue(
                            UIResult.Success(response.body()),
                        )
                    } else {
                        _getClaimApprovalProgress.postValue(
                            UIResult.Error(response.body()?.message),
                        )
                    }
                } else {
                    _getClaimApprovalProgress.postValue(
                        UIResult.Error(
                            errorMessage

                        ),
                    )
                }
            }

        } catch (e: Exception) {
            _getClaimApprovalProgress.postValue(UIResult.Error(e.message.toString()))
        }
    }

    //getclaimApprovalProgress api used in CompletedClaimApprovalFragment
    val _getClaimApprovalComplete = MutableLiveData<getClaimApprovalCompleteModel?>()
    val getClaimApprovalComplete: MutableLiveData<getClaimApprovalCompleteModel?> get() = _getClaimApprovalComplete
    fun getClaimApprovalComplete() = viewModelScope.launch {
        try {
            _getClaimApprovalComplete.postValue(UIResult.Loading())
            val response = myRepo.claimApprovalCompleteList()
            withContext(Dispatchers.IO) {
                if (response.body()?.status != null) {
                    if (response.body()?.status == "success") {
                        _getClaimApprovalComplete.postValue(
                            UIResult.Success(response.body()),
                        )
                    } else {
                        _getClaimApprovalComplete.postValue(
                            UIResult.Error(response.body()?.message),
                        )
                    }
                } else {
                    _getClaimApprovalComplete.postValue(
                        UIResult.Error(
                            errorMessage

                        ),
                    )
                }
            }

        } catch (e: Exception) {
            _getClaimApprovalComplete.postValue(UIResult.Error(e.message.toString()))
        }
    }






    //actionApprove api used in ClaimApprovalTabDetailFragment
    val _actionApprove= MutableLiveData<actionApproveModel?>()
    val actionApprove: MutableLiveData<actionApproveModel?> get() = _actionApprove
    fun actionApproveApi(approveRequest: ActionRequest) = viewModelScope.launch {
        try {
            _actionApprove.postValue(UIResult.Loading())
            val response = myRepo.actionApprove(approveRequest)
            withContext(Dispatchers.IO) {
                if (response.body()?.data != null) {
                    if (response.body()?.status== "success") {
                        _actionApprove.postValue(
                            UIResult.Success(response.body())
                        )
                    } else if(response.body()?.status == "error") {
                        _actionApprove.postValue(
                            UIResult.Error(response.body()?.status)
                        )
                    }
                } else {
                    _actionApprove.postValue(
                        UIResult.Error(
                            errorMessage
                        )
                    )
                }
            }
        } catch (e: Exception) {

            _actionApprove.postValue(
                UIResult.Error(
                    errorMessage
                )
            )
        }
    }

    //actionReject api used in ClaimApprovalTabDetailFragment
    val _actionReject= MutableLiveData<actionApproveModel?>()
    val actionReject: MutableLiveData<actionApproveModel?> get() = _actionReject
    fun actionRejectApi(rejectRequest: ActionRequest) = viewModelScope.launch {
        try {
            _actionReject.postValue(UIResult.Loading())
            val response = myRepo.actionReject(rejectRequest)
            withContext(Dispatchers.IO) {
                if (response.body()?.data != null) {
                    if (response.body()?.status== "success") {
                        _actionReject.postValue(
                            UIResult.Success(response.body())
                        )
                    } else if(response.body()?.status == "error") {
                        _actionReject.postValue(
                            UIResult.Error(response.body()?.status)
                        )
                    }
                } else {
                    _actionReject.postValue(
                        UIResult.Error(
                            errorMessage
                        )
                    )
                }
            }
        } catch (e: Exception) {

            _actionReject.postValue(
                UIResult.Error(
                    errorMessage
                )
            )
        }
    }
}