package com.example.chat_app.model

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.android.parcel.Parcelize
import java.io.File


data class data_User(var name:String,var tag:String,var image:String)


@Parcelize
data class frndslist(var name:String,var image:String,var recevierid:String) :Parcelable

data class maindata(var reciverid:String,var name: String,var image: String,var tag: String)

data class dataForChat(var message:String,var image:String,var reciverid: String)