package com.prettypasswords.features.pages.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.lxj.xpopup.XPopup
import com.prettypasswords.databinding.FragmentBrowserBinding

class BrowserFragment: Fragment() {

    private lateinit var binding: FragmentBrowserBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBrowserBinding.inflate(inflater, container, false)
        return binding.root
    }

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

       // binding.webview.loadUrl(url)

        val settings = binding.webview.settings
        settings.javaScriptEnabled = false

/*        // must base64 or # will give error
        val encodedHtml =
            Base64.encodeToString(html.getBytes(), Base64.NO_PADDING)
        myWebView.loadData(encodedHtml, "text/html", "base64")*/
    }



}