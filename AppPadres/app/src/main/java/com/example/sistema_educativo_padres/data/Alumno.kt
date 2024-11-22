package com.example.sistema_educativo_padres.data

import android.os.Parcel
import android.os.Parcelable

data class Alumno(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val email: String,
    var padre: Int
) : Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }
}
