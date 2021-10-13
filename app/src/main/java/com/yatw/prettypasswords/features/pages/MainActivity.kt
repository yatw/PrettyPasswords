package com.yatw.prettypasswords.features.pages


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yatw.prettypasswords.R


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

//    /**
//     * Let text edit field lose focus when click on spot outside the field
//     * https://stackoverflow.com/questions/6677969/tap-outside-edittext-to-lose-focus
//     */
//    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
//        if (event.action == MotionEvent.ACTION_UP) {
//            val v = currentFocus
//            if (v is EditText) {
//                val outRect = Rect()
//                v.getGlobalVisibleRect(outRect)
//                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
//                    v.clearFocus()
//                    val imm: InputMethodManager =
//                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
//                }
//            }
//        }
//        return super.dispatchTouchEvent(event)
//    }
//



}