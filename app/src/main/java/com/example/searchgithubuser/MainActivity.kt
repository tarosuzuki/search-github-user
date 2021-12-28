package com.example.searchgithubuser

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.searchgithubuser.ui.theme.SearchGithubUsersTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchGithubUsersTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    SearchUsersScreen()
                    //Greeting("Android")
                }
            }
        }
        //apiTest()
    }
}

fun apiTest() {
    val gitHubService = CloudGitHubService()
    runBlocking {
        //gitHubService.getUsers("taro in:login")
        //gitHubService.getUserInfo("taro-0")
        gitHubService.getRepositoryInfo("tarosuzuki")
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SearchGithubUsersTheme {
        Greeting("Android")
    }
}
