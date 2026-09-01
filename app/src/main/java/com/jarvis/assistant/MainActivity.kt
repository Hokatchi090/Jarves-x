package com.jarvis.assistant

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var prankster: PranksterModule
    private lateinit var securityGuard: SecurityGuardModule
    private lateinit var halalScreener: HalalScreener
    private lateinit var tradingEngine: UnlimitedWealthEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // تهيئة الوحدات
        prankster = PranksterModule(this)
        securityGuard = SecurityGuardModule(this)
        halalScreener = HalalScreener()
        tradingEngine = UnlimitedWealthEngine(halalScreener)

        // طلب الأذونات الأساسية
        requestPermissions()

        // ربط الأزرار (من ملف XML)
        findViewById<Button>(R.id.btn_camera).setOnClickListener { 
            Toast.makeText(this, "تم تفعيل التعرف على الأشياء (الكاميرا)", Toast.LENGTH_SHORT).show()
            // هنا يتم استدعاء دالة الكاميرا (مثلاً: startCamera())
        }

        findViewById<Button>(R.id.btn_safety).setOnClickListener {
            securityGuard.triggerPanicMode()
            Toast.makeText(this, "وضع الطوارئ مفعل! تم بدء التسجيل.", Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.btn_prank).setOnClickListener {
            prankster.triggerFlashIR()
            prankster.playSpookySound()
        }

        findViewById<Button>(R.id.btn_trade).setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                // مثال: فحص سهم
                val isHalal = halalScreener.screenStock("AAPL")
                runOnUiThread { 
                    Toast.makeText(this@MainActivity, "هل السهم حلال؟ $isHalal", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.NFC,
            Manifest.permission.BLUETOOTH_CONNECT
        )
        val missing = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (missing.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, missing.toTypedArray(), 100)
        }
    }
}
