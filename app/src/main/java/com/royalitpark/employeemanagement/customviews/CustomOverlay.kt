package com.royalitpark.employeemanagement.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable
import androidx.core.content.res.ResourcesCompat
import com.royalitpark.employeemanagement.R


class CustomOverlay :View{
    constructor(context: Context?) : super(context!!) // constructor
    constructor(context: Context?, @Nullable attrs: AttributeSet?) : super(context!!,attrs) {} // constructor

    var paint=Paint()
    private val paint2: Paint by lazy { Paint() }
    private lateinit var clearPaint: Paint

    init {
        clearPaint = Paint()
        clearPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
    }
    override fun onDraw(canvas: Canvas) { // Override the onDraw() Method
        super.onDraw(canvas)
        paint.style = Paint.Style.STROKE
        paint.color = Color.GREEN
        paint.strokeWidth = 10f

        paint2.color = ResourcesCompat.getColor(resources, R.color.color_trans, null)
        canvas.drawPaint(paint2)

        //center
       /* val x0 = canvas.width / 2
        val y0 = canvas.height / 2
        val dx = canvas.height / 3
        val dy = canvas.height / 3
        //draw guide box
        canvas.drawRect(
            (x0 - dx).toFloat(),
            (y0 - dy).toFloat(),
            (x0 + dx).toFloat(),
            (y0 + dy).toFloat(),
            paint
        )*/

        val x = width
        val y = height
        val radius: Int
        radius = 100

        val circleRadius: Float = canvas.width.toFloat() / 2.3f  //royalitpark radius
        clearPaint.color = ResourcesCompat.getColor(resources, R.color.transparent, null)
        //circle
        canvas.drawCircle(canvas.width .toFloat() / 2, canvas.height.toFloat() / 2, circleRadius, clearPaint)

        //canvas.drawCircle((canvas.width / 2).toFloat(), (canvas.height / 2).toFloat(), (canvas.width / 3).toFloat(), paint)
    }
}