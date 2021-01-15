package com.example.course_project.dto

import java.util.*

class ProductDto(var name: String, var price: Int, var available: Boolean, var description: String, var type: String, image: String?, nameBuilding: String) {
    var image: ByteArray
    var nameBuilding: String

    init {
        this.image = Base64.getDecoder().decode(image)
        this.nameBuilding = nameBuilding
    }
}