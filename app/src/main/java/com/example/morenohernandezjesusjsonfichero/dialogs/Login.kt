package com.example.morenohernandezjesusjsonfichero.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.morenohernandezjesusjsonfichero.MainActivity
import com.example.morenohernandezjesusjsonfichero.R
import com.example.morenohernandezjesusjsonfichero.model.Usuario

class Login(private val listaUsers: ArrayList<Usuario>?, private val mainActivity: MainActivity) :
    DialogFragment() {

    lateinit var usuarioLog: EditText
    lateinit var contraLog: EditText


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var viewDialog = inflater.inflate(R.layout.dialog_login, container, false)

        usuarioLog = viewDialog.findViewById<EditText>(R.id.loginUser)
        contraLog = viewDialog.findViewById<EditText>(R.id.loginContra)

        var botonLogin = viewDialog.findViewById<Button>(R.id.buttonLogin)
        botonLogin.setOnClickListener { view -> login(view) }


        return viewDialog
    }


    fun login(view: View) {

        var uslog = usuarioLog.text.toString()
        var contlog = contraLog.text.toString()

        var existeUser = false

        var esteUser: Usuario? = null

        if (listaUsers != null) {
            for (i in 0 until listaUsers.size) {
                esteUser = listaUsers[i]
                if (esteUser.usuario == uslog && esteUser.contra == contlog) {
                    existeUser = true
                    //esteUser.isLogeado = true
                    mainActivity.miUsuarioLogeado = esteUser
                }
            }
            if (existeUser) {
                if (esteUser != null) {
                    mainActivity.activarInfo(esteUser)
                }
                dismiss()
            } else {
                Toast.makeText(this.activity, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG)
                    .show()
            }
        } else {
            Toast.makeText(this.activity, "No hay ningun usuario registrado", Toast.LENGTH_LONG)
                .show()
        }
    }
}








