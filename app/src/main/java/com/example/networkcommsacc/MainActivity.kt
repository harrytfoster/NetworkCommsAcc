package com.example.networkcommsacc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.networkcommsacc.ui.theme.NetworkCommsAccTheme
import com.github.kittinunf.fuel.core.Parameters
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson // for JSON - uncomment when needed
//import com.github.kittinunf.fuel.gson.responseObject // for GSON - uncomment when needed
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import org.json.JSONArray

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NetworkCommsAccTheme {
                FuelExampleJson()
                //FuelExamplePost()
            }
        }
    }
}

@Composable
fun FuelExampleJson() { // GET DATA FROM WEB
    var responseText by remember { mutableStateOf("") } // Create variable for response given
    Column {
        Button( onClick = {
            var url = "http://10.0.2.2:3000/artist/Blur" // URL to get data from (local server start with 10.0.2.2)
            url.httpGet().responseJson { request, response, result -> // Get request starts
                when(result) { // Checks if response is given
                    is Result.Success -> {
                        val jsonArray = result.get().array()
                        var str = "" // Create string to put data into
                        for(i in 0 until jsonArray.length()) {
                            val curObj = jsonArray.getJSONObject(i)
                            str += "Title: ${curObj.getString("title")} Year: ${curObj.getString("year")}\n" // Choose which items to retrieve
                        }
                        responseText = str // Make response a string
                    }

                    is Result.Failure -> {
                        responseText = "ERROR ${result.error.message}" // Errors can be seen
                    }
                }
            }
        }) {
            Text("Get data from Web (Fuel/JSON)!")
        }
        Text(responseText) // Print the data retrieved
    }
}

@Composable
fun FuelExamplePost() {
    var responseText by remember { mutableStateOf("") }
    Column {
        Button( onClick = {
            val url = "http://10.0.2.2:3000/song/create"
            val postData = listOf("id" to "1010", "title" to "New Blur Song", "artist" to "Blur", "year" to 1998, "downloads" to 0, "price" to 0.50, "quantity" to 10000)
            url.httpPost(postData).response { request, response, result ->
                when (result) {
                    is Result.Success -> {
                        responseText = result.get().decodeToString()
                    }

                    is Result.Failure -> {
                        responseText = "ERROR ${result.error.message}"
                    }
                }
            }
        }) {
            Text("POST data to server!")
        }
        Text(responseText)
    }
}