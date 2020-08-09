package com.prettypasswords.view.utilities

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.text.TextPaint
import com.prettypasswords.R
import kotlin.math.abs


// https://stackoverflow.com/questions/23122088/colored-boxed-with-letters-a-la-gmail
class LetterTileProvider(context: Context) {

    private val NUM_OF_TILE_COLORS = 10

    private val mPaint = TextPaint()

    /** The bounds that enclose the letter  */
    private val mBounds = Rect()
    private val mCanvas = Canvas()
    private val mFirstChar = CharArray(1)
    private val mColors: TypedArray

    /** The font size used to display the letter  */
    private val mTileLetterFontSize: Int
    fun getLetterTile(
        firstChar: Char,
        key: String,
        width: Int,
        height: Int,
        circle: Boolean
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = mCanvas
        c.setBitmap(bitmap)
        if (circle) {
            val paint = Paint()
            paint.style = Paint.Style.FILL
            paint.color = pickColor(key)
            c.drawCircle(width / 2.toFloat(), height / 2.toFloat(), width / 2.toFloat(), paint)
        } else {
            c.drawColor(pickColor(key))
        }
        mFirstChar[0] = Character.toUpperCase(firstChar)
        mPaint.textSize = mTileLetterFontSize.toFloat()
        mPaint.getTextBounds(mFirstChar, 0, 1, mBounds)
        c.drawText(
            mFirstChar,
            0,
            1,
            0 + width / 2.toFloat(),
            0 + height / 2 + ((mBounds.bottom - mBounds.top) / 2).toFloat(),
            mPaint
        )
        return bitmap
    }

    private fun pickColor(key: String): Int {
        // String.hashCode() is not supposed to change across java versions, so
        // this should guarantee the same key always maps to the same color
        val color: Int = abs(key.hashCode()) % NUM_OF_TILE_COLORS
        return try {
            mColors.getColor(color, Color.BLACK)
        } finally {
            mColors.recycle()
        }
    }


    init {
        val res = context.resources
        mPaint.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
        mPaint.color = Color.WHITE
        mPaint.textAlign = Paint.Align.CENTER
        mPaint.isAntiAlias = true
        mColors = res.obtainTypedArray(R.array.letter_tile_colors)
        mTileLetterFontSize = res.getDimensionPixelSize(R.dimen.tile_letter_font_size)
    }
}