@file:Suppress("DEPRECATION")

package com.example.sistema_educativo_padres.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sistema_educativo_padres.MainActivity
import com.example.sistema_educativo_padres.R
import com.example.sistema_educativo_padres.data.Padre
import com.example.sistema_educativo_padres.sec.TokenManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        FirebaseApp.initializeApp(this)

        firebaseAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.api_key))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        findViewById<Button>(R.id.loginButtonGoogle).setOnClickListener {
            signInWithGoogle()
        }

        findViewById<Button>(R.id.loginButton).setOnClickListener {
            signInWithEmail()
        }

        findViewById<Button>(R.id.registerButton).setOnClickListener {
            registerWithEmail()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signInWithEmail() {
        val emailField = findViewById<EditText>(R.id.emailText)
        val passwordField = findViewById<EditText>(R.id.passwordText)

        val email = emailField.text.toString()
        val password = passwordField.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Inicio de sesión con correo exitoso", Toast.LENGTH_SHORT).show()
                    val user = FirebaseAuth.getInstance().currentUser
                    if(user != null) {
                        verifyCurrentUser(user)
                    }
                } else {
                    Toast.makeText(this, "Error de inicio de sesión con correo", Toast.LENGTH_SHORT).show()
                }
            }
        }else {
            Toast.makeText(this, "Por favor, ingrese correo y contraseña", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()

        val user = FirebaseAuth.getInstance().currentUser
        if(user != null) {
            verifyCurrentUser(user)
            return
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val cuenta = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(cuenta)
            } catch (e: ApiException) {
                Toast.makeText(this, "Error en Google Sign-In", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun firebaseAuthWithGoogle(cuenta: GoogleSignInAccount?) {
        val crediencial = GoogleAuthProvider.getCredential(cuenta?.idToken, null)
        firebaseAuth.signInWithCredential(crediencial).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Inicio de sesión con Google exitoso", Toast.LENGTH_SHORT).show()

                    val user = firebaseAuth.currentUser

                    if(user != null) {
                        val padreGoogle = Padre(-1, nombre = cuenta?.displayName ?: "", cuenta?.email ?: "")
                        savePadreToDatabase(padreGoogle)
                    }

                    if (user != null) {
                        verifyCurrentUser(user)
                    }
                }else {
                    Toast.makeText(this, "Fallo en la autenticación", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun registerWithEmail() {
        val userField = findViewById<EditText>(R.id.userText)
        val emailField = findViewById<EditText>(R.id.emailText)
        val passwordField = findViewById<EditText>(R.id.passwordText)

        val user = userField.text.toString()
        val email = emailField.text.toString()
        val password = passwordField.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty() && user.isNotEmpty()) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()

                        UserProfileChangeRequest.Builder().setDisplayName(user).build()

                        val padre = Padre(-1, nombre = user, email = email)
                        savePadreToDatabase(padre)

                        val userNow = FirebaseAuth.getInstance().currentUser
                        if (userNow != null) {
                            verifyCurrentUser(userNow)
                        }
                    }else {
                        Toast.makeText(this, "Error en el registro", Toast.LENGTH_SHORT).show()
                    }
            }
        }else {
            Toast.makeText(this, "Por favor, ingrese correo, contraseña y usuario", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePadreToDatabase(padre: Padre) {
        val url = "http://192.168.0.10:8080/escuelaPadres/api/padres/add"
        val jsonBody = JSONObject()
        jsonBody.put("nombre", padre.nombre)
        jsonBody.put("email", padre.email)

        val requestBody = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder().url(url).post(requestBody).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val token = response.header("Authorization")?.substringAfter("Bearer ")
                    if(token != null) {
                        TokenManager.guardarToken(applicationContext, token)
                    }

                    Log.d("Registro", "Padre registrado en la base de datos")
                }else {
                    Log.e("Registro", "Error al registrar en la base de datos")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                Log.e("Registro", "Fallo en la conexión al servicio REST", e)
            }
        })
    }

    private fun verifyCurrentUser(user: FirebaseUser) {
        savePadreToDatabase(Padre(0, user.displayName.toString(), user.email.toString()))
        val main = Intent(this, MainActivity::class.java).apply {
            putExtra("userName", user.displayName)
            putExtra("userEmail", user.email)
            putExtra("userPhotoUrl", user.photoUrl.toString())
        }
        startActivity(main)
        finish()
    }

    public fun getCurrentUserEmail(): String {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            return user.email.toString()
        }
        return ""
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}