package com.example.findjob.utils

import android.content.Context
import android.net.Uri
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CloudinaryConfig @Inject constructor(
    private val context: Context
) {

    init {
        val config = mapOf(


        )
        MediaManager.init(context, config)
    }

    fun uploadImage(uri: Uri, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        try {
            MediaManager.get()
                .upload(uri)
                .callback(object : UploadCallback {
                    override fun onStart(requestId: String) {
                    }

                    override fun onProgress(requestId: String, bytes: Long, totalBytes: Long) {
                    }

                    override fun onSuccess(requestId: String, resultData: Map<Any?, Any?>) {
                        val secureUrl = resultData["secure_url"] as String
                        onSuccess(secureUrl)
                    }

                    override fun onError(requestId: String, error: ErrorInfo) {
                        onError(error.description)
                    }

                    override fun onReschedule(requestId: String, error: ErrorInfo) {
                        onError(error.description)
                    }
                })
                .dispatch()
        } catch (e: Exception) {
            onError("Error preparing file: ${e.message}")
        }
    }
}