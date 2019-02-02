package com.example.android.chatapp2.Model

class Users {

    var name: String=""
    var image: String=""
    var id: String=""

    constructor() {}

    constructor(name: String, image: String, id: String) {
        this.name = name
        this.image = image
        this.id = id
    }
}
