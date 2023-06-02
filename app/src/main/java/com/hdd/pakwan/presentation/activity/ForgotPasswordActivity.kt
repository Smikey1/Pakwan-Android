package com.hdd.pakwan.presentation.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.hdd.pakwan.R
import com.hdd.pakwan.data.models.User
import com.hdd.pakwan.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgotPasswordActivity : AppCompatActivity() {
    //Initialize

    private lateinit var et_fp_email: EditText
    private lateinit var et_fp_new_password: EditText
    private lateinit var et_fp_confirm_password: EditText
    private lateinit var et_fp_reset_code: EditText
    private lateinit var tv_fp_email: TextView
    private lateinit var btnFP: Button
    private lateinit var btnAlready: Button
    private lateinit var ll_fp_reset_layout: LinearLayout

    private var isTimeForNewPassword: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        et_fp_email = findViewById(R.id.et_fp_email)
        btnFP = findViewById(R.id.btnFP)
        btnAlready = findViewById(R.id.btnAlready)
        et_fp_new_password = findViewById(R.id.et_fp_new_password)
        et_fp_confirm_password = findViewById(R.id.et_fp_confirm_password)
        et_fp_reset_code = findViewById(R.id.et_fp_reset_code)
        tv_fp_email = findViewById(R.id.tv_fp_email)
        ll_fp_reset_layout = findViewById(R.id.ll_fp_reset_layout)

        et_fp_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkInputProvidedByUser()
            }
        })

        btnFP.setOnClickListener {
            if (isTimeForNewPassword) {
                when {
                    TextUtils.isEmpty(et_fp_new_password.text) -> {
                        et_fp_new_password.requestFocus()
                        et_fp_new_password.error = "Password cannot be empty."
                    }
                    (et_fp_new_password.text.length < 5) -> {
                        et_fp_new_password.requestFocus()
                        et_fp_new_password.error = "must be greater than 4 character."
                    }
                    TextUtils.isEmpty(et_fp_confirm_password.text) -> {
                        et_fp_confirm_password.requestFocus()
                        et_fp_confirm_password.error = "Confirm Password cannot be empty."
                    }
                    (et_fp_confirm_password.text.length < 5) -> {
                        et_fp_confirm_password.requestFocus()
                        et_fp_confirm_password.error = "must be greater than 4 character."
                    }
                    TextUtils.isEmpty(et_fp_reset_code.text) -> {
                        et_fp_reset_code.requestFocus()
                        et_fp_reset_code.error = "Reset code cannot be empty."
                    }
                    (et_fp_reset_code.text.length != 6) -> {
                        et_fp_reset_code.requestFocus()
                        et_fp_reset_code.error = "Reset code must be 6 digit."
                    }
                    else -> {
                        var password: String = et_fp_new_password.text.toString()
                        var email: String = tv_fp_email.text.toString()
                        var cpassword: String = et_fp_confirm_password.text.toString()
                        var resetCode: String = et_fp_reset_code.text.toString()
                        if (password == cpassword) {
                            setNewPassword(email, password, resetCode)
                        } else {
                            et_fp_confirm_password.error = "Confirm password doesnot match password"
                        }
                    }
                }
            } else {
                checkForEmailAndPassword()
            }
        }

        btnAlready.setOnClickListener {
            val email = et_fp_email.text.toString()
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()
            ) {
                validateEmail(email)
            } else {
                et_fp_email.error = "Please enter a valid email address"
                et_fp_email.requestFocus()
            }
        }
    }

    private fun validateEmail(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository = UserRepository()
                val user = User(email = email)
                val response = userRepository.validateEmail(user)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    if (response.success == true) {
                        checkEmail(email)
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    println(ex)
                }
            }
        }
    }

    private fun setNewPassword(email: String, password: String, resetCode: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository = UserRepository()
                val user = User(email = email, newPassword = password, resetCode = resetCode)
                val response = userRepository.setNewPassword(user)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ForgotPasswordActivity,
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
//                if (response.success == true) {}
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    println(ex)
                }
            }
        }
    }


    @SuppressLint("ResourceAsColor")
    private fun checkForEmailAndPassword() {
        val email = et_fp_email.text.toString()
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()
        // TODO 6: &&  et_fp_email.text.toString() == db_register_email
        ) {
            btnFP.setTextColor(R.color.textDisabledColor)
            resetPassword(email)
        } else {
            et_fp_email.error = "Please enter a valid email address"
            et_fp_email.requestFocus()
            return
        }
    }

    private fun resetPassword(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository = UserRepository()
                val user = User(email = email)
                val response = userRepository.resetUser(user)
                if (response.success == true) {
                    withContext(Dispatchers.Main) {
                        checkEmail(email)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    println(ex)
                }
            }
        }
    }

    private fun checkEmail(email: String) {
        ll_fp_reset_layout.visibility = View.VISIBLE
        et_fp_email.visibility = View.GONE
        isTimeForNewPassword = true
        tv_fp_email.text = email
        btnFP.text = "Change Password"
        btnAlready.visibility = View.GONE
        Toast.makeText(
            this@ForgotPasswordActivity,
            "Check your email",
            Toast.LENGTH_SHORT
        ).show()
    }


    @SuppressLint("ResourceAsColor")
    private fun checkInputProvidedByUser() {
        if (!TextUtils.isEmpty(et_fp_email.text)) {
            btnFP.isEnabled = true
            btnFP.setBackgroundColor(application.resources.getColor(R.color.buttonColor))
            btnFP.setTextColor(application.resources.getColor(R.color.white))
            btnAlready.isEnabled = true
            btnAlready.setBackgroundColor(application.resources.getColor(R.color.buttonColor))
            btnAlready.setTextColor(application.resources.getColor(R.color.white))

        } else {
            btnFP.isEnabled = false
            btnFP.setTextColor(R.color.textDisabledColor)
            btnAlready.isEnabled = false
            btnAlready.setTextColor(R.color.textDisabledColor)
        }
    }


}