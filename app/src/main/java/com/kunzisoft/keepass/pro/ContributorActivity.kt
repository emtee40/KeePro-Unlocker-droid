package com.kunzisoft.keepass.pro

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.kunzisoft.keepass.pro.databinding.ActivityContributorBinding
import java.util.*


/**
 * Checkout implementation for the app
 */
class ContributorActivity : AppCompatActivity() {

    private lateinit var layoutBinding: ActivityContributorBinding
    private var mExternalFileHelper: ExternalFileHelper? = null

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
        if (containsAppProperties(this)) {
            mExternalFileHelper = ExternalFileHelper(this)
            mExternalFileHelper?.buildCreateDocument { createdFileUri ->
                // Export app properties result
                try {
                    createdFileUri?.let { uri ->
                        contentResolver?.openOutputStream(uri)?.use { outputStream ->
                            getAppProperties(this)
                                .store(outputStream, getString(R.string.description_app_properties))
                        }
                        Toast.makeText(this, R.string.success_export_app_properties, Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, R.string.error_export_app_properties, Toast.LENGTH_LONG).show()
                    Log.e("Contributor", "Unable to export app properties", e)
                }
            }

            layoutBinding.keepassdxExportText.isVisible = true
            layoutBinding.keepassdxExportSettingsButton.isVisible = true
            layoutBinding.keepassdxHowExportSettings.isVisible = true
            layoutBinding.keepassdxExportSettingsButton.setOnClickListener {
                mExternalFileHelper?.createDocument(getString(R.string.app_properties_file_name))
            }
            layoutBinding.keepassdxHowExportSettings.setOnClickListener {
                gotoUrl("https://github.com/Kunzisoft/KeePassDX/wiki/Import-and-Export#app-properties")
            }
        } else {
            layoutBinding.keepassdxExportText.isVisible = false
            layoutBinding.keepassdxExportSettingsButton.isVisible = false
            layoutBinding.keepassdxHowExportSettings.isVisible = false
        }

        layoutBinding.yubikeyButton.setOnClickListener {
            openExternalApp("com.kunzisoft.hardware.key")
        }

        layoutBinding.filesyncButton.setOnClickListener {
            // TODO
        }
        layoutBinding.filesyncSoon.setOnClickListener {
            gotoUrl("https://github.com/Kunzisoft/FileSync")
        }
    }

    private fun openExternalApp(packageName: String) {
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

    private fun gotoUrl(url: String?) {
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

    private fun containsAppProperties(context: Context): Boolean {
        var containsKeePassProperties = false
        for ((name, _) in PreferenceManager.getDefaultSharedPreferences(context).all) {
            containsKeePassProperties = isAppProperty(name)
        }
        return containsKeePassProperties
    }

    private fun isAppProperty(name: String): Boolean {
        return when (name) {
            getString(R.string.allow_no_password_key) -> true
            getString(R.string.delete_entered_password_key) -> true
            getString(R.string.enable_read_only_key) -> true
            getString(R.string.enable_auto_save_database_key) -> true
            getString(R.string.enable_keep_screen_on_key) -> true
            getString(R.string.auto_focus_search_key) -> true
            getString(R.string.subdomain_search_key) -> true
            getString(R.string.app_timeout_key) -> true
            getString(R.string.lock_database_screen_off_key) -> true
            getString(R.string.lock_database_back_root_key) -> true
            getString(R.string.lock_database_show_button_key) -> true
            getString(R.string.password_length_key) -> true
            getString(R.string.list_password_generator_options_key) -> true
            getString(R.string.hide_password_key) -> true
            getString(R.string.allow_copy_password_key) -> true
            getString(R.string.remember_database_locations_key) -> true
            getString(R.string.show_recent_files_key) -> true
            getString(R.string.hide_broken_locations_key) -> true
            getString(R.string.remember_keyfile_locations_key) -> true
            getString(R.string.biometric_unlock_enable_key) -> true
            getString(R.string.device_credential_unlock_enable_key) -> true
            getString(R.string.biometric_auto_open_prompt_key) -> true
            getString(R.string.temp_advanced_unlock_enable_key) -> true
            getString(R.string.temp_advanced_unlock_timeout_key) -> true

            getString(R.string.magic_keyboard_key) -> true
            getString(R.string.clipboard_notifications_key) -> true
            getString(R.string.clear_clipboard_notification_key) -> true
            getString(R.string.clipboard_timeout_key) -> true
            getString(R.string.settings_autofill_enable_key) -> true
            getString(R.string.keyboard_notification_entry_key) -> true
            getString(R.string.keyboard_notification_entry_clear_close_key) -> true
            getString(R.string.keyboard_entry_timeout_key) -> true
            getString(R.string.keyboard_selection_entry_key) -> true
            getString(R.string.keyboard_search_share_key) -> true
            getString(R.string.keyboard_save_search_info_key) -> true
            getString(R.string.keyboard_auto_go_action_key) -> true
            getString(R.string.keyboard_key_vibrate_key) -> true
            getString(R.string.keyboard_key_sound_key) -> true
            getString(R.string.keyboard_previous_database_credentials_key) -> true
            getString(R.string.keyboard_previous_fill_in_key) -> true
            getString(R.string.keyboard_previous_lock_key) -> true
            getString(R.string.autofill_close_database_key) -> true
            getString(R.string.autofill_auto_search_key) -> true
            getString(R.string.autofill_inline_suggestions_key) -> true
            getString(R.string.autofill_manual_selection_key) -> true
            getString(R.string.autofill_save_search_info_key) -> true
            getString(R.string.autofill_ask_to_save_data_key) -> true
            getString(R.string.autofill_application_id_blocklist_key) -> true
            getString(R.string.autofill_web_domain_blocklist_key) -> true

            getString(R.string.setting_style_key) -> true
            getString(R.string.setting_style_brightness_key) -> true
            getString(R.string.setting_icon_pack_choose_key) -> true
            getString(R.string.show_entry_colors_key) -> true
            getString(R.string.list_entries_show_username_key) -> true
            getString(R.string.list_groups_show_number_entries_key) -> true
            getString(R.string.show_otp_token_key) -> true
            getString(R.string.show_uuid_key) -> true
            getString(R.string.list_size_key) -> true
            getString(R.string.monospace_font_fields_enable_key) -> true
            getString(R.string.hide_expired_entries_key) -> true
            getString(R.string.enable_education_screens_key) -> true

            getString(R.string.sort_node_key) -> true
            getString(R.string.sort_group_before_key) -> true
            getString(R.string.sort_ascending_key) -> true
            getString(R.string.sort_recycle_bin_bottom_key) -> true
            getString(R.string.allow_copy_password_first_time_key) -> true

            else -> false
        }
    }

    private fun getAppProperties(context: Context): Properties {
        val properties = Properties()
        for ((name, value) in PreferenceManager.getDefaultSharedPreferences(context).all) {
            if (isAppProperty(name)) {
                properties[name] = value.toString()
            }
        }
        return properties
    }
}