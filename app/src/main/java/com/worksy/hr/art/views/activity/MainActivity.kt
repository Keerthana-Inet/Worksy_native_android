package   com.worksy.hr.art.views.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.ActivityMainBinding
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.utils.gone
import com.worksy.hr.art.utils.visible
import com.worksy.hr.art.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var appPref: SharedPrefManager
    companion object {
        private var isLoading = false
        private lateinit var mContext: MainActivity
        lateinit var fragment: FragmentManager
        fun showLoading() {
            if (!isLoading) {
                mContext.binding.progressCircular.visible()
                isLoading = true
            }
        }

        fun hideLoading() {
            if (isLoading) {
                mContext.binding.progressCircular.gone()
                isLoading = false
            }
        }

        fun back() {
            mContext.finish()
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding = ActivityMainBinding.inflate(layoutInflater)
        mContext = this
        fragment = supportFragmentManager
        setContentView(binding.root)

    }
}