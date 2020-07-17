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
    drawCircle(w / 2, h / 2, r * (sf2 - sf3), paint)
}

fun Canvas.drawBCNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    paint.color = Color.parseColor(colors[i])
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    drawBallCreator(scale, w, h, paint)
}

class BallCreatorView(ctx : Context) : View(ctx) {

    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class BCNode(var i : Int, val state : State = State()) {

        private var next : BCNode? = null
        private var prev : BCNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < colors.size - 1) {
                next = BCNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawBCNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : BCNode {
            var curr : BCNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class BallCreator(var i : Int) {

        private var curr : BCNode = BCNode(0)
        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(cb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : BallCreatorView) {

        private val animator : Animator = Animator(view)
        private val bc : BallCreator = BallCreator(0)
        private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

        fun render(canvas : Canvas) {
            canvas.drawColor(backColor)
            bc.draw(canvas, paint)
            animator.animate {
                bc.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            bc.startUpdating {
                animator.start()
            }
        }
    }

    companion object {

        fun create(activity : Activity) : BallCreatorView {
            val view : BallCreatorView = BallCreatorView(activity)
            activity.setContentView(view)
            return view
        }
    }
}