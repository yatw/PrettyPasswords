package com.prettypasswords.view.activities

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lxj.xpopup.XPopup
import com.prettypasswords.R
import kotlinx.android.synthetic.main.activity_browser.*


class BrowserActivity : AppCompatActivity() {

    lateinit var url: String
    lateinit var title: String
    lateinit var myWebView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)   // remove splash screen
        setContentView(R.layout.activity_browser)


        initData()
        initView()
        initClick()
    }

    private fun initData(){
        url = intent.getStringExtra("url")!!
        title = intent.getStringExtra("title")!!

    }

    private fun initView(){


        val loadingView = XPopup.Builder(this)
            .asLoading("Loading URL...")
            .show()

        val tv = findViewById<TextView>(R.id.title)
        tv.text = title

        val url = intent.getStringExtra("url")


        myWebView = findViewById<WebView>(R.id.webview)

        myWebView.setWebViewClient(object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                loadingView.dismiss()
            }
        })

        myWebView.loadUrl(url)

        val settings = myWebView.settings
        settings.javaScriptEnabled = false

/*        // must base64 or # will give error
        val encodedHtml =
            Base64.encodeToString(html.getBytes(), Base64.NO_PADDING)
        myWebView.loadData(encodedHtml, "text/html", "base64")*/
    }

    private fun initClick(){

        btn_close.setOnClickListener {
            finish()
        }

    }

    override fun onBackPressed() {

        if (myWebView.canGoBack()){
            myWebView.goBack()
        }else{
            finish()
        }

    }

}