/*
 * Copyright 2019 Jeremy Jamet / Kunzisoft.
 *
 * This file is part of KeePassDX.
 *
 *  KeePassDX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  KeePassDX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with KeePassDX.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.kunzisoft.keepass.pro

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity

class ExternalFileHelper(context: FragmentActivity) {

    private var activity: FragmentActivity? = context

    private var createDocumentResultLauncher: ActivityResultLauncher<String>? = null

    fun buildCreateDocument(typeString: String = "application/octet-stream",
                            onFileCreated: (fileCreated: Uri?)->Unit) {

        val resultCallback = ActivityResultCallback<Uri?> { result ->
            onFileCreated.invoke(result)
        }

        createDocumentResultLauncher =
            activity?.registerForActivityResult(
                CreateDocument(typeString),
                resultCallback
            )
    }

    fun createDocument(titleString: String) {
        try {
            createDocumentResultLauncher?.launch(titleString)
        } catch (e: Exception) {
            Log.e(TAG, "Unable to create document", e)
        }
    }

    class CreateDocument(private val typeString: String) : ActivityResultContracts.CreateDocument(typeString) {
        override fun createIntent(context: Context, input: String): Intent {
            return super.createIntent(context, input).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = typeString
            }
        }
    }


    companion object {

        private const val TAG = "OpenFileHelper"
    }
}
