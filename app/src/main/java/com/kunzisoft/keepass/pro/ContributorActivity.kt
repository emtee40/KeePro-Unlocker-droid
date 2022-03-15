package com.kunzisoft.keepass.pro

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kunzisoft.keepass.pro.databinding.ActivityContributorBinding


/**
 * Checkout implementation for the app
 */
class ContributorActivity : AppCompatActivity() {

    private lateinit var layoutBinding: ActivityContributorBinding

    /**
     * Initialize the Google Pay API on creation of the activity
     *
     * @see AppCompatActivity.onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use view binding to access the UI elements
        layoutBinding = ActivityContributorBinding.inflate(layoutInflater)
        setContentView(layoutBinding.root)

        layoutBinding.keepassdxButton.setOnClickListener {
            openExternalApp("com.kunzisoft.keepass.free")
        }
        layoutBinding.keepassdxExportSettingsButton.setOnClickListener {
            // TODO
        }
        layoutBinding.keepassdxHowExportSettings.setOnClickListener {
            gotoUrl("https://github.com/Kunzisoft/KeePassDX/wiki/Import-and-Export#app-properties")
        }

        layoutBinding.filesyncButton.setOnClickListener {
            // TODO
        }
        layoutBinding.filesyncSoon.setOnClickListener {
            gotoUrl("https://github.com/Kunzisoft/FileSync")
        }
    }

    fun openExternalApp(packageName: String) {
        var launchIntent: Intent? = null
        try {
            launchIntent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        } catch (ignored: Exception) {
        }
        try {
            if (launchIntent == null) {
                startActivity(
                    Intent(Intent.ACTION_VIEW)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .setData(Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
                )
            } else {
                startActivity(launchIntent)
            }
        } catch (e: Exception) {
            Log.e("Contributor", "App cannot be open", e)
        }
    }

    fun gotoUrl(url: String?) {
        try {
            if (url != null && url.isNotEmpty()) {
                // Default http:// if no protocol specified
                val newUrl = if (!url.contains("://")) {
                    "http://$url"
                } else {
                    url
                }
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(newUrl)))
            }
        } catch (e: Exception) {
            Toast.makeText(this, R.string.no_url_handler, Toast.LENGTH_LONG).show()
        }
    }
}