package com.example.dogchallenge.ui.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import com.example.dogchallenge.R
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun ErrorDialog(message: String, title: String, dismissText: String) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(message)
            },
            confirmButton = {
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    }) {
                    Text(dismissText)
                }
            }
        )
    }
}

@Composable
fun LoadingView() {
    Dialog(
        onDismissRequest = { },
        DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp)
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun TextDetailWidget(detailName: String, detailValue: String) {
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Blue)) {
                append("$detailName ")
            }
            append(detailValue)
        },
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        style = TextStyle(fontSize = 15.sp)
    )
}

@ExperimentalCoilApi
@SuppressLint("UnrememberedMutableState")
@Composable
fun LoadPicture(url: String?) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .build()

    CompositionLocalProvider(LocalImageLoader provides imageLoader) {
        val painter =
            if (url == null) painterResource(R.drawable.placeholder) else rememberImagePainter(
                url
            )
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RectangleShape),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun TextBox(elements: List<String>, color: Color, paddingStart: Dp = 0.dp) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = paddingStart)
            .wrapContentHeight(),
        mainAxisSpacing = 5.dp,
        crossAxisSpacing = 5.dp,
    ) {
        elements.forEach {
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(5.dp))
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Text(color = Color.White, text = it, modifier = Modifier.padding(6.dp))
            }
        }
    }
}