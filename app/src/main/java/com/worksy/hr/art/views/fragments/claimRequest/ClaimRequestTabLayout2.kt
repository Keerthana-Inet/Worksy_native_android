package com.worksy.hr.art.views.fragments.claimRequest

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.integration.android.IntentIntegrator
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.FragmentClaimRequestTab2Binding
import com.worksy.hr.art.models.claimRequestResponse.AddItemRequest
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.utils.ImageFileHandler
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.activity.Homeactivity
import com.worksy.hr.art.views.adapter.claimRequestListing.CreateFormClaimAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class ClaimRequestTabLayout2 : Fragment(R.layout.fragment_claim_request_tab_layout2),
    CustomBottomSheetDialog.GalleryInteractionListener {
    private lateinit var fileHandler: ImageFileHandler
    private lateinit var gson: Gson
    private val READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 123
    var isCamera: Boolean = false
    private lateinit var rvAdapter: CreateFormClaimAdapter
    private var _binding: FragmentClaimRequestTab2Binding? = null
    private val requestViewModel: ClaimViewModel by activityViewModels()
    private val viewmodel: ClaimViewModel by activityViewModels()
    private val binding get() = _binding!!
    private lateinit var context: Context
    private val PICK_IMAGE_REQUEST = 101
    private val PICK_GALLERY_REQUEST = 102
    private val PICK_FILE_REQUEST = 201
    var request_edit_from_detail:String?=""
    private var customBottomSheetDialog: CustomBottomSheetDialog? = null
    @Inject
    lateinit var appPref: SharedPrefManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClaimRequestTab2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.GONE)
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = this.requireContext()
        gson= Gson()
        (activity as? Homeactivity)?.setBottomNavigationVisibility(View.GONE)
        fileHandler = ImageFileHandler(
            context = context,
            requireActivity = requireActivity(),
            viewModel = requestViewModel,
            viewLifecycleOwner = viewLifecycleOwner
        )
        if(arguments!=null){
            request_edit_from_detail=requireArguments().getString("request_edit_from_detail")
        }
        if(!request_edit_from_detail.isNullOrEmpty()){
            showCustomBottomSheetDialog(false)
        }

        binding.apply {
            cameraIcon.setOnClickListener {
                if (isCamera.equals(false)) {
                    fileUploadLayout.visibility = View.VISIBLE
                    isCamera = true
                } else if (isCamera.equals(true)) {
                    fileUploadLayout.visibility = View.GONE

                    isCamera = false
                }
            }
            uploadFileLyt.setOnClickListener {
                openGallery()
            }
            scanQrLyt.setOnClickListener {
                startQRScanner()
            }
            selectFromReceiptsLyt.setOnClickListener {
                openFilePicker()
            }
            claimItemLyt.setOnClickListener {
                showCustomBottomSheetDialog(false)
            }

            var test = appPref.getClaimRequest()
            Log.d("testingResponse", "testingResponse: "+test)
            if (!test.isNullOrEmpty()) {
                val savedClaimRequest: AddItemRequest = gson.fromJson(test, object : TypeToken<AddItemRequest>() {}.type)
                rvAdapter = CreateFormClaimAdapter(requireContext(),savedClaimRequest.pendingItems,viewmodel, onViewDetailClick = { _, progressData,itemData->
                    Log.d("ClickedData", "ClickedData: "+progressData)
                    Log.d("ItemData", "ItemData: "+itemData)
                    showCustomBottomSheetDialog(true,false,progressData,itemData)
                })
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = rvAdapter
                rvAdapter.notifyDataSetChanged()

            } else {
                // Handle case where savedClaimRequestJson is null or empty
            }
        }
        //Retrieve the item count value from viewmodel
        viewmodel.itemTotalCount.observe(viewLifecycleOwner, Observer {
                totalCount->
            binding.itemCountTxt.text=totalCount.toString()+" item"
        })
        //Retrieve total amount value from viewmodel
        viewmodel.itemTotalReqAmount.observe(viewLifecycleOwner, Observer {
                totalAmount->
            binding.rmValueTxt.text="MYR "+totalAmount.toString()
        })
    }

    override fun onResume() {
        super.onResume()
        requestViewModel.itemsLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                rvAdapter = CreateFormClaimAdapter(requireContext(),it,viewmodel, onViewDetailClick = { _, progressData,itemData->
                    Log.d("ClickedData", "ClickedData: "+progressData)
                    Log.d("ItemData", "ItemData: "+itemData)
                    showCustomBottomSheetDialog(true,false,progressData,itemData)
                })
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = rvAdapter
                rvAdapter.notifyDataSetChanged()
            }
        })
    }
    private fun showCustomBottomSheetDialog(requestEditBtn: Boolean,requestEditFromDetail: Boolean? = null,progressData: AddItemRequest.PendingItem?=null,itemData: AddItemRequest.PendingItem.Item?=null) {
         customBottomSheetDialog = CustomBottomSheetDialog(
            context = requireContext(),
            requestEditBtn = requestEditBtn,
            requestEditFromDetail = false,
            viewModel = requestViewModel,
            progressData = progressData,
            itemData = itemData,
            viewLifecycleOwner,
            appPref,gson,requireActivity()
        ).apply {
             setGalleryResultListener(object : CustomBottomSheetDialog.GalleryResultListener {
                 override fun onGalleryResult(uri: Uri) {
                     // Handle the gallery result here
                 }
             })
         }
        customBottomSheetDialog?.setGalleryInteractionListener(this)
        // Show the dialog, you can customize the parameters passed here
        customBottomSheetDialog?.show()
    }
    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"  // Accept all file types
        startActivityForResult(intent, PICK_FILE_REQUEST)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("check1", "check1: ")
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            Log.d("ImageGallery", "ImageGallery: $selectedImageUri")
        } else if (requestCode == PICK_GALLERY_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            selectedImageUri?.let { uri ->
                Log.d("ImageUploadURI", "ImageUploadURI: $uri")
                var uriList: MutableList<String> = mutableListOf()
                uriList.add(uri.toString())
                requestViewModel.fileImageUri = uriList
                val file = File(fileHandler.getFilePath(uri))
                var fileList: MutableList<String> = mutableListOf()
                fileList.add(file.toString())
                requestViewModel.filePath = fileList
                Log.d("FileURI", "FileURI: $file")
                fileHandler.uploadFile(file, "image/*") { code ->
                    fileHandler.uploadedFileCodes.add(code.toString())
                }
                val imageSize = fileHandler.getFileSize(uri)
                fileHandler.updateBottomSheet(file, imageSize)
            }
        } else if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val fileUri: Uri? = data.data
            fileUri?.let { uri ->
                val file = File(fileHandler.getFilePath(uri))
                fileHandler.uploadFile(file, "application/octet-stream") {}
                val fileSize = fileHandler.getFileSize(uri)
                fileHandler.updateBottomSheet(file, fileSize)
            }
        } else if (requestCode == IntentIntegrator.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents != null) {
                    val scannedContent = result.contents
                    Log.d("QRScanner", "QRScanner: $scannedContent")
                } else {
                    // Handle when no QR code is found or scanning is canceled
                }
            }
        }
    }

    private fun startQRScanner() {
        val integrator = IntentIntegrator(requireActivity())
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR code")
        integrator.setCameraId(0)  // Use the device's back camera by default
        integrator.initiateScan()
    }
    override fun openGalleryForImage() {
        requestPermissions()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted, proceed with opening the gallery
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_GALLERY_REQUEST)
        } else {
            // Permission not granted, request it from the user
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_EXTERNAL_STORAGE_PERMISSION_REQUEST
            )
        }
    }
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
    }
    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, proceed with file handling
            } else {
                Log.e("PermissionError", "Permission denied to read external storage")
            }
        }
    }
}
