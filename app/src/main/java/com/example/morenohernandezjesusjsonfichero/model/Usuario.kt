package com.example.morenohernandezjesusjsonfichero.model

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

class Usuario(user: String, pasword: String, name: String, preName: String) : Serializable{
    var usuario: String = user
    var contra: String = pasword
    var nombre: String = name
    var apellidos: String = preName
    //var isLogeado: Boolean = isLogeado


    /*fun getBundle(): Bundle {
        var bundle = Bundle()
        bundle.putCharSequence("usuario", usuario)
        bundle.putCharSequence("password", contra)
        bundle.putCharSequence("nombre", nombre)
        bundle.putCharSequence("apellidos", apellidos)

        return bundle
    }

    fun setBundle(bundle: Bundle) {
        usuario = bundle.getString("usuario", "")
        contra = bundle.getString("contra", "")
        nombre = bundle.getString("nombre", "")
        apellidos = bundle.getString("apellidos", "")
    }*/

}