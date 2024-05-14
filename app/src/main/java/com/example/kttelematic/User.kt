package com.example.kttelematic

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User(
    @PrimaryKey var username: String = "",
    var password: String = "",
    var mobileno: String =""
)