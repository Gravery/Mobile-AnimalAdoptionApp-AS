package com.example.adoptionapp.ui.home

import android.os.Parcel
import android.os.Parcelable

class Animal(var id: String = "", var image: String = "", var name: String = "", var description: String = "",
             var breed: String = "", var age: String = "", var type: String = "", var contact: String = "", var user: String = "") :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(image)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(breed)
        parcel.writeString(age)
        parcel.writeString(type)
        parcel.writeString(contact)
        parcel.writeString(user)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Animal> {
        override fun createFromParcel(parcel: Parcel): Animal {
            return Animal(parcel)
        }

        override fun newArray(size: Int): Array<Animal?> {
            return arrayOfNulls(size)
        }
    }
}
