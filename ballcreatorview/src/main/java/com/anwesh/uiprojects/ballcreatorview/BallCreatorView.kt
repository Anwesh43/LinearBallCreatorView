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

val parts : Int = 3
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
