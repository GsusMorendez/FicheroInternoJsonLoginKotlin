package com.example.morenohernandezjesusjsonfichero

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.morenohernandezjesusjsonfichero.activities.RegistroActivity
import com.example.morenohernandezjesusjsonfichero.dialogs.Login
import com.example.morenohernandezjesusjsonfichero.model.Usuario
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    val REQUEST_CODE_REGISTRO: Int = 10;
    lateinit var buttonLogin: Button
    lateinit var buttonInformacion: Button
    lateinit var miUsuarioLogeado: Usuario
    lateinit var syso: EditText
    var alguienLogueado = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLogin = findViewById<Button>(R.id.buttonLogin)
        buttonInformacion = findViewById<Button>(R.id.buttonInformacion)

        buttonLogin.isEnabled = comprobarActivacionLogin()
        alguienLogueado = intent.getIntExtra("alguienLogeado", 0)
        var vengoDe = intent.getStringExtra("vengoDe")

        if (alguienLogueado == 1) {
            buttonInformacion.isEnabled = true
            if (vengoDe != "registro") {
                miUsuarioLogeado = intent.getSerializableExtra("usuario") as Usuario
            }
        }
    }

    fun onRegister(view: View) {
        var intentRegistroActivity = Intent(this, RegistroActivity::class.java)
        intentRegistroActivity.putExtra("alguienLogeado", alguienLogueado)
        intentRegistroActivity.putExtra("vengode", "registro")

        if (alguienLogueado == 1) {
            intentRegistroActivity.putExtra("usuario", miUsuarioLogeado)
        }
        startActivity(intentRegistroActivity)
    }

    fun onLogin(view: View) {
        var loginDialog = Login(traerTodosUsuarios(), this)
        loginDialog.show(supportFragmentManager, "logiDialog_Tag")

    }

    fun onInformation(view: View) {
        var intentInformacion = Intent(this, RegistroActivity::class.java)
        intentInformacion.putExtra("alguienLogeado", alguienLogueado)
        intentInformacion.putExtra("vengode", "informacion")
        intentInformacion.putExtra("usuario", miUsuarioLogeado)

        startActivity(intentInformacion)
    }


    fun comprobarActivacionLogin(): Boolean {

        var debeActivarse = false;
        var bufferedReader =
            BufferedReader(InputStreamReader(openFileInput("ficheroInternoUsuarios.txt")))
        var textoLeido = bufferedReader.readLine()
        bufferedReader.close()

        if (textoLeido != null) {
            debeActivarse = true
        }
        return debeActivarse
    }


    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Mensaje!!")
        builder.setMessage("¿Está seguro que quiere salir?")

        builder.setPositiveButton("Si") { dialog, _ -> finish() }
        builder.setNegativeButton("No") { dialog, which -> }

        builder.show()
        //super.onBackPressed()
    }

    fun traerTodosUsuarios(): ArrayList<Usuario>? {

        var bufferedReader =
            BufferedReader(InputStreamReader(openFileInput("ficheroInternoUsuarios.txt")))
        var textoLeido = bufferedReader.readLine()
        bufferedReader.close()
        val gson = Gson()
        var listUsuarios: ArrayList<Usuario>?
        listUsuarios = if (textoLeido != "") {
            gson.fromJson(textoLeido, object : TypeToken<List<Usuario?>?>() {}.type)
        } else {
            ArrayList()
        }
        return listUsuarios
    }

    fun activarInfo(esteUser: Usuario) {
        buttonInformacion.isEnabled = true
        alguienLogueado = 1
        miUsuarioLogeado = esteUser

    }

}