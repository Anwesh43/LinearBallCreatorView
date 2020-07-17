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
