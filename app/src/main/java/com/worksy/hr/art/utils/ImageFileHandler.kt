package com.worksy.hr.art.utils

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.worksy.hr.art.databinding.CreateClaimFormLayoutBinding
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.activity.Homeactivity.Companion.hideLoading
import com.worksy.hr.art.views.activity.Homeactivity.Companion.showLoading
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ImageFileHandler(
    private val context: Context,
    private val requireActivity: FragmentActivity,
    private val viewModel: ClaimViewModel,
    private val viewLifecycleOwner: LifecycleOwner,
    private val bottomSheetBinding: CreateClaimFormLayoutBinding?=null
) {
    val uploadedFileCodes = mutableListOf<String>()
    fun uploadFile(file: File, mimeType: String, progressListener: (Int) -> Unit) {
        val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        Log.d("uploadFileMethod", "uploadFileMethod: $body")
        viewModel.uploadFileClaim(body, progressListener)
        viewModel.uploadFileClaim.observe(viewLifecycleOwner) {
            when (it) {
                is UIResult.Success -> {
                    hideLoading()
                    val uploadedFileUrl = it.data?.data?.key
                    uploadedFileUrl?.let { code ->
                        uploadedFileCodes.add(code)
                    }
                    bottomSheetBinding?.relImageUploadedFile?.visibility=View.VISIBLE
                    Toast.makeText(requireActivity, it.data!!.message, Toast.LENGTH_SHORT).show()
                    Log.d("uploadFile", "uploadFile: $uploadedFileUrl")
                }

                is UIResult.Error -> {
                    Toast.makeText(requireActivity, it.message, Toast.LENGTH_SHORT).show()
                    hideLoading()
                    Log.d("uploadFileError", "uploadFileError: ${it.message}")
                }

                is UIResult.Loading -> {
                    showLoading()
                }

                else -> {
                    hideLoading()
                }
            }
        }
    }

    fun updateBottomSheet(fileName: File, imageSize: Long?) {
        Log.d("ImageFileHandler", "Before Update: ${bottomSheetBinding?.relImageUploadedFile?.visibility}")
        requireActivity.runOnUiThread {
            bottomSheetBinding?.apply {
                Log.d("ImageFileHandler", "Updating bottom sheet")
                relImageUploadedFile?.let {
                    it.visibility = View.VISIBLE
                    Log.d("ImageFileHandler", "Setting visibility to VISIBLE")
                } ?: Log.e("ImageFileHandler", "relImageUploadedFile is null")

                deleteImg?.visibility = View.VISIBLE
                imgName?.text = fileName.toString()
                imgSize?.text = formatFileSize(imageSize)

                var fileImgSize: MutableList<String> = mutableListOf()
                fileImgSize.add(formatFileSize(imageSize))
                viewModel.fileSize = fileImgSize
                Log.d("ImageFileHandler", "File size updated: $imageSize")
            } ?: Log.e("ImageFileHandler", "bottomSheetBinding is null")
        }
    }
    fun getFileSize(uri: Uri): Long {
        return requireActivity.contentResolver.openFileDescriptor(uri, "r")?.use { descriptor ->
            descriptor.statSize
        } ?: 0L
    }
    fun getFilePath(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireActivity.contentResolver.query(uri, projection, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        val filePath = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return filePath ?: ""
    }
    private fun formatFileSize(size: Long?): String {
        size ?: return "0 B"
        val kiloBytes = size / 1024.0
        return when {
            kiloBytes < 1 -> "$size B"
            kiloBytes < 1024 -> String.format("%.2f KB", kiloBytes)
            else -> String.format("%.2f MB", kiloBytes / 1024)
        }
    }
}
