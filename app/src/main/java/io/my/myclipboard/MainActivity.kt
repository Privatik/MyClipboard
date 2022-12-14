package io.my.myclipboard

import android.annotation.SuppressLint
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope

import io.my.myclipboard.MyClipboardService.Companion.START_ACTION
import io.my.myclipboard.MyClipboardService.Companion.STOP_ACTION
import io.my.myclipboard.ui.theme.MyClipboardTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val intentFilter by lazy { IntentFilter("clipboard") }
    private val receiver: BroadcastReceiver by lazy { MyClipboardBroadcast() }

    private val batteryIgnoreOptimizationContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        checkIncludedIgnoreBattery(
            blockWithIntentForPermission = { _ ->
               finish()
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkIncludedIgnoreBattery(
            blockWithIntentForPermission = {
                lifecycleScope.launchWhenResumed { batteryIgnoreOptimizationContract.launch(it) }
            }
        )

        registerReceiver(receiver, intentFilter)

        setContent {
            Box(modifier = Modifier.fillMaxSize().background(Color.Green))
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    @SuppressLint("BatteryLife")
    private fun checkIncludedIgnoreBattery(
        blockWithIntentForPermission: (Intent) -> Unit
    ){
        val powerManager = getSystemService(AppCompatActivity.POWER_SERVICE) as PowerManager
        if (!powerManager.isIgnoringBatteryOptimizations(packageName)){
            val intent = Intent(
                Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                Uri.parse("package:${packageName}")
            )
            blockWithIntentForPermission(intent)
        }
    }
}