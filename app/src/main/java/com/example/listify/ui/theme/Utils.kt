package com.example.listify.ui.theme


import androidx.annotation.DrawableRes
import com.example.listify.R

object Utils {
    val category = listOf(
        Category(title = "Drinks", resId = R.drawable.drink, id = 0),
        Category(title = "Vegetable", resId = R.drawable.vegetable, id = 1),
        Category(title = "Fruits", resId = R.drawable.basket, id = 2),
        Category(title = "Cleaning", resId = R.drawable.mop, id = 3),
        Category(title = "Electronic", resId = R.drawable.plug, id = 4),
        Category(title = "None", resId =R.drawable.baseline_not_interested_24 ,id = 10001)
    )
}

data class Category(
    @DrawableRes val resId: Int = -1,
    val title: String = "",
    val id: Int = -1,
)