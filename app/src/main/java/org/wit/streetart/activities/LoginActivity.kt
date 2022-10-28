package org.wit.streetart.activities



import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast



class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(org.wit.streetart.R.layout.activity_login)

        val username = findViewById<View>(org.wit.streetart.R.id.username) as TextView
        val password = findViewById<View>(org.wit.streetart.R.id.password) as TextView

        val loginbtn = findViewById<View>(org.wit.streetart.R.id.loginbtn) as Button
        val registerbtn = findViewById<View>(org.wit.streetart.R.id.register_btn) as Button

        registerbtn.setOnClickListener{
            val intent1 = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent1)
        }

        loginbtn.setOnClickListener() {
            if (username.text.toString() == "admin" && password.text.toString() == "admin") {
                val intent = Intent(this@LoginActivity, StreetArtListActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@LoginActivity,"LOGIN FAILED !!!",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
