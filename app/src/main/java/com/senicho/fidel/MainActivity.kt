package com.senicho.fidel

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : ComponentActivity() {

    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        // ጸሊም ኣይኮናት ኣብ ልዕሊ ብሩህ ድሕረ-ባይታ (ሕብሪ ናይቲ app ኩሉ ግዜ ብሩህ እዩ)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // እቲ app ትሕቲ status bar ወይ navigation bar ከይሓብእ
        val root = findViewById<android.view.View>(R.id.root)
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        webView = findViewById(R.id.webView)
        webView.settings.apply {
            javaScriptEnabled = true                 // app ብ JavaScript እዩ ዝሰርሕ
            domStorageEnabled = true                 // localStorage
            mediaPlaybackRequiresUserGesture = false // ጣቕዒትን ድምጽን
            allowFileAccess = false                  // android_asset ብዘይ እዚ ይሰርሕ
            cacheMode = WebSettings.LOAD_DEFAULT
            textZoom = 100                           // ፊደላት ብዓቐኖም ይርኣዩ
        }
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()

        // index.html ካብ assets ፎልደር ይኽፈት (ብዘይ ኢንተርነት ይሰርሕ)
        webView.loadUrl("file:///android_asset/index.html")

        // መመለሲ (Back): ኣብ ውሽጢ app ይምለስ፡ ኣብ ቀንዲ ገጽ ጥራይ ይዕጾ
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                webView.evaluateJavascript("typeof appBack==='function'&&appBack()") { handled ->
                    if (handled != "true") finish()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}
