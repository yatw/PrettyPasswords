package com.prettypasswords.features.pages.content

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lxj.xpopup.XPopup

class CodeFragment: BrowserFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val loadingView = XPopup.Builder(activity)
            .asLoading("Loading URL...")
            .show()

        binding.webview.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                loadingView.dismiss()
            }
        })

        binding.webview.loadUrl("https://github.com/yatw/PrettyPasswords")

        val settings = binding.webview.settings
        settings.javaScriptEnabled = false

/*        // must base64 or # will give error
        val encodedHtml =
            Base64.encodeToString(html.getBytes(), Base64.NO_PADDING)
        myWebView.loadData(encodedHtml, "text/html", "base64")*/
    }
}