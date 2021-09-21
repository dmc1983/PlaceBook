package com.raywenderlich.placebook.model

@Entity
data class Bookmark(@PrimaryKey(autoGenerate = true)
                    var id: Long? = null,
                    var placeId:String? = null,
                    var name: String = "",
                    var address: String = "",
                    var latitude: Double = 0.0,
                    var longitude: Double = 0.0,
                    var phone: String = "")

annotation class Entity

annotation class PrimaryKey(val autoGenerate: Boolean)
