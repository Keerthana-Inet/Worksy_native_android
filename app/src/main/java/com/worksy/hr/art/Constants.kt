package com.worksy.hr.art


import com.worksy.hr.art.models.CommonResponse
import com.worksy.hr.art.models.NewpasswdResponse
import com.worksy.hr.art.models.claimApproval.ClaimApprovalDetailResponse
import com.worksy.hr.art.models.claimApproval.ClaimApprovalProgressResponse
import com.worksy.hr.art.models.claimRequestResponse.AddClaimSettingsResponse
import com.worksy.hr.art.models.claimRequestResponse.ClaimRequestListResponse
import com.worksy.hr.art.models.claimRequestResponse.GetClaimFormResponse
import com.worksy.hr.art.models.companies.ListCompaniesResponse
import com.worksy.hr.art.models.login.LoginResponse
import com.worksy.hr.art.utils.UIResult

typealias PasswordModel = UIResult<NewpasswdResponse>
typealias loginUserCheckModel = UIResult<LoginResponse>
typealias listOfCompaniesModel = UIResult<ListCompaniesResponse>
typealias listOfclaimRequestModel = UIResult<ClaimRequestListResponse>
typealias logoutModel = UIResult<CommonResponse>
typealias addListClaimSettings = UIResult<AddClaimSettingsResponse>
typealias submitClaimRequestModel = UIResult<CommonResponse>
typealias getClaimFormModel = UIResult<GetClaimFormResponse>
typealias getClaimApprovalDetailModel = UIResult<ClaimApprovalDetailResponse>
typealias getClaimApprovalProgressModel = UIResult<ClaimApprovalProgressResponse>
typealias getClaimApprovalCompleteModel = UIResult<ClaimApprovalProgressResponse>
typealias actionApproveModel = UIResult<CommonResponse>

object Constants {
    const val TAG = ""

    const val MY_DATABASE_NAME = "my_database"
    const val USER_TABLE = "UserTable"
    const val IMG_PLACEHOLDER = "https://via.placeholder.com/150"

    const val LOCALIZATION_TABLE = "LocalizationTable"
    
    const val BASE_URL = "https://graceful-farm-jb4kz7takq4s.vapor-farm-g1.com/"
    // Preff
    const val TOKEN = "token"
    const val FCMTOKEN = "fcm_token"
    const val UPDATEDEMAIL = "updated_email"
    const val HINT_STATUS = "hint_status"

    const val LOGIN_STATUS = "login_status"
    const val EDIT_STATUS = "edit_status"
    const val PROFILE_PIC = "profile_pic"
    const val UNIQUE_ID = "unique_id"

    const val AVATAR_ID = "avatar_id"
    const val AVATAR_URL = "avatar_url"
    const val EMAIL = "email"
    const val COMPANY_ACCOUNT_ID = "company_account_id"
    const val PHONE_NUMBER = "phone_number"
    const val USER_NAME = "user_name"
    const val CLAIM_REQUEST = "claim_request"
    const val PENDING_AMOUNT = "pending_amount"
    const val PENDING_ID = "pending_id"
    const val COMPANY_CODE = "company_code"
    const val FORM_TITLE = "form_title"
    const val FORM_DESC = "form_desc"
    const val FORM_ID = "form_id"
    const val STATUS_TYPE = "status_type"
    const val USER_ID = "user_id"
    const val COMMENT = "comment"
    const val AUTH_PROVIDER = "auth_provider"
    const val COUNTRY_TO = "country_to"
    const val RECEIVE_METHOD = "receive_method"
    const val CURRENCY_FROM = "currency_from"
    const val CURRENCY_TO = "currency_to"
    const val GUEST_WELCOME_USER = "auth_provider"
    const val WELCOME_USER = "welcome_user"
    const val GUEST_USER = "guest_user"
    const val STATUS_NME = "statustype_name"
    const val MANAGER_RATE = "manager_rate"

    const val MY_PREF = "my_pref"
    const val PREF_NAME = "maya_pref"
    const val OTP = "otp"
    const val PHONE = "phone"
    const val IS_REGISTER = "is_register"
    const val PASSWORD = "passowrd"
    const val USER_DT = "user_dt"
    const val LOCALIZATION_VERSION = "localization_version"
}
