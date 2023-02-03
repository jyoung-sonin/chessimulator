package com.muto.chessimulator

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.floor

private const val CELL_COUNT = 8

@Composable
fun GameUi() {
    val board = remember { mutableStateOf(Array(CELL_COUNT) { BooleanArray(CELL_COUNT) }) }

    board.value[1][1] = true

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.Gray),
        verticalArrangement = Arrangement.Center
    ) {
        Canvas(
            modifier = Modifier
                .aspectRatio(1f)
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        Log.d(
                            "PointerInput",
                            "offset=$it cellX=${floor(it.x % CELL_COUNT).toInt()} cellY=${floor(it.y % CELL_COUNT).toInt()}"
                        )

                        board.value[floor(it.y % CELL_COUNT).toInt()][floor(it.x % CELL_COUNT).toInt()] =
                            true
                    })
                }
        ) {
            chessBoard(board.value)
        }
    }
}

private fun DrawScope.chessBoard(board: Array<BooleanArray>) {
    val width = size.width
    val cellSize = width / CELL_COUNT

    for (i in 0 until CELL_COUNT) {
        val y = cellSize * i
        var x: Float

        for (j in 0 until CELL_COUNT) {
            x = cellSize * j

            val rect = Rect(Offset(x, y), Size(cellSize, cellSize))

            drawRect(
                color = when (board[i][j]) {
                    true -> Color.Red
                    false -> when ((i + j) % 2 == 0) {
                        true -> Color.White
                        false -> Color.Black
                    }
                },
                size = rect.size,
                topLeft = rect.topLeft
            )
        }
    }
}

@Preview
@Composable
fun Preview_GameUi() {
    GameUi()
}