package com.example.myapplication

class Contact {

    var id : Int = 0
    var name : String = ""
    var age : Int = 0
    var phoneNumber : String = ""
    var address : String = ""
    var mail : String = ""

    constructor(id: Int, name : String, age : Int, phoneNumber : String, address : String, mail : String)
    {
        this.id = id
        this.name = name
        this.age = age
        this.phoneNumber = phoneNumber
        this.address = address
        this.mail = mail
    }
}

data class Message(
    val id: Int,
    val convId: Int,
    val message: String,
    val isSent: Boolean,
    val timestamp: String
)