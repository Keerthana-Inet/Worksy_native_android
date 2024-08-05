package   com.worksy.hr.art.repository.data.localdatabse

import android.content.SharedPreferences
import androidx.core.content.edit
import com.worksy.hr.art.Constants.CLAIM_REQUEST
import com.worksy.hr.art.Constants.COMPANY_ACCOUNT_ID
import com.worksy.hr.art.Constants.COMPANY_CODE
import com.worksy.hr.art.Constants.EMAIL
import com.worksy.hr.art.Constants.FCMTOKEN
import com.worksy.hr.art.Constants.FORM_DESC
import com.worksy.hr.art.Constants.FORM_TITLE
import com.worksy.hr.art.Constants.FORM_ID
import com.worksy.hr.art.Constants.LOCALIZATION_VERSION
import com.worksy.hr.art.Constants.PENDING_AMOUNT
import com.worksy.hr.art.Constants.PENDING_ID
import com.worksy.hr.art.Constants.PHONE_NUMBER
import com.worksy.hr.art.Constants.PROFILE_PIC
import com.worksy.hr.art.Constants.TOKEN
import com.worksy.hr.art.Constants.USER_ID
import com.worksy.hr.art.Constants.USER_NAME
import javax.inject.Inject

class SharedPrefManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    fun clearPreferences() {
        sharedPreferences.edit().clear().apply()
    }
    fun setToken(token: String) = sharedPreferences.edit { putString(TOKEN, token) }
    fun getToken() = sharedPreferences.getString(TOKEN, "")

    fun setProfilePic(pick: String) = sharedPreferences.edit { putString(PROFILE_PIC, pick) }
    fun getProfilePic() = sharedPreferences.getString(PROFILE_PIC, "")

    fun setName(name: String) = sharedPreferences.edit { putString(USER_NAME, name) }
    fun getName() = sharedPreferences.getString(USER_NAME, "")

    fun setEmail(name: String) = sharedPreferences.edit { putString(EMAIL, name) }
    fun getEmail() = sharedPreferences.getString(EMAIL, "")

    fun setCompanyAccountId(id: Int) = sharedPreferences.edit { putInt(COMPANY_ACCOUNT_ID, id) }
    fun getCompanyAccountId() = sharedPreferences.getInt(COMPANY_ACCOUNT_ID, 0)
    fun setPhoneNumber(name: String) = sharedPreferences.edit { putString(PHONE_NUMBER, name) }
    fun getPhoneNumber() = sharedPreferences.getString(PHONE_NUMBER, "")

    fun setUserName(name: String) = sharedPreferences.edit { putString(USER_NAME, name) }
    fun getUserName() = sharedPreferences.getString(USER_NAME, "")

    fun setClaimRequest(name: String) = sharedPreferences.edit { putString(CLAIM_REQUEST, name) }
    fun getClaimRequest() = sharedPreferences.getString(CLAIM_REQUEST, "")
    fun setPendingTotalAmount(name: String) = sharedPreferences.edit { putString(PENDING_AMOUNT, name) }
    fun getPendingTotalAmount() = sharedPreferences.getString(PENDING_AMOUNT, "")

    fun setPendingId(name: String) = sharedPreferences.edit { putString(PENDING_ID, name) }
    fun getPendingId() = sharedPreferences.getString(PENDING_ID, "")
    fun setFormTitle(name: String) = sharedPreferences.edit { putString(FORM_TITLE, name) }
    fun getFormTitle() = sharedPreferences.getString(FORM_TITLE, "")
    fun setFormDesc(name: String) = sharedPreferences.edit { putString(FORM_DESC, name) }
    fun getFormDesc() = sharedPreferences.getString(FORM_DESC, "")

    fun setFormId(name: String) = sharedPreferences.edit { putString(FORM_ID, name) }
    fun getFormId() = sharedPreferences.getString(FORM_ID, "")

    fun setCompanyCode(name: String) = sharedPreferences.edit { putString(COMPANY_CODE, name) }
    fun getCompanyCode() = sharedPreferences.getString(COMPANY_CODE, "")



    fun setuserId(name: String) = sharedPreferences.edit { putString(USER_ID, name) }
    fun getuserId() = sharedPreferences.getString(USER_ID, "")

    fun setLocalizationVersion(version: String) = sharedPreferences.edit { putString(
        LOCALIZATION_VERSION, version) }
    fun getLocalizationVersion() = sharedPreferences.getString(LOCALIZATION_VERSION, "")

    fun setFCMToken(token: String) = sharedPreferences.edit { putString(FCMTOKEN, token) }
    fun getFCMToken() = sharedPreferences.getString(FCMTOKEN, "")
}