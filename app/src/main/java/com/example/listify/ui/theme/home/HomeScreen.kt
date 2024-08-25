package com.example.listify.ui.theme.home

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ExperimentalMaterialApi
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.rememberDismissState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.listify.data.room.ItemsWithStoreAndList
import com.example.listify.data.room.models.Item
import com.example.listify.ui.theme.Category
import com.example.listify.ui.theme.Utils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.DismissValue






@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    onNavigate:(Int) -> Unit
){

    val homeViewMode = viewModel(modelClass = Home::class.java)
    val state = homeViewMode.state

    Scaffold(
        modifier = Modifier
            .padding(top = 40.dp),
        floatingActionButton = {
            FloatingActionButton(onClick = {onNavigate.invoke(-1)}) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) {
        LazyColumn {

            item {
                LazyRow {

                    items(Utils.category) { category: Category ->
                        ChipSection(
                            iconRes = category.resId,
                            title = category.title,
                            selected = category == state.category,
                        ) {
                            homeViewMode.onCategoryChange(category)
                        }

                    }

                }
            }
            items(state.items){
                /*val dismissedState = rememberDismissState(
                    confirmStateChange = {st->
                        //if (st == DismissValue.DismissedToEnd) {
                            homeViewMode.deleteItems(it.item)
                        //}
                        true
                    }
                )
                
                SwipeToDismiss(
                    state = dismissedState,
                    background = {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Red
                        ) {

                        }
                    },
                    dismissContent = {
                        ShoppingList(
                            item = it,
                            isChecked = it.item.isChecked,
                            onCheckedChange = homeViewMode::onItemCheckedChange
                        ) {
                            onNavigate.invoke(it.item.id)
                        }
                    }
                )*/
                ShoppingList(
                    item = it,
                    isChecked = it.item.isChecked,
                    onCheckedChange = homeViewMode::onItemCheckedChange,
                ) {
                    onNavigate.invoke(it.item.id)
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun ChipSection(
    @DrawableRes iconRes: Int,
    title: String,
    selected: Boolean,
    onChipClick:() -> Unit
){


    Card(

        border = BorderStroke(
            1.dp,
            if(selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier
            .padding(top = 8.dp, start = 8.dp, bottom = 8.dp)
            .selectable(
                selected = selected,
                interactionSource = MutableInteractionSource(),
                indication = rememberRipple(),
                onClick = { onChipClick.invoke() },

                )
            //.background(if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface),
        //contentColor = if(selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
    ) {

        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(8.dp)
        ){

            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp),
                tint = if(selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = if(selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }

}

@Composable
fun ShoppingList(

    item: ItemsWithStoreAndList,
    isChecked: Boolean,
    onCheckedChange: (Item,Boolean) -> Unit,
    onItemClick: () -> Unit
){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onItemClick.invoke()
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = item.item.itemName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.store.storeName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = formatDate(item.item.date),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Light
                )
            }

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = "Qty: ${item.item.qty}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {onCheckedChange.invoke(item.item,it)}
                )
            }
        }
    }
}

fun formatDate(date: Date): String = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault()).format(date)