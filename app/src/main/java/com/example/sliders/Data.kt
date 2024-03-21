package com.example.sliders

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("min")
    var min: Int,
    @SerializedName("max")
    var max: Int,
    @SerializedName("points")
    var points: List<Point>,
    @SerializedName("items")
    var items: List<Item>
)

data class Item (
    @SerializedName("name")
    var name: String,
    @SerializedName("multiple")
    var multiple: Int,
    @SerializedName("eachQtyValue")
    var eachQtyValue: Int
)

data class Point(
    @SerializedName("value")
    val value: Int
)