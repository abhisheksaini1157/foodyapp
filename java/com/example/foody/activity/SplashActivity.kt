package com.example.foody.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.foody.R

class SplashActivity : AppCompatActivity() {

    //splash screen timer
    private val SPLASH_TIME_OUT=1000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(
            {
                val intent=Intent(this@SplashActivity,
                    LoginRegisterActivity::class.java)
                startActivity(intent)
                finish()
            },SPLASH_TIME_OUT)
    }
}