package   com.worksy.hr.art.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.worksy.hr.art.PasswordModel
import com.worksy.hr.art.models.NewpasswdResponse
import com.worksy.hr.art.repository.MyRepo
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.utils.SingleLiveEvent
import com.worksy.hr.art.utils.UIResult
import com.worksy.hr.art.utils.Validator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val myRepo: MyRepo,
    private val appPref: SharedPrefManager,

) : ViewModel() {
}