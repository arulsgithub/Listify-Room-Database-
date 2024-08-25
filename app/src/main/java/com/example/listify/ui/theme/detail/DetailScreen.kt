package com.example.listify.ui.theme.detail

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.listify.ui.theme.Category
import com.example.listify.ui.theme.Utils
import com.example.listify.ui.theme.home.ChipSection
import com.example.listify.ui.theme.home.formatDate
import java.util.Calendar
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailScreen(
    id: Int,
    navigateUp:() -> Unit
){
    val detailViewModel = viewModel<DetailViewModel>(factory = DetailViewModelFactory(id))
    Scaffold {
        DetailEntry(
            state = detailViewModel.state,
            onDateSelected = detailViewModel::onDateChange,
            onItemChange = detailViewModel::onItemChange,
            onStoreChange = detailViewModel::onStoreChange,
            onQtyChange = detailViewModel::onQtyChange,
            onCategoryChange = detailViewModel::onCategoryChange,
            onDialogDismissed = detailViewModel::onScreenDialogDismissed,
            onSaveStore = detailViewModel::addStore,
            updateItem = { detailViewModel.updateShoppingListItem(id) },
            saveItem = detailViewModel::addShoppingListItem,
            navigateUp = navigateUp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailEntry(
    modifier: Modifier = Modifier,
    state: DetailState,
    onDateSelected: (Date) -> Unit,
    onItemChange: (String) -> Unit,
    onStoreChange: (String) -> Unit,
    onQtyChange: (String) -> Unit,
    onCategoryChange: (Category) -> Unit,
    onDialogDismissed: (Boolean) -> Unit,
    onSaveStore: () -> Unit,
    updateItem:() -> Unit,
    saveItem:() -> Unit,
    navigateUp: () -> Unit
){

    var isNewEnabled by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .padding(top = 50.dp)
    ) {
        TextField(
            value = state.item,
            onValueChange = {onItemChange(it)},
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            shape = MaterialTheme.shapes.large,
            label = {Text(text = "Item")}
        )
        Spacer(modifier = Modifier.Companion.size(16.dp))

        Row {
            TextField(
                value = state.store,
                onValueChange = {
                    if(isNewEnabled) onStoreChange.invoke(it)
                },
                modifier = Modifier
                    .weight(1f),
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
                shape = MaterialTheme.shapes.large,
                label = {Text(text = "Store")},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown ,
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            onDialogDismissed.invoke(!state.isScreenDialogDismissed)
                        }
                    )
                }
            )
            if(!state.isScreenDialogDismissed){
                Popup(
                    onDismissRequest = {
                        onDialogDismissed.invoke(!state.isScreenDialogDismissed)
                    }
                ) {
                    Surface(modifier = Modifier.padding(16.dp)) {

                        Column {
                            state.storeList.forEach{
                                Text(
                                    text = it.storeName,
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .clickable {
                                            onStoreChange.invoke(it.storeName)
                                            onDialogDismissed.invoke(!state.isScreenDialogDismissed)
                                        }
                                )
                            }
                        }
                    }
                }
            }
            TextButton(onClick = {
                isNewEnabled = if(isNewEnabled){
                    onSaveStore.invoke()
                    !isNewEnabled
                }else !isNewEnabled
            }) {
                Text(text = if(isNewEnabled) "Save" else "New")
            }
        }
        Spacer(modifier = Modifier.Companion.size(16.dp))
        Row (horizontalArrangement = Arrangement.SpaceEvenly){
            Row (verticalAlignment = Alignment.CenterVertically){

                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = formatDate(state.date))
                Spacer(modifier = Modifier.size(4.dp))
                val xDatePicker = datePickerDialog(
                    context = LocalContext.current,
                    onDateSelected = {date ->
                        onDateSelected.invoke(date)

                    }
                )
                IconButton(
                    onClick = { xDatePicker.show() }
                ) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null)
                }
            }
            TextField(
                value = state.qty,
                onValueChange = {onQtyChange(it)},
                label = { Text(text = "Quantity")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.large
            )
        }


        Spacer(modifier = Modifier.size(16.dp))
        LazyRow {
            items(Utils.category) { category: Category ->
                ChipSection(
                    iconRes = category.resId,
                    title = category.title,
                    selected = category == state.category
                ) {
                    onCategoryChange(category)
                }
            }
        }
        val buttonTitle = if (state.isUpdatingItem) "Update Item" else "Add Item"
        Button(onClick = {
                when(state.isUpdatingItem){
                    true->{
                        updateItem.invoke()
                    }
                    false->{
                        saveItem.invoke()
                    }
                }
                navigateUp.invoke()
            },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.item.isNotEmpty() && state.store.isNotEmpty() && state.qty.isNotEmpty(),
                shape = MaterialTheme.shapes.large
            ) {
                Text(text = buttonTitle)
            }

    }
}

@Composable
fun datePickerDialog(
    context: Context,
    onDateSelected:(Date) -> Unit
): DatePickerDialog {
    val cal = Calendar.getInstance()
    val year = cal.get(Calendar.YEAR)
    val mon = cal.get(Calendar.MONTH)
    val date = cal.get(Calendar.DATE)
    cal.time = Date()

    val xDatePickerDialog = DatePickerDialog(
        context,
        {
            _: DatePicker, xYear: Int, xMon: Int, xDate: Int ->
            val cal = Calendar.getInstance()
            cal.set(xYear,xMon,xDate)
            onDateSelected.invoke(cal.time)
        },year,mon,date
    )
    return xDatePickerDialog
}