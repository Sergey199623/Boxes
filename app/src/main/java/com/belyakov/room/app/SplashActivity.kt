package com.belyakov.room.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.belyakov.room.R
import com.belyakov.room.app.screens.splash.SplashFragment
import com.belyakov.room.app.screens.splash.SplashViewModel

/**
 * Entry point of the app.
 *
 * Splash activity contains only window background, all other initialization logic is placed to
 * [SplashFragment] and [SplashViewModel].
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Repositories.init(applicationContext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

}