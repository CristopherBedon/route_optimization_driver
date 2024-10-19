package com.aproc.uberclonedriverkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.aproc.uberclonedriverkotlin.databinding.ActivityRegisterBinding
import com.aproc.uberclonedriverkotlin.models.Client
import com.aproc.uberclonedriverkotlin.models.Driver
import com.aproc.uberclonedriverkotlin.providers.AuthProvider
import com.aproc.uberclonedriverkotlin.providers.ClientProvider
import com.aproc.uberclonedriverkotlin.providers.DriverProvider

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authProvider = AuthProvider()
    private val driverProvider = DriverProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        //Métodos
        binding.btnGoToLogin.setOnClickListener { goToLogin() }
        binding.btnRegister.setOnClickListener { register() }

    }

    private fun register() {
        val name = binding.textFieldName.text.toString()
        val lastName = binding.textFieldLastname.text.toString()
        val fon = binding.textFieldPhone.text.toString()
        val email = binding.textFieldEmail.text.toString()
        val password = binding.textFieldPassword.text.toString()
        val confirmPassword = binding.textFieldConfirmPassword.text.toString()

        if (isValidForm(name, lastName, fon, email, password, confirmPassword)) {
            authProvider.register(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val driver = Driver(
                        id = authProvider.getId(),
                        name = name,
                        lastname = lastName,
                        phone = fon,
                        email = email
                    )
                    driverProvider.create(driver).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registro Exitoso",
                                Toast.LENGTH_LONG
                            ).show()
                            goToMap()
                        } else {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Hubo un error almacenando los datos del usuario ${it.exception.toString()}",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.d("FIREBASE", "Error: ${it.exception.toString()}")
                        }
                    }
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registro Fallido ${it.exception.toString()}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("FIREBASE", "Error: ${it.exception.toString()}")
                }
            }
        }
    }

    private fun goToMap() {
        val i = Intent(this, MapActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(i)
    }

    private fun isValidForm(
        name: String,
        lastName: String,
        fon: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(this, "Ingresa tus Nombres", Toast.LENGTH_SHORT).show()
            return false
        }
        if (lastName.isEmpty()) {
            Toast.makeText(this, "Ingresa tus Apellidos", Toast.LENGTH_SHORT).show()
            return false
        }
        if (fon.isEmpty()) {
            Toast.makeText(this, "Ingresa tu teléfono", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty()) {
            Toast.makeText(this, "Ingresa tu correo", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Ingresa tu contraseña", Toast.LENGTH_SHORT).show()
            return false
        }
        if (confirmPassword.isEmpty()) {
            Toast.makeText(this, "Ingresa la confirmacion de tu contraseña", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(
                this,
                "La contraseña debe tener al menos 6 caracteres",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }


    private fun goToLogin() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }
}