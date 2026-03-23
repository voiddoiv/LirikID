package com.example.lirikid.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lirikid.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Mengaktifkan mode layar penuh
        ViewCompat.getWindowInsetsController(window.decorView)?.hide(WindowInsetsCompat.Type.statusBars())

        Handler(Looper.getMainLooper()).postDelayed({
            goSigninActivity()
        }, 3000L) // Tunggu 3 detik
    }

    private fun goSigninActivity() {
        Intent(this, LoginActivity::class.java).also {
            startActivity(it)
            finish() // Mengakhiri SplashActivity
        }
    }
}