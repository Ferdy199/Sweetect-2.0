package com.daftech.sweetectapp.core.data

data class HistoryFirebase(
    var id: String? = null,
    var email: String? = null,
    var dataHistory: List<FoodData>? = null
)