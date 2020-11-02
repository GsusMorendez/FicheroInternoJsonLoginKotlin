package com.example.morenohernandezjesusjsonfichero.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.morenohernandezjesusjsonfichero.MainActivity
import com.example.morenohernandezjesusjsonfichero.R
import com.example.morenohernandezjesusjsonfichero.model.Usuario
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader

class RegistroActivity : AppCompatActivity() {

    lateinit var editTextUsuario: EditText
    lateinit var editTextConta: EditText
    lateinit var editTextNombre: EditText
    lateinit var editTextApellidos: EditText
    var nombreFichero = "ficheroInternoUsuarios.txt"
    val gson = Gson()
    lateinit var buttonBorrar: Button
    var alguienLogeado: Int = 0
    var vengode: String? = null
    lateinit var miUsuarioLogeado: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        editTextUsuario = findViewById<EditText>(R.id.editTextUsuario)
        editTextConta = findViewById<EditText>(R.id.editTextContra)
        editTextNombre = findViewById<EditText>(R.id.editTextNombre)
        editTextApellidos = findViewById<EditText>(R.id.editTextApellidos)

        buttonBorrar = findViewById<Button>(R.id.buttonBorrar)
        var buttonAceptar = findViewById<Button>(R.id.buttonAceptar)
        var buttonActualizar = findViewById<Button>(R.id.buttonActualizar)

        var titulo = findViewById<TextView>(R.id.textViewTitulo)

        buttonBorrar.isEnabled = traerTodosUsuarios() != null

        alguienLogeado = intent.getIntExtra("alguienLogeado", 0)
        vengode = intent.getStringExtra("vengode")

        if (alguienLogeado == 1) {
            miUsuarioLogeado = intent.getSerializableExtra("usuario") as Usuario
        }

        if (vengode == "informacion") {

            titulo.text = "Información"

            buttonAceptar.visibility = View.GONE;
            buttonActualizar.visibility = View.VISIBLE;
            buttonBorrar.visibility = View.GONE;

            editTextUsuario.setText(miUsuarioLogeado.usuario)
            editTextConta.setText(miUsuarioLogeado.contra)
            editTextNombre.setText(miUsuarioLogeado.nombre)
            editTextApellidos.setText(miUsuarioLogeado.apellidos)

        } else {
            titulo.text = "Registra tus datos"
            buttonActualizar.visibility = View.GONE;
            buttonAceptar.visibility = View.VISIBLE;
        }
    }

    fun RecogerRegistro(view: View) {
        var userName = editTextUsuario.text.toString()
        var contra = editTextConta.text.toString()
        var nombre = editTextNombre.text.toString()
        var apellidos = editTextApellidos.text.toString()

        if (userName == "" || contra == "" || nombre == "" || apellidos == "") {

            Toast.makeText(
                this,
                "Debe completar todos los campos del formulario",
                Toast.LENGTH_LONG
            ).show()

        } else {
            var miUsuario = Usuario(userName, contra, nombre, apellidos)

            //Ya tenemos el usuario creado, ahora lo pintaremos en el fichero, el main tendra un metodo que mirara en el fichero cada vez que se llame a oncreate y si no está vacío activará el login
            pintarUsuarioEnFchero(miUsuario)

            //le devolvemos al main una variable para que sepa si activar o no el login
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("alguienLogeado", alguienLogeado)
            intent.putExtra("vengoDe", "registro")

            startActivity(intent)

        }
    }


    fun pintarUsuarioEnFchero(miUsuario: Usuario) {

        val listUsuarios: ArrayList<Usuario>? = if (traerTodosUsuarios() != null) {
            traerTodosUsuarios()
        } else {
            ArrayList()
        }
        var isRepe = existeUsuario(listUsuarios, miUsuario)
        if (isRepe == -1) {
            listUsuarios?.add(miUsuario)
        } else {
            var userAcambiar = listUsuarios?.get(isRepe)
            userAcambiar?.nombre = miUsuario.nombre
            userAcambiar?.apellidos = miUsuario.apellidos
            userAcambiar?.contra = miUsuario.contra
        }
        var usuarioJsonString = gson.toJson(listUsuarios)
        var fileOutput = openFileOutput(nombreFichero, Context.MODE_PRIVATE)

        fileOutput.write(usuarioJsonString.toByteArray())
        fileOutput.close()
    }

    fun traerTodosUsuarios(): ArrayList<Usuario>? {

        var bufferedReader = BufferedReader(InputStreamReader(openFileInput(nombreFichero)))
        var textoLeido = bufferedReader.readLine()
        bufferedReader.close()

        var listUsuarios: ArrayList<Usuario>?

        listUsuarios = if (textoLeido != "") {
            gson.fromJson(textoLeido, object : TypeToken<List<Usuario?>?>() {}.type)
        } else {
            ArrayList()
        }
        return listUsuarios

    }

    fun existeUsuario(listUsuarios: ArrayList<Usuario>?, miUsuario: Usuario): Int {
        //declaramos una variable que nos informa de si está repetido y de la posición del int (-1 = no esta repetido= el resto = al indice donde se repite
        var isRepe = -1

        if (listUsuarios != null) {
            for (i in 0 until listUsuarios.size) {
                var esteUsuario = listUsuarios[i]

                if (esteUsuario.usuario == miUsuario.usuario) {
                    isRepe = i
                }
            }
        }

        return isRepe
    }


    fun cancelarRegistro(view: View) {
        var intent = Intent(this, MainActivity::class.java)
        intent.putExtra("alguienLogeado", alguienLogeado)
        if (alguienLogeado == 1) {
            intent.putExtra("usuario", miUsuarioLogeado)
        }
        startActivity(intent)
        //finish()
    }

    fun vaciarFichero(view: View) {
        var fileOutput = openFileOutput(nombreFichero, Context.MODE_PRIVATE)
        fileOutput.write("".toByteArray())
        fileOutput.close()
        buttonBorrar.isEnabled = false
        alguienLogeado = 0
    }

    override fun onBackPressed() {
        var intent = Intent(this, MainActivity::class.java)
        //intent.putExtra("hayUsuarios", traerTodosUsuarios() != null)
        intent.putExtra("alguienLogeado", alguienLogeado)
        intent.putExtra("usuario", miUsuarioLogeado)

        startActivity(intent)
    }

    fun actualizarUsuarioLogeado(view: View) {

        var listUsuarios: ArrayList<Usuario>? = traerTodosUsuarios()

        var userName = editTextUsuario.text.toString()
        var contra = editTextConta.text.toString()
        var nombre = editTextNombre.text.toString()
        var apellidos = editTextApellidos.text.toString()

        if (userName == "" || contra == "" || nombre == "" || apellidos == "") {

            Toast.makeText(
                this,
                "Debe completar todos los campos del formulario",
                Toast.LENGTH_LONG
            ).show()

        } else {
            for (i in 0 until listUsuarios!!.size) {
                var esteUsuario = listUsuarios[i]
                if (esteUsuario.usuario == miUsuarioLogeado.usuario) {
                    esteUsuario.usuario = userName
                    esteUsuario.contra = contra
                    esteUsuario.nombre = nombre
                    esteUsuario.apellidos = apellidos
                }
            }
            var usuarioJsonString = gson.toJson(listUsuarios)
            var fileOutput = openFileOutput(nombreFichero, Context.MODE_PRIVATE)
            fileOutput.write(usuarioJsonString.toByteArray())
            fileOutput.close()

            //Como hemos cambiado todos los datos del usuario le echamos (alguienLogeado =0) y para entrar tendría que volver a loguearse

            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("alguienLogeado", 0)
            startActivity(intent)
        }
    }


}