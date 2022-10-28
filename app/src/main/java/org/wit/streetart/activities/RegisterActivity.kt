package org.wit.streetart.activities

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.streetart.databinding.ActivityStreetartBinding
import org.wit.streetart.main.MainApp
import org.wit.streetart.models.UserModel
import timber.log.Timber


class RegisterActivity : AppCompatActivity() {

    var users = UserModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(org.wit.streetart.R.layout.activity_register)

        app = application as MainApp

        val username = findViewById<View>(org.wit.streetart.R.id.username ) as TextView
        val password = findViewById<View>(org.wit.streetart.R.id.password) as TextView

        val signupbtn = findViewById<View>(org.wit.streetart.R.id.signupbtn) as Button

        signupbtn.setOnClickListener() {
            users.name = username.text.toString()
            users.password = password.text.toString()
            app.users.create(users.copy())
            setResult(RESULT_OK)
            finish()
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            }
    }
}

