package com.nothinglondon.sdkdemo.demos.gameoflife

import android.content.Context
import com.nothing.ketchum.GlyphMatrixManager
import com.nothinglondon.sdkdemo.demos.GlyphMatrixService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameOfLifeService : GlyphMatrixService("GameOfLife-Demo") {

    private val backgroundScope = CoroutineScope(Dispatchers.IO)
    private val uiScope = CoroutineScope(Dispatchers.Main)

    private lateinit var gameEngine: GameOfLifeEngine
    private lateinit var renderer: GlyphMatrixRenderer
    private var gameState: GameState = GameState.Playing

    private enum class GameState {
        Playing,
        Paused
    }

    override fun onTouchPointLongPress() {
        gameState = when (gameState) {
            GameState.Playing -> GameState.Paused
            GameState.Paused -> {
                gameEngine.reset()
                GameState.Playing
            }
        }
    }

    override fun performOnServiceConnected(
        context: Context,
        glyphMatrixManager: GlyphMatrixManager
    ) {
        gameEngine = GameOfLifeEngine()
        renderer = GlyphMatrixRenderer()

        backgroundScope.launch {
            while (isActive) {
                if (gameState == GameState.Playing) {
                    gameEngine.calculateNextGeneration()
                    val frame = renderer.renderFrame(gameEngine.getGrid())
                    uiScope.launch {
                        glyphMatrixManager.setMatrixFrame(frame)
                    }
                }
                delay(200) // 200ms per generation
            }
        }
    }

    override fun performOnServiceDisconnected(context: Context) {
        backgroundScope.cancel()
    }

    // Inner class for Game of Life logic
    inner class GameOfLifeEngine {
        private var grid = Array(HEIGHT) { BooleanArray(WIDTH) }
        private var nextGrid = Array(HEIGHT) { BooleanArray(WIDTH) }

        init {
            reset()
        }

        fun reset() {
            for (y in 0 until HEIGHT) {
                for (x in 0 until WIDTH) {
                    grid[y][x] = false
                }
            }
            // Add six gliders with random positions and rotations, ensuring they are within bounds
            for (i in 0..5) {
                val randomX = (2 until WIDTH - 2).random()
                val randomY = (2 until HEIGHT - 2).random()
                val randomRotation = (0..3).random()
                addGlider(randomX, randomY, randomRotation)
            }
        }

        private fun addGlider(centerX: Int, centerY: Int, rotation: Int) {
            // Original glider points relative to a center (1, 1)
            val points = arrayOf(
                Pair(0, -1), Pair(1, 0), Pair(-1, 1), Pair(0, 1), Pair(1, 1)
            )

            val rotatedPoints = points.map { (x, y) ->
                var newX = x
                var newY = y
                for (r in 0 until rotation) {
                    val tempX = newX
                    newX = -newY
                    newY = tempX
                }
                Pair(newX, newY)
            }

            for ((dx, dy) in rotatedPoints) {
                val finalX = centerX + dx
                val finalY = centerY + dy
                if (finalX in 0 until WIDTH && finalY in 0 until HEIGHT) {
                    grid[finalY][finalX] = true
                }
            }
        }

        fun calculateNextGeneration() {
            var hasLiveCells = false
            for (y in 0 until HEIGHT) {
                for (x in 0 until WIDTH) {
                    val liveNeighbors = countLiveNeighbors(x, y)
                    if (grid[y][x]) {
                        nextGrid[y][x] = liveNeighbors == 2 || liveNeighbors == 3
                    } else {
                        nextGrid[y][x] = liveNeighbors == 3
                    }
                    if (nextGrid[y][x]) {
                        hasLiveCells = true
                    }
                }
            }
            val temp = grid
            grid = nextGrid
            nextGrid = temp

            if (!hasLiveCells) {
                reset()
            }
        }

        private fun countLiveNeighbors(x: Int, y: Int): Int {
            var count = 0
            for (i in -1..1) {
                for (j in -1..1) {
                    if (i == 0 && j == 0) continue
                    val newY = (y + i + HEIGHT) % HEIGHT
                    val newX = (x + j + WIDTH) % WIDTH
                    if (grid[newY][newX]) {
                        count++
                    }
                }
            }
            return count
        }

        fun getGrid(): Array<BooleanArray> {
            return grid
        }
    }

    // Inner class for rendering the game state to the Glyph Matrix
    inner class GlyphMatrixRenderer {
        fun renderFrame(grid: Array<BooleanArray>): IntArray {
            val pixelArray = IntArray(WIDTH * HEIGHT)
            for (y in 0 until HEIGHT) {
                for (x in 0 until WIDTH) {
                    val index = y * WIDTH + x
                    pixelArray[index] = if (grid[y][x]) 255 else 0
                }
            }
            return pixelArray
        }
    }

    private companion object {
        private const val WIDTH = 25
        private const val HEIGHT = 25
    }
}