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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.floor

private const val CELL_COUNT = 8

data class Coordinate(val x: Int, val y: Int)

data class Tile(val coordinate: Coordinate, val color: Color)

typealias Board = List<List<Tile>>

@Composable
fun GameUi() {
    val board = remember { mutableStateOf(generateEmptyBoard()) }
    val selectedTileCoordinate = remember { mutableStateOf(Coordinate(-1, -1)) }

    val screenWidth = LocalConfiguration.current.screenWidthDp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        verticalArrangement = Arrangement.Center
    ) {
        Canvas(
            modifier = Modifier
                .aspectRatio(1f)
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { offset ->
                            Log.d(
                                "PointerInput",
                                "offset=$offset cellX=${floor(offset.x % CELL_COUNT).toInt()} cellY=${
                                    floor(
                                        offset.y % CELL_COUNT
                                    ).toInt()
                                }"
                            )

                            val boardWidthPx = screenWidth * density
                            val cellWidth = boardWidthPx / CELL_COUNT

                            val xCell = (offset.x / cellWidth).toInt()
                            val yCell = (offset.y / cellWidth).toInt()

                            selectedTileCoordinate.value = Coordinate(xCell, yCell)

                            board.value = getBoardWithSelectedTile(board.value, selectedTileCoordinate.value)
                        }
                    )
                }
        ) {
            chessBoard(board.value)
        }
    }
}

private fun getBoardWithSelectedTile(
    board: Board,
    selectedTileCoordinate: Coordinate
): Board {
    val emptyBoard = generateEmptyBoard()
    return board.map { xTiles ->
        xTiles.map { tile ->
            if (tile.coordinate == selectedTileCoordinate && tile.color == Color.Red) {
                Tile(tile.coordinate, emptyBoard[tile.coordinate.y][tile.coordinate.x].color)
            } else if (tile.coordinate == selectedTileCoordinate) {
                Tile(tile.coordinate, Color.Red)
            } else {
                emptyBoard[tile.coordinate.y][tile.coordinate.x]
            }
        }
    }
}

private fun generateEmptyBoard(): Board {
    return List(CELL_COUNT) { y ->
        List(CELL_COUNT) { x ->
            val tileColor = if (y % 2 == 0) {
                if (x % 2 == 0) Color.White else Color.Black
            } else {
                if (x % 2 != 0) Color.White else Color.Black
            }

            Tile(Coordinate(x, y), tileColor)
        }
    }
}

private fun DrawScope.chessBoard(board: List<List<Tile>>) {
    val width = size.width
    val cellSize = width / CELL_COUNT

    for (xTiles in board) {
        val y = xTiles.first().coordinate.y * cellSize
        var x: Float

        for (tile in xTiles) {
            x = tile.coordinate.x * cellSize

            val rect = Rect(Offset(x, y), Size(cellSize, cellSize))

            drawRect(
                color = tile.color,
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