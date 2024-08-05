package com.worksy.hr.art.repository

import com.worksy.hr.art.models.claimApproval.ApproveFormRequest
import com.worksy.hr.art.models.claimApproval.ActionRequest
import com.worksy.hr.art.models.claimRequestResponse.SubmitClaimRequest
import com.worksy.hr.art.models.login.LoginEmailRequest
import com.worksy.hr.art.models.login.LoginRequest
import com.worksy.hr.art.repository.data.networkapi.MyWebApi
import okhttp3.MultipartBody
import javax.inject.Inject

class MyRepo @Inject constructor(private val myWebApi: MyWebApi) {

    suspend fun checkUserLogin(loginRequest: LoginRequest) =
        myWebApi.checkLogin(loginRequest)

    suspend fun checkEmailLogin(loginEmailRequest: LoginEmailRequest) =
        myWebApi.checkEmailLogin(loginEmailRequest)
    suspend fun listOfCompanies() = myWebApi.listCompanies()

    suspend fun claimRequestList(type:String) = myWebApi.claimRequestList(type)

    suspend fun claimCompleteRequestList() = myWebApi.claimCompleteRequestList()
    suspend fun addClaimSettingList() = myWebApi.addClaimSetting()
    suspend fun uploadClaimFile(file: MultipartBody.Part) = myWebApi.uploadClaimFile(file)
    suspend fun logout(deviceId:String) = myWebApi.logout(deviceId)
    suspend fun getClaimFormDetails(id:String) = myWebApi.getClaimFormDetails(id)
    suspend fun submitClaimRequest(submitClaimRequest: SubmitClaimRequest) = myWebApi.submitClaim(submitClaimRequest)
    suspend fun claimApprovalProgressList(type:String) = myWebApi.claimApprovalProgressList(type)
    suspend fun claimApprovalCompleteList() = myWebApi.claimApprovalCompleteList()
    suspend fun getClaimApprovalDetail(id:String) = myWebApi.getClaimApprovalFormDetails(id)
    suspend fun approveForm(approveFormRequest: ApproveFormRequest) = myWebApi.approveForm(approveFormRequest)
    suspend fun actionApprove(approveRequest: ActionRequest) = myWebApi.actionApprove(approveRequest)
    suspend fun actionReject(approveRequest: ActionRequest) = myWebApi.actionReject(approveRequest)

}
