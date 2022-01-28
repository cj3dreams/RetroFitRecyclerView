package com.pseudoencom.retrofitrecyclerview.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.core.os.HandlerCompat.postDelayed
import com.pseudoencom.retrofitrecyclerview.MainActivity
import com.pseudoencom.retrofitrecyclerview.R
import android.webkit.WebChromeClient
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar


class WebViewFragment : Fragment() {
    private lateinit var progressBar: ProgressBar
    private lateinit var webView: WebView
    lateinit var gotUrl: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var activity = activity as MainActivity
        activity.toolbar.visibility = View.GONE
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.fragment_web_view, container, false)
        var isPageLoaded = false

        progressBar = view.findViewById(R.id.webViewLoading)
        webView = view.findViewById(R.id.webView)
        if (webView.isVisible) {
            webView.webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, progress: Int) {
                    when (progress) {
                        in 1..10 -> this@WebViewFragment.view?.let { Snackbar.make(it, "Started loading page . . .", 1500).show() }
                        in 25..35 -> this@WebViewFragment.view?.let { Snackbar.make(it, "Loading page $progress%", 1500).show()}
                        in 55..65 -> this@WebViewFragment.view?.let { Snackbar.make(it, "Loading page $progress%", 1500).show()}
                        in 75..85 -> this@WebViewFragment.view?.let { Snackbar.make(it, "Loading page $progress%", 1500).show()}
                        in 90..100 -> this@WebViewFragment.view?.let { Snackbar.make(it, "Loading page $progress%", 1500).show()}
                    }
                    if (progress >= 75) progressBar.visibility = View.GONE
                }
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(gotUrl)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        var activity = activity as MainActivity
        activity.toolbar.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webView.isEnabled = false
        var activity = activity as MainActivity
        activity.toolbar.visibility = View.VISIBLE
    }

    companion object{
        fun getUrl(url: String): WebViewFragment{
            val fragment = WebViewFragment()
            fragment.gotUrl = url
            return fragment
        }
    }
}