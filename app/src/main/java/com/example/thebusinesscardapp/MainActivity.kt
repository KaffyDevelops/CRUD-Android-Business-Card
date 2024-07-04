package com.example.thebusinesscardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thebusinesscardapp.ui.theme.TheBusinessCardAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: BusinessCardDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = BusinessCardDatabaseHelper(this)

        setContent {
            TheBusinessCardAppTheme {
                TheBusinessCardApp(dbHelper)
            }
        }
    }
}

@Composable
fun TheBusinessCardApp(dbHelper: BusinessCardDatabaseHelper) {
    var businessCards by remember { mutableStateOf(listOf<BusinessCard>()) }
    var selectedCard by remember { mutableStateOf<BusinessCard?>(null) }
    var isEditing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        businessCards = dbHelper.getAllBusinessCards()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        businessCards.forEach { card ->
            BusinessCardComposable(
                card = card,
                onEdit = {
                    selectedCard = card
                    isEditing = true
                },
                onDelete = {
                    dbHelper.deleteBusinessCard(card)
                    businessCards = dbHelper.getAllBusinessCards()
                }
            )
        }

        if (isEditing) {
            EditBusinessCardForm(
                dbHelper = dbHelper,
                card = selectedCard!!,
                onUpdate = {
                    businessCards = dbHelper.getAllBusinessCards()
                    isEditing = false
                    selectedCard = null
                }
            )
        } else {
            AddBusinessCardForm(dbHelper) {
                businessCards = dbHelper.getAllBusinessCards()
            }
        }
    }
}

@Composable
fun BusinessCardComposable(card: BusinessCard, onEdit: () -> Unit, onDelete: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_android_black_24dp),
            contentDescription = "Android Logo",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = card.name, fontSize = 24.sp, color = Color.Black)
        Text(text = card.title, fontSize = 16.sp, color = Color.Gray)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.baseline_add_ic_call_24), contentDescription = "Phone")
            Text(text = card.phone, fontSize = 16.sp, color = Color.Black)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.baseline_attach_email_24), contentDescription = "Email")
            Text(text = card.email, fontSize = 16.sp, color = Color.Black)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.baseline_add_location_alt_24), contentDescription = "Location")
            Text(text = card.location, fontSize = 16.sp, color = Color.Black)
        }
        Row {
            Button(onClick = onEdit) {
                Text("Edit")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onDelete) {
                Text("Delete")
            }
        }
    }
}

@Composable
fun AddBusinessCardForm(dbHelper: BusinessCardDatabaseHelper, onAdd: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") }
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") }
        )
        Button(onClick = {
            coroutineScope.launch {
                dbHelper.insertBusinessCard(
                    BusinessCard(
                        name = name,
                        title = title,
                        phone = phone,
                        email = email,
                        location = location
                    )
                )
                onAdd()
            }
        }) {
            Text("Add Business Card")
        }
    }
}

@Composable
fun EditBusinessCardForm(dbHelper: BusinessCardDatabaseHelper, card: BusinessCard, onUpdate: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var name by remember { mutableStateOf(card.name) }
    var title by remember { mutableStateOf(card.title) }
    var phone by remember { mutableStateOf(card.phone) }
    var email by remember { mutableStateOf(card.email) }
    var location by remember { mutableStateOf(card.location) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone") }
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") }
        )
        Button(onClick = {
            coroutineScope.launch {
                dbHelper.updateBusinessCard(
                    BusinessCard(
                        id = card.id,
                        name = name,
                        title = title,
                        phone = phone,
                        email = email,
                        location = location
                    )
                )
                onUpdate()
            }
        }) {
            Text("Update Business Card")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TheBusinessCardAppTheme {
        BusinessCardComposable(
            card = BusinessCard(
                id = 1,
                name = "John Doe",
                title = "Software Engineer",
                phone = "123-456-7890",
                email = "john.doe@example.com",
                location = "New York, USA"
            ),
            onEdit = {},
            onDelete = {}
        )
    }
}
