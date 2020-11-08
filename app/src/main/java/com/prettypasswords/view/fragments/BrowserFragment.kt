package com.prettypasswords.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lxj.xpopup.XPopup
import com.prettypasswords.R
import kotlinx.android.synthetic.main.fragment_browser.*


class BrowserFragment : Fragment() {

    lateinit var url: String
    lateinit var title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // read arguments at onCreate
        url = arguments!!.getString("url", "")
        title = arguments!!.getString("title", "")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initClick()
    }

    private fun initView(){

        val loadingView = XPopup.Builder(activity)
            .asLoading("Loading URL...")
            .show()

        tv_title.text = title

        webview.setWebViewClient(object : WebViewClient() {

            override fun onPageFinished(view: WebView, url: String) {
                loadingView.dismiss()
            }
        })

        webview.loadUrl(url)

        val settings = webview.settings
        settings.javaScriptEnabled = false

/*        // must base64 or # will give error
        val encodedHtml =
            Base64.encodeToString(html.getBytes(), Base64.NO_PADDING)
        myWebView.loadData(encodedHtml, "text/html", "base64")*/
    }

    private fun initClick(){

        btn_close.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }


}