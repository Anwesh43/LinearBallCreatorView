package com.anwesh.uiprojects.ballcreatorview

/**
 * Created by anweshmishra on 17/07/20.
 */

import android.view.View
import android.view.MotionEvent
import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color

val parts : Int = 4
val colors : Array<String> = arrayOf("#4CAF50", "#F44336", "#2196F3", "#FFC107", "#009688")
val scGap : Float = 0.02f / parts
val rFactor : Float = 4.5f
val strokeFactor : Int = 90
val delay : Long = 20
val backColor : Int = Color.parseColor("#BDBDBD")

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.sinify() : Float = Math.sin(this * Math.PI).toFloat()

fun Canvas.drawBallCreator(scale : Float, w : Float, h : Float, paint : Paint) {
    val r : Float = Math.min(w, h) / rFactor
    val sf : Float = scale.sinify()
    val sf1 : Float = sf.divideScale(0, parts)
    val sf2 : Float = sf.divideScale(1, parts)
    val sf3 : Float = sf.divideScale(2, parts)
    val sf4 : Float = sf.divideScale(3, parts)
    val x1 : Float = (w / 2) * (sf2 + sf4)
    val x2 : Float = (w / 2) * (sf1 + sf3)
    drawLine(x1, h / 2, x2, h / 2, paint)
    drawCircle(0f, h / 2, r * (sf2 - sf3), paint)
}

fun Canvas.drawBCNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = Color.parseColor(colors[i])
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawBallCreator(scale, w, h, paint)
}
