package com.worksy.hr.art.repository.data.networkapi

import com.worksy.hr.art.models.CommonResponse
import com.worksy.hr.art.models.claimApproval.ApproveFormRequest
import com.worksy.hr.art.models.claimApproval.ActionRequest
import com.worksy.hr.art.models.claimApproval.ClaimApprovalDetailResponse
import com.worksy.hr.art.models.claimApproval.ClaimApprovalProgressResponse
import com.worksy.hr.art.models.claimRequestResponse.AddClaimSettingsResponse
import com.worksy.hr.art.models.claimRequestResponse.ClaimRequestListResponse
import com.worksy.hr.art.models.claimRequestResponse.GetClaimFormResponse
import com.worksy.hr.art.models.claimRequestResponse.SubmitClaimRequest
import com.worksy.hr.art.models.claimRequestResponse.UploadClaimFileResponse
import com.worksy.hr.art.models.companies.ListCompaniesResponse
import com.worksy.hr.art.models.login.LoginEmailRequest
import com.worksy.hr.art.models.login.LoginRequest
import com.worksy.hr.art.models.login.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface MyWebApi {
    //Login with Mobile Number
    @POST("api/login")
    suspend fun checkLogin(
        @Body loginRequest: LoginRequest,
    ): Response<LoginResponse>

    // Login with email
    @POST("api/login")
    suspend fun checkEmailLogin(
        @Body loginEmailRequest: LoginEmailRequest,
    ): Response<LoginResponse>

    //companies list
    @GET("api/companies")
    suspend fun listCompanies(): Response<ListCompaniesResponse>

    //claim progress request list
    @GET("api/claim/request/list")
    suspend fun claimRequestList(@Query("type") type:String): Response<ClaimRequestListResponse>

    //claim completed request list
    @GET("api/claim/request/list")
    suspend fun claimCompleteRequestList(): Response<ClaimRequestListResponse>

    //add claim settings api
    @GET("api/claim/request/item-setting")
    suspend fun addClaimSetting(): Response<AddClaimSettingsResponse>

    //uploading file in add claim
    @Multipart
    @POST("api/claim/request/upload-file")
    suspend fun uploadClaimFile(@Part file: MultipartBody.Part): Response<UploadClaimFileResponse>

    //logout api
    @GET("api/logout")
    suspend fun logout(@Query("device_id") deviceId:String): Response<CommonResponse>

    //submit claim request api
    @POST("api/claim/request/submit-form")
    suspend fun submitClaim(@Body submitClaimRequest: SubmitClaimRequest): Response<CommonResponse>

    //Get claim form details
    @GET("api/claim/request/form")
    suspend fun getClaimFormDetails(@Query("id") id:String): Response<GetClaimFormResponse>

    //Claim approval progress list api
    @GET("api/claim/approval/list")
    suspend fun claimApprovalProgressList(@Query("type") type: String): Response<ClaimApprovalProgressResponse>
    //claim approval complete list appi
    @GET("api/claim/approval/list")
    suspend fun claimApprovalCompleteList(): Response<ClaimApprovalProgressResponse>

    //Get claim approval form detail api
    @GET("api/claim/approval/form")
    suspend fun getClaimApprovalFormDetails(@Query("id") id:String): Response<ClaimApprovalDetailResponse>

    // approve form
    @POST("api/claim/approval/action")
    suspend fun approveForm(@Body approveFormRequest: ApproveFormRequest): Response<CommonResponse>

    //Api for approve the pending items
    @POST("api/claim/approval/action")
    suspend fun actionApprove(
        @Body actionRequest: ActionRequest,
    ): Response<CommonResponse>

    //Api for Reject pending items
    @POST("api/claim/approval/action")
    suspend fun actionReject(
        @Body actionRequest: ActionRequest,
    ): Response<CommonResponse>
}



