package io.my.myclipboard

import android.app.Activity
import android.content.*
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN


class MyClipboardBroadcast: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val clipboard = context.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        if (
            clipboard.hasPrimaryClip() &&
            clipboard.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN) == true
        ) {
            val item: ClipData.Item = clipboard.primaryClip!!.getItemAt(0)
            item.text.let {
                if (it.isNullOrBlank()){
                    println("MyClipboardBroadcast txt is empty")
                    resultCode = Activity.RESULT_CANCELED
                } else {
                    println("MyClipboardBroadcast success")
                    resultCode = Activity.RESULT_OK
                    resultData = it.toString()
                }
            }
        } else {
            println("MyClipboardBroadcast error")
            resultCode = Activity.RESULT_CANCELED
        }
    }
}