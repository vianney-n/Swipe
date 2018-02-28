package com.paperatus.swipe.map

import com.badlogic.gdx.math.MathUtils
import com.paperatus.swipe.data.PathPoint
import Path
import ktx.collections.GdxArray

interface MapGenerator {
    fun generatePoints(leftBound: Float, rightBound: Float,
                                start: PathPoint): GdxArray<PathPoint>
}

private const val SOFT_CURVE_MIN_Y_DISTANCE = 8.0f
private const val SOFT_CURVE_MAX_Y_DISTANCE = 20.0f
private const val SOFT_CURVE_MIN_POINTS = 3
private const val SOFT_CURVE_MAX_POINTS = 8

private const val HARD_CURVE_MIN_Y_DISTANCE = 5.0f
private const val HARD_CURVE_MAX_Y_DISTANCE = 6.0f
private const val HARD_CURVE_MIN_POINTS = 2
private const val HARD_CURVE_MAX_POINTS = 3

private const val UP_MIN_POINTS = 2
private const val UP_MAX_POINTS = 4
private const val UP_MAX_X_DELTA = 3.0f

class ProceduralMapGenerator : MapGenerator {
    private val tempArray = GdxArray<PathPoint>()
    private val path: Path = Path()

    override fun generatePoints(leftBound: Float,
                                rightBound: Float,
                                start: PathPoint): GdxArray<PathPoint> {

        tempArray.clear()

        val (type, direction) = path.random()

        when (type) {
            Path.Type.SoftCurve -> generateSoftCurve(
                    leftBound, rightBound, direction, start
            )
            Path.Type.HardCurve -> generateHardCurve(
                    leftBound, rightBound, direction, start
            )
            Path.Type.Up -> generateUp(leftBound, rightBound, start)
            Path.Type.Uninitialized -> throw RuntimeException()
        }

        return tempArray
    }

    private fun generateSoftCurve(leftBound: Float,
                                  rightBound: Float,
                                  direction: Path.Direction,
                                  start: PathPoint) {
        val count = randomSoftCurvePointCount()

        var previous = start
        for (i in 1..count) {
            val nextPoint = PathPoint.obtain()
            nextPoint.set(previous)

            val delta = when(direction) {
                Path.Direction.Left -> leftBound - previous.x
                Path.Direction.Right -> rightBound - previous.x
                Path.Direction.None -> throw RuntimeException()
            }

            val offsetX = MathUtils.random(delta / 2.0f, delta / 5.0f)
            val offsetY = MathUtils.random(
                    SOFT_CURVE_MIN_Y_DISTANCE, SOFT_CURVE_MAX_Y_DISTANCE
            )

            nextPoint.add(offsetX, offsetY)
            tempArray.add(nextPoint)

            previous = nextPoint
        }
    }

    private fun generateHardCurve(leftBound: Float,
                                  rightBound: Float,
                                  direction: Path.Direction,
                                  start: PathPoint) {
        val count = randomHardCurvePointCount()

        var previous = start
        for (i in 1..count) {
            val nextPoint = PathPoint.obtain()
            nextPoint.set(previous)

            val delta = when(direction) {
                Path.Direction.Left -> leftBound - previous.x
                Path.Direction.Right -> rightBound - previous.x
                Path.Direction.None -> throw RuntimeException()
            }

            val offsetX = MathUtils.random(delta / 2.0f, delta / 3.0f)
            val offsetY = MathUtils.random(
                    HARD_CURVE_MIN_Y_DISTANCE, HARD_CURVE_MAX_Y_DISTANCE
            )

            nextPoint.add(offsetX, offsetY)
            tempArray.add(nextPoint)

            previous = nextPoint
        }
    }

    private fun generateUp(leftBound: Float,
                           rightBound: Float,
                           start: PathPoint) {
        val count = randomUpPointCount()

        var previousPoint = start
        for (i in 1..count) {
            val leftDelta = leftBound - previousPoint.x
            val rightDelta = rightBound - previousPoint.x

            val nextPoint = PathPoint.obtain()
            nextPoint.set(previousPoint)

            val offsetX = MathUtils.clamp(
                    MathUtils.random(leftDelta, rightDelta),
                    -UP_MAX_X_DELTA,
                    UP_MAX_X_DELTA
            )

            nextPoint.add(
                    offsetX,
                    MathUtils.random(SOFT_CURVE_MIN_Y_DISTANCE, SOFT_CURVE_MAX_Y_DISTANCE)
            )

            tempArray.add(nextPoint)
            previousPoint = nextPoint
        }
    }

    private fun randomSoftCurvePointCount() = MathUtils.random(
            SOFT_CURVE_MIN_POINTS, SOFT_CURVE_MAX_POINTS)

    private fun randomHardCurvePointCount() = MathUtils.random(
            HARD_CURVE_MIN_POINTS, HARD_CURVE_MAX_POINTS
    )

    private fun randomUpPointCount() = MathUtils.random(
            UP_MIN_POINTS, UP_MAX_POINTS
    )

}