package com.example.findjob.ui.screen.employee

import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.findjob.ui.components.EmployeeBottomBar
import com.example.findjob.viewmodel.AIState
import com.example.findjob.viewmodel.AIViewModel
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIScreen(
    navController: NavController,
    viewModel: AIViewModel = hiltViewModel()
) {
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val aiState by viewModel.state.collectAsState()
    val context = LocalContext.current

    // Launcher để chọn file PDF
    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                // Kiểm tra xem có thể đọc file không
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    // Kiểm tra kích thước file
                    val fileSize = inputStream.available()
                    if (fileSize > 5 * 1024 * 1024) { // 5MB
                        viewModel.setError("File size exceeds 5MB limit")
                        return@rememberLauncherForActivityResult
                    }
                    selectedFileUri = uri
                } ?: run {
                    viewModel.setError("Cannot access the selected file")
                }
            } catch (e: IOException) {
                viewModel.setError("Error reading file: ${e.message}")
            } catch (e: Exception) {
                viewModel.setError("Unexpected error: ${e.message}")
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Text(
                text = "REVIEW CV",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1446)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // File selection area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 2.dp,
                        color = Color(0xFF4B3FAE),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (selectedFileUri != null) {
                    Text(
                        text = "Selected file: ${selectedFileUri.toString().split("/").last()}",
                        color = Color(0xFF1A1446)
                    )
                } else {
                    Text(
                        text = "No file selected",
                        color = Color.Gray
                    )
                }
            }

            // Upload status
            when (aiState) {
                is AIState.Success -> {
                    Text(
                        text = "Analysis completed successfully!",
                        color = Color(0xFF4CAF50)
                    )
                }
                is AIState.Error -> {
                    Text(
                        text = (aiState as AIState.Error).message,
                        color = Color.Red
                    )
                }
                else -> {}
            }

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Select file button
                Button(
                    onClick = { pdfPickerLauncher.launch("application/pdf") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4B3FAE)
                    )
                ) {
                    Text("Select PDF")
                }

                // Upload button
                Button(
                    onClick = {
                        selectedFileUri?.let { uri ->
                            viewModel.analyzeCV(uri)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = selectedFileUri != null && aiState !is AIState.Loading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4B3FAE)
                    )
                ) {
                    if (aiState is AIState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Text("Analyze")
                    }
                }
            }

            // Analysis Result
            if (aiState is AIState.Success) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = 120.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                webViewClient = WebViewClient()
                                settings.apply {
                                    javaScriptEnabled = true
                                    domStorageEnabled = true
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize(),
                        update = { webView ->
                            val analysis = (aiState as AIState.Success).analysis
                            
                            // Parse markdown to HTML
                            val parser = Parser.builder().build()
                            val document: Node = parser.parse(analysis.analysis)
                            val renderer = HtmlRenderer.builder().build()
                            val html = renderer.render(document)
                            
                            val styledHtml = """
                                <html>
                                <head>
                                    <meta name="viewport" content="width=device-width, initial-scale=1">
                                    <style>
                                        body {
                                            font-family: Arial, sans-serif;
                                            padding: 16px;
                                            line-height: 1.6;
                                            color: #333;
                                        }
                                        h1, h2, h3, h4, h5, h6 {
                                            color: #1A1446;
                                            margin-top: 24px;
                                            margin-bottom: 16px;
                                        }
                                        h3 {
                                            font-size: 1.2em;
                                            border-bottom: 1px solid #eee;
                                            padding-bottom: 8px;
                                        }
                                        p {
                                            margin-bottom: 16px;
                                        }
                                        ul, ol {
                                            margin-bottom: 16px;
                                            padding-left: 24px;
                                        }
                                        li {
                                            margin-bottom: 8px;
                                        }
                                        code {
                                            background-color: #f5f5f5;
                                            padding: 2px 4px;
                                            border-radius: 4px;
                                            font-family: monospace;
                                        }
                                        pre {
                                            background-color: #f5f5f5;
                                            padding: 16px;
                                            border-radius: 8px;
                                            overflow-x: auto;
                                            white-space: pre-wrap;
                                        }
                                        blockquote {
                                            border-left: 4px solid #4B3FAE;
                                            margin: 16px 0;
                                            padding: 8px 16px;
                                            background-color: #f8f8f8;
                                        }
                                        strong {
                                            font-weight: bold;
                                        }
                                        em {
                                            font-style: italic;
                                        }
                                        a {
                                            color: #4B3FAE;
                                            text-decoration: none;
                                        }
                                        a:hover {
                                            text-decoration: underline;
                                        }
                                    </style>
                                </head>
                                <body>
                                    $html
                                </body>
                                </html>
                            """.trimIndent()
                            webView.loadDataWithBaseURL(null, styledHtml, "text/html", "UTF-8", null)
                        }
                    )
                }
            }

            // Instructions
            if (aiState !is AIState.Success) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF6E5)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Instructions:",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1446)
                        )
                        Text(
                            text = "• Select your CV in PDF format",
                            color = Color(0xFF1A1446)
                        )
                        Text(
                            text = "• Maximum file size: 5MB",
                            color = Color(0xFF1A1446)
                        )
                        Text(
                            text = "• Make sure your CV is up to date",
                            color = Color(0xFF1A1446)
                        )
                    }
                }
            }
        }

        EmployeeBottomBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}