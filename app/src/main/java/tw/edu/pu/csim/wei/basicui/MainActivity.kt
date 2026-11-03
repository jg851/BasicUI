package tw.edu.pu.csim.wei.basicui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
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
        TextFieldExample(modifier = Modifier.fillMaxSize())
    }
}

@Composable
fun SimpleVideoPlayer(
    @RawRes videoRes: Int
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    // 當切換影片時自動暫停舊影片，並載入新影片播放
    LaunchedEffect(videoRes) {
        exoPlayer.pause()
        val uri = "android.resource://${context.packageName}/$videoRes"
        val mediaItem = MediaItem.fromUri(uri)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    // 離開畫面時暫停並釋放資源
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.pause()
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
    )
}

@Composable
fun LazyColumnExample(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .alpha(0.0f), // 底層透明列表
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
    var currentVideoRes by remember { mutableStateOf(R.raw.love) }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0x8AE0BBE4))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        gestureMessage = "短按"
                        Toast.makeText(context, "觸發了短按！", Toast.LENGTH_SHORT).show()
                    },
                    onDoubleTap = { gestureMessage = "連點兩下" },
                    onLongPress = { gestureMessage = "長按" }
                )
            },
        verticalArrangement = Arrangement.SpaceBetween, // 讓版面上下舒展開來，背景不擁擠
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 上半部：影片與作者資訊
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            SimpleVideoPlayer(videoRes = currentVideoRes)

            Spacer(modifier = Modifier.height(8.dp))
            Text("行動應用軟體開發：基本元件")
            Text("作者：魏仁楷 ")

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = gestureMessage)

            Spacer(modifier = Modifier.height(6.dp))
            RowExample()
        }

        // 中間：切換影片按鈕（按下後影片會跟著同步切換並暫停舊影片）
        Button(
            onClick = {
                currentVideoRes = if (currentVideoRes == R.raw.love) {
                    R.raw.handpan
                } else {
                    R.raw.love
                }
                Toast.makeText(context, "已切換影片", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Text(text = if (currentVideoRes == R.raw.love) "切換至 Handpan 影片" else "切換至 Love 影片")
        }

        // 下半部：動物卡片橫向列表（預留底部 TextField 空間）
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp)
        ) {
            LazyRowExample()
        }
    }
}

data class Animal(
    val name: String,
    @DrawableRes val imageRes: Int
)

@Composable
fun LazyRowExample(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val animals = listOf(
        Animal("鴨子", R.drawable.animal0),
        Animal("企鵝", R.drawable.animal1),
        Animal("青蛙", R.drawable.animal2),
        Animal("貓頭鷹", R.drawable.animal3),
        Animal("海豚", R.drawable.animal4),
        Animal("牛", R.drawable.animal5),
        Animal("無尾熊", R.drawable.animal6),
        Animal("獅子", R.drawable.animal7),
        Animal("狐狸", R.drawable.animal8),
        Animal("小雞", R.drawable.animal9)
    )

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(animals) { index, animal ->
            Card(
                modifier = Modifier
                    .width(150.dp)
                    .height(180.dp)
                    .clickable {
                        Toast.makeText(context, "你點擊了第 $index 個動物：${animal.name}", Toast.LENGTH_SHORT).show()
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD0BCFF)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = animal.imageRes),
                        contentDescription = animal.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "$index: ${animal.name}")
                }
            }
        }
    }
}

@Composable
fun TextFieldExample(modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("請輸入文字") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = if (inputText.isBlank()) "尚未輸入內容" else "您輸入的是：$inputText"
        )
    }
}

@Composable
fun RowExample(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.android),
            contentDescription = "Android icon",
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.Yellow),
            alpha = 0.6f,
        )

        Spacer(modifier = Modifier.width(16.dp))

        Image(
            painter = painterResource(id = R.drawable.compose),
            contentDescription = "Compose icon",
            modifier = Modifier.size(60.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    BasicUITheme {
        Main()
    }
}