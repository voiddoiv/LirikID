package com.example.lirikid.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lirikid.databinding.ActivitySingupBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class SingupActivity : AppCompatActivity() {

    lateinit var binding: ActivitySingupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createAccountBtn.setOnClickListener {
            val email = binding.emailEdittext.text.toString()
            val password = binding.passwordEdittext.text.toString()
            val confirmPassword = binding.confirmPasswordEdittext.text.toString()

            if (!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), email)) {
                binding.emailEdittext.error = "Email tidak valid"
                return@setOnClickListener
            }

            if (password.length < 6) {
                binding.passwordEdittext.error = "Password harus 6 karakter"
                return@setOnClickListener
            }

            if (!password.equals(confirmPassword)) {
                binding.confirmPasswordEdittext.error = "Password tidak sama"
                return@setOnClickListener
            }

            createAccountWithFirebase(email, password)
        }

        binding.gotoLoginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    fun createAccountWithFirebase(email: String, password: String) {
        setInProgress(true)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                setInProgress(false)
                Toast.makeText(applicationContext, "Berhasil membuat akun", Toast.LENGTH_SHORT).show()

                // Pindah ke LoginActivity setelah registrasi berhasil
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                // finish() // Menutup aktivitas saat ini agar tidak kembali ke sini
            }.addOnFailureListener {
                setInProgress(false)
                Toast.makeText(applicationContext, "Gagal membuat akun", Toast.LENGTH_SHORT).show()
            }
    }

    fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.createAccountBtn.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.createAccountBtn.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }
}
