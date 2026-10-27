package tw.edu.pu.csim.wei.basicui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tw.edu.pu.csim.wei.basicui.ui.theme.BasicUITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BasicUITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Main(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Main(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        LazyColumnExample(modifier = Modifier.fillMaxSize())
        ColumnExample(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun LazyColumnExample(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(text = "Header")
        }

        items(50) { index ->
            Text(text = "$index")
        }

        item {
            Text(text = "Footer")
        }
    }
}

@Composable
fun ColumnExample(modifier: Modifier = Modifier) {
    var gestureMessage by remember { mutableStateOf("請嘗試：短按、連點兩下或長按") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0x8AE0BBE4))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { gestureMessage = "短按 (Tap)" },
                    onDoubleTap = { gestureMessage = "連點兩下 (Double Tap)" },
                    onLongPress = { gestureMessage = "長按 (Long Press)" }
                )
            },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("行動應用軟體開發：基本元件")
        Text("作者：魏仁楷")

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = gestureMessage)
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    BasicUITheme {
        Main()
    }
}