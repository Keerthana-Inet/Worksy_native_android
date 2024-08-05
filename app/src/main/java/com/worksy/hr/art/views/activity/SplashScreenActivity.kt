package com.worksy.hr.art.views.activity
import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.worksy.hr.art.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpalshScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val relGettingReady:RelativeLayout=findViewById(R.id.rel_getting_ready)
        relGettingReady.setOnClickListener {
           /* val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)*/
        }
    }
}
