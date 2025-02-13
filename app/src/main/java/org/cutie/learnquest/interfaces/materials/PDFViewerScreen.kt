package org.cutie.learnquest.interfaces.materials

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfViewerScreen(
    pdfBytes: ByteArray?,
    isLoading: Boolean,
    error: String?,
    fileName: String,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var pdfFile by remember { mutableStateOf<File?>(null) }
    var pageCount by remember { mutableStateOf(0) }
    var pdfPages by remember { mutableStateOf<List<Bitmap>>(emptyList()) }

    val listState = rememberLazyListState() // For scrolling
    val isDragging = remember { mutableStateOf(false) } // Track drag state

    // Load PDF pages
    LaunchedEffect(pdfBytes) {
        if (pdfBytes != null) {
            withContext(Dispatchers.IO) {
                try {
                    val file = File(context.cacheDir, "temp_$fileName")
                    FileOutputStream(file).use { it.write(pdfBytes) }
                    pdfFile = file

                    val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
                    val renderer = PdfRenderer(fileDescriptor)
                    pageCount = renderer.pageCount

                    val pages = mutableListOf<Bitmap>()

                    for (i in 0 until pageCount) {
                        val page = renderer.openPage(i)
                        val bitmap = Bitmap.createBitmap(page.width * 2, page.height * 2, Bitmap.Config.ARGB_8888)
                        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                        pages.add(bitmap)
                        page.close()
                    }
                    pdfPages = pages

                    renderer.close()
                    fileDescriptor.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    // Cleanup effect
    DisposableEffect(Unit) {
        onDispose {
            pdfPages.forEach { it.recycle() }
            pdfFile?.delete()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFD780))
    ) {
        // Top Bar
        TopAppBar(
            title = { Text(text = fileName) },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black
            )
        )

        // Content
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> CircularProgressIndicator(color = Color.Black)
                error != null -> Text(text = error, color = Color.Red, style = MaterialTheme.typography.bodyLarge)
                pdfPages.isNotEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    // Detect dragging gestures to scroll content
                                    detectDragGestures { _, dragAmount ->
                                        scope.launch {
                                            // Scroll based on drag amount
                                            listState.animateScrollBy(-dragAmount.y)
                                        }
                                    }
                                }
                        ) {
                            items(pdfPages.size) { index ->
                                var scale by remember { mutableStateOf(1f) }
                                var offset by remember { mutableStateOf(0f to 0f) }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color.White)
                                        .padding(vertical = 8.dp) // Adds spacing between pages
                                        .pointerInput(Unit) {
                                            detectTransformGestures { centroid, pan, zoom, _ ->
                                                val newScale = (scale * zoom).coerceIn(1f, 3f)

                                                if (newScale != scale) {
                                                    val focalX = centroid.x - offset.first
                                                    val focalY = centroid.y - offset.second

                                                    val newX = offset.first + (focalX - focalX * zoom)
                                                    val newY = offset.second + (focalY - focalY * zoom)

                                                    scale = newScale
                                                    offset = newX to newY
                                                } else {
                                                    val maxTranslationX = ((scale - 1f) * 200f).coerceAtLeast(0f)
                                                    val maxTranslationY = ((scale - 1f) * 300f).coerceAtLeast(0f)

                                                    val newX = (offset.first + pan.x).coerceIn(-maxTranslationX, maxTranslationX)
                                                    val newY = (offset.second + pan.y).coerceIn(-maxTranslationY, maxTranslationY)

                                                    offset = newX to newY
                                                }
                                            }
                                        }
                                ) {
                                    Image(
                                        bitmap = pdfPages[index].asImageBitmap(),
                                        contentDescription = "Page ${index + 1}",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .graphicsLayer(
                                                scaleX = scale,
                                                scaleY = scale,
                                                translationX = offset.first,
                                                translationY = offset.second
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}