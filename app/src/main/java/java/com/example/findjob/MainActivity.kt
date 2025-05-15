package com.example.findjob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.findjob.navigation.AppNavGraph
import com.example.findjob.ui.theme.FindJobTheme
import com.example.findjob.utils.InfoManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @AndroidEntryPoint: Cho phép Hilt inject các dependency vào Activity này
 * - Nếu bạn có một ViewModel dùng @Inject constructor, thì Hilt sẽ tạo và
 * cấp ViewModel cho bạn.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var infoManager: InfoManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FindJobTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController, infoManager = infoManager)
            }
        }
    }
}