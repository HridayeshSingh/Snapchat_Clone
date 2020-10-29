package com.hridayesh.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    var emailEditText: EditText? = null
    var passwordEditText: EditText? = null
    private lateinit var auth : FirebaseAuth
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        if(auth.currentUser != null) {
            logIn()
        }
    }

    fun goClicked(view : View) {
        val email = emailEditText?.text.toString()
        val password = passwordEditText?.text.toString()

        //Check if we can log in the user
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    logIn()
                } else {
                    //Sign up the user
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if(task.isSuccessful) {
                                Firebase.database.reference.child("users").child(task.result!!.user!!.uid).child("email").setValue(email)
                                logIn()
                            } else {
                                Toast.makeText(this, "Login Failed!! Try Again!!.", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
    }

    fun logIn() {
        //Move to next activity
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }
}
