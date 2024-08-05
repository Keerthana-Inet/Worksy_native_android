package com.worksy.hr.art.views.fragments.claimRequest

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.CreateClaimFormLayoutBinding
import com.worksy.hr.art.models.claimRequestResponse.AddClaimSettingsResponse
import com.worksy.hr.art.models.claimRequestResponse.AddItemRequest
import com.worksy.hr.art.models.claimRequestResponse.GetClaimFormResponse
import com.worksy.hr.art.repository.data.localdatabse.SharedPrefManager
import com.worksy.hr.art.utils.CustomSpinner
import com.worksy.hr.art.utils.CustomTextField
import com.worksy.hr.art.utils.ImageFileHandler
import com.worksy.hr.art.utils.ShowDatePickerUtils
import com.worksy.hr.art.utils.UIResult
import com.worksy.hr.art.viewmodels.ClaimViewModel
import com.worksy.hr.art.views.activity.Homeactivity.Companion.hideLoading
import com.worksy.hr.art.views.activity.Homeactivity.Companion.showLoading
import com.worksy.hr.art.views.adapter.CustomAdapter
import com.worksy.hr.art.views.adapter.claimRequestListing.CustomFieldAdapter

public class CustomBottomSheetDialog(
    private val context: Context,
    private val requestEditBtn: Boolean,
    private var requestEditFromDetail: Boolean? = null,
    private val viewModel: ClaimViewModel,
    private val progressData: AddItemRequest.PendingItem? = null,
    private val itemData: AddItemRequest.PendingItem.Item? = null,
    private val viewLifecycleOwner: LifecycleOwner,
    private val appPref: SharedPrefManager?=null,
    private val gson: Gson?=null,
    private var requireActivity: FragmentActivity
) : BottomSheetDialogFragment() {
    private val dialog: BottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog)
    private lateinit var binding: CreateClaimFormLayoutBinding
    private var claimGroupList = listOf<AddClaimSettingsResponse.Data.Group>()
    private var claimItemList = listOf<AddClaimSettingsResponse.Data.Group.Item>()
    private var claimOptionList = listOf<AddClaimSettingsResponse.Data.Group.Item.Fields.CustomField.Option>()
    private lateinit var rvCustomAdapter: CustomFieldAdapter
    private lateinit var fileHandler: ImageFileHandler
    private var galleryResultListener: GalleryResultListener? = null
    private var galleryInteractionListener: GalleryInteractionListener? = null
    private val customSpinner= CustomSpinner(context)
    private val customTextField = CustomTextField(context)
    fun show() {
        binding = CreateClaimFormLayoutBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)
        setupViews(requestEditBtn, requestEditFromDetail!!,progressData, itemData)
        dialog.setOnShowListener {
            setupBottomSheetBehavior()
        }
        dialog.show()
    }
    interface GalleryInteractionListener {
        fun openGalleryForImage()
    }



    fun setGalleryInteractionListener(listener: GalleryInteractionListener) {
        this.galleryInteractionListener = listener
    }
    interface GalleryResultListener {
        fun onGalleryResult(uri: Uri)
    }
    fun setGalleryResultListener(listener: GalleryResultListener) {
        this.galleryResultListener = listener
    }
    private fun AddCaimApi(
        claimGroupName: String? = null,
        claimItem: String? = null,
        clickedItem: GetClaimFormResponse.ClaimFormData.ClaimForm.Item?=null
    ) {
        viewModel.addClaimSettingList()
        viewModel.addClaimSettingList.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is UIResult.Success -> {
                        hideLoading()
                        it.data?.let {data->
                            if(!it.data.data?.groups.isNullOrEmpty()){
                                claimGroupList=data.data.groups
                                viewModel.claimGroupStringList.clear()
                                for (group in claimGroupList) {
                                    viewModel.claimGroupStringList.add(group.groupName ?: "Unknown")
                                }
                                Log.d("ClaimGRoup", "ClaimGRoup: "+viewModel.claimGroupStringList)
                                if (requestEditBtn) {
                                    claimGroupListSpinner(binding, claimGroupName,claimItem)
                                }else if(requestEditFromDetail!!.equals(true)){
                                    updateClaimGroupSpinner(binding, claimGroupName, clickedItem)
                                }
                                else {
                                    claimGroupListSpinner(binding)
                                }
                            }
                        }

                    }

                    is UIResult.Error -> {
                        hideLoading()
                        Toast.makeText(context,"Something went wrong, Please try again later",Toast.LENGTH_SHORT).show()
                    }

                    is UIResult.Loading -> {
                        showLoading()
                    }

                    else -> {

                    }
                }
            }
        }
    }

    private fun updateClaimGroupSpinner(
        binding: CreateClaimFormLayoutBinding,
        selectedGroupName: String? = null,
        clickedItem: GetClaimFormResponse.ClaimFormData.ClaimForm.Item?
    ) {
        val updatedClaimGroupList = if (selectedGroupName != null) {
            val updatedList = mutableListOf<String>()
            if (!viewModel.claimGroupStringList.contains(selectedGroupName)) {
                updatedList.add(selectedGroupName)
            }
            updatedList.addAll(viewModel.claimGroupStringList)
            updatedList
        } else {
            viewModel.claimGroupStringList
        }

        // Create and set the adapter with the updated list
        val spinCashAdapter = CustomAdapter(requireActivity, updatedClaimGroupList)
        binding.claimGroupSpinner.adapter = spinCashAdapter

        // Set the initial selection if a group name is provided
        if (selectedGroupName != null) {
            val selectedGroupIndex = updatedClaimGroupList.indexOf(selectedGroupName)
            if (selectedGroupIndex != -1) {
                binding.claimGroupSpinner.setSelection(selectedGroupIndex)
            }
            binding.claimGroupSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Nothing to do if nothing selected
                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long,
                    ) {
                        spinCashAdapter.setSelectedPosition(position)
                        this@CustomBottomSheetDialog.binding.apply {
                            if (!claimGroupList.isNullOrEmpty()) {
                                viewModel.claimGroupId = claimGroupList[position].groupId
                                viewModel.claimGroupName = claimGroupList[position].groupName
                                claimItemList = claimGroupList[position].items
                                claimItemSpinner(binding, position, clickedItem?.claimItem)  // Update the claim item spinner
                                binding.tvClaimGroup.text = claimGroupList[position].groupName
                            }
                        }
                    }
                }
        }
    }

    private fun setupViews(
        requestEditBtn: Boolean,
        requestEditFromDetail: Boolean,
        progressData: AddItemRequest.PendingItem?,
        itemData: AddItemRequest.PendingItem.Item?
    ) {
        binding.apply {
            if(requestEditFromDetail){
            viewModel.clickedItemRequest.observe(viewLifecycleOwner, Observer { clickedItem ->
                var requestDetailData= clickedItem
                if(requestDetailData!=null){
                    etProject.setText(requestDetailData.reason)
                    remarksEt.setText(requestDetailData.remarks)
                    tax.setText(requestDetailData.tax)
                    amountTax.setText(requestDetailData.requestedAmount)
                    date.setText(requestDetailData.transactionDate)
                    etPaxStaff.setText(requestDetailData.paxStaff)
                    etPaxCustomerExisting.setText(requestDetailData.paxClientExisting)
                    etPaxCustomerPotential.setText(requestDetailData.paxClientPotential)
                    etPaxPrincipal.setText(requestDetailData.paxPrincipal)
                }
                viewModel.groupName.observe(viewLifecycleOwner, Observer { groupName ->
                    Log.d("EDitGroup", "EDitGroup: "+groupName)
                    AddCaimApi(groupName.toString(),null,clickedItem)
                })
            })}else{
                AddCaimApi()
            }

            ShowDatePickerUtils.setCurrentDate(binding, viewModel)
            titleTxt.text = if (requestEditBtn || requestEditFromDetail) context.getString(R.string.edit_item)  else context.getString(R.string.new_claim_item_txt)

            backImg.setOnClickListener { dialog.dismiss() }
            cancelBtn.setOnClickListener {
                dialog.dismiss()
                viewModel.addAnotherBtnTrigger = true
                viewModel.updateAdapter()
            }
            fileHandler = ImageFileHandler(
                context = context,
                requireActivity = requireActivity,
                viewModel = viewModel,
                viewLifecycleOwner = viewLifecycleOwner,
                bottomSheetBinding = binding // Pass your bottom sheet binding if needed
            )
            if (requestEditBtn || requestEditFromDetail) {
                addBtn.text = "Save"
                addAnotherBtn.text = "Delete"
                if (addAnotherBtn.text.equals("Delete")) {
                    addAnotherBtn.setOnClickListener {
                        deleteItem(viewModel.claimGroupName,true)
                        dialog.dismiss()
                    }
                }
            } else {
                addAnotherBtn.text = "Add Another"
                addBtn.text = "Add"
            }

            customSpinner.setupSpinner(binding.claimGroupSpinner, viewModel.claimGroups, itemData?.claimGroupName)
            customSpinner.setupSpinner(binding.spinner, viewModel.claimItems, itemData?.claimItem)
            setUpTextField(binding)
            if (progressData != null) {
                populateProgressFields(progressData)
            }

            if (itemData != null) {
                populateItemFields(itemData)
            }

            if(addAnotherBtn.text.equals("Add Another")){
                addAnotherBtn.setOnClickListener {
                    val newItem = createNewItem()
                    if (validateFields()) {
                        addNewClaim(viewModel.claimGroupName, newItem, false,itemData)
                        if (newItem.claimItem.isNotEmpty()) {
                            multipleItemsAdding(binding)
                        }
                        viewModel.addAnotherBtnTrigger = true
                        bindingTextField(binding)
                    }
                }
            }
            addBtn.setOnClickListener {
                val newItem = createNewItem()
                if (validateFields()) {
                    if(requestEditBtn.equals(true)) {
                        addNewClaim(viewModel.claimGroupName,newItem,true,itemData)
                    }else{
                        addNewClaim(viewModel.claimGroupName,newItem,false,itemData)
                    }
                    bindingTextField(binding)
                    dialog.dismiss()
                }
            }

            relUploadFile.setOnClickListener {
                galleryInteractionListener?.openGalleryForImage()
            }
            deleteImg.setOnClickListener { relImageUploadedFile.visibility = View.GONE }
            dateTxt.setOnClickListener { startDatePicker() }
        }
    }

    private fun setUpTextField(binding: CreateClaimFormLayoutBinding) {
        binding.apply {
            customTextField.setupTextField(amountTax)
            customTextField.setupTextField(tax)
            customTextField.setupTextField(remarksEt)
            customTextField.setupTextField(etProject)
            customTextField.setupTextField(etPaxStaff)
            customTextField.setupTextField(etPaxCustomerExisting)
            customTextField.setupTextField(etPaxCustomerPotential)
            customTextField.setupTextField(etPaxPrincipal)
        }
    }

    private fun bindingTextField(binding: CreateClaimFormLayoutBinding) {
        binding.apply {
            viewModel.claimAmount = customTextField.getText(amountTax)
            viewModel.claimTax = customTextField.getText(tax)
            viewModel.claimRemarks = customTextField.getText(remarksEt)
            viewModel.claimReasons = customTextField.getText(etProject)
            viewModel.paxStaff = customTextField.getText(etPaxStaff)
            viewModel.paxClientExisting = customTextField.getText(etPaxCustomerExisting)
            viewModel.paxClientPotential = customTextField.getText(etPaxCustomerPotential)
            viewModel.paxPrincipal = customTextField.getText(etPaxPrincipal).toString()
        }
        viewModel.updateAdapter()
    }


    private fun multipleItemsAdding(binding: CreateClaimFormLayoutBinding) {
        Toast.makeText(context,"Item Added Successfully",Toast.LENGTH_SHORT).show()
        binding.apply {
            amountTax.text?.clear()
            remarksEt.text?.clear()
            tax.text?.clear()
            etProject.text?.clear()
        }
    }

    private fun deleteItem(
        groupName: String,
        editRequest: Boolean,
    ) {
        // Retrieve the existing data from SharedPreferences
        val existingDataJson = appPref!!.getClaimRequest()
        val existingData: AddItemRequest = if (!existingDataJson.isNullOrEmpty()) {
            gson!!.fromJson(existingDataJson, object : TypeToken<AddItemRequest>() {}.type)
        } else {
            AddItemRequest()
        }

        val existingGroup = existingData.pendingItems.find { it.groupName == groupName }
        val existingItem= existingGroup?.items?.find { it.claimItem == viewModel.claimItemName }
        if (existingGroup != null) {
            if (editRequest) {
                if(existingItem?.claimItem == viewModel.claimItemName) {
                    existingGroup.items.remove(existingItem)
                }else{
                    Toast.makeText(context,"Select correct Item name", Toast.LENGTH_SHORT).show()
                }
                if(existingGroup.items.size == 0){
                    existingData.pendingItems.remove(existingGroup)
                }

            }
        }
        // Save the updated data back to SharedPreferences
        val updatedDataJson = gson!!.toJson(existingData)
        appPref.setClaimRequest(updatedDataJson)
       viewModel.updateAdapter()
    }
    private fun claimGroupListSpinner(binding: CreateClaimFormLayoutBinding, selectedGroupName: String? = null, selectedItemName: String? = null) {
        val spinCashAdapter = CustomAdapter(context, claimGroupList.map { it.groupName })
        binding.claimGroupSpinner.adapter = spinCashAdapter

        // Find the index of the selected group
        val selectedGroupIndex = claimGroupList.indexOfFirst { it.groupName == selectedGroupName }

        // Set the initial selection if a group name is provided
        if (selectedGroupIndex != -1) {
            binding.claimGroupSpinner.setSelection(selectedGroupIndex)
        }

        binding.claimGroupSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Nothing to do if nothing selected
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    spinCashAdapter.setSelectedPosition(position)
                    this@CustomBottomSheetDialog.binding.apply {
                        if (!claimGroupList.isNullOrEmpty()) {
                            viewModel.claimGroupId = claimGroupList[position].groupId
                            viewModel.claimGroupName = claimGroupList[position].groupName
                            claimItemList = claimGroupList[position].items
                            claimItemSpinner(binding, position, selectedItemName)  // Update the claim item spinner
                            binding.tvClaimGroup.text = claimGroupList[position].groupName
                        }
                    }
                }
            }
    }

    private fun claimItemSpinner(binding: CreateClaimFormLayoutBinding, groupPosition: Int, selectedItemName: String? = null) {
        val items = claimItemList.map { it.itemName }

        val spinCashAdapter = CustomAdapter(context, items)
        binding.spinner.adapter = spinCashAdapter

        // Find the index of the selected item
        val selectedItemIndex = claimItemList.indexOfFirst { it.itemName == selectedItemName }

        // Set the initial selection if an item name is provided
        if (selectedItemIndex != -1) {
            binding.spinner.setSelection(selectedItemIndex)
        }

        binding.spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Nothing to do if nothing selected
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    spinCashAdapter.setSelectedPosition(position)
                    this@CustomBottomSheetDialog.binding.apply {
                        if (!claimItemList.isNullOrEmpty()) {
                            val selectedItem = claimItemList[position]
                            viewModel.claimItemId = selectedItem.itemId
                            viewModel.claimItemName = selectedItem.itemName
                            claimOptionList = selectedItem.fields.customFields.flatMap { it.options }

                            // Reason
                            if (selectedItem.fields.reason.isNotEmpty()) {
                                binding.relProject.visibility = View.VISIBLE
                                binding.etProject.hint = "Reason (${selectedItem.fields.reason})"
                            } else {
                                binding.relProject.visibility = View.GONE
                            }


                            // Remark
                            if (selectedItem.fields.remarks.isNotEmpty()) {
                                binding.remarksType.text = "(${selectedItem.fields.remarks})"
                                binding.relRemarks.visibility = View.VISIBLE
                            } else {
                                binding.relRemarks.visibility = View.GONE
                            }

                            // Tax
                            val taxField = selectedItem.fields.tax
                            if (!taxField.isNullOrEmpty()) {
                                binding.taxLyt.visibility = if (taxField == "hide") View.GONE else View.VISIBLE
                            } else {
                                binding.taxLyt.visibility = View.GONE
                            }

                            // Entertainment Pax
                            if (viewModel.claimGroupId == "4") {
                                binding.relPaxStaff.visibility = View.VISIBLE
                                binding.relPaxCustomerExisting.visibility = View.VISIBLE
                                binding.relPaxCustomerPotential.visibility = View.VISIBLE
                                binding.relPaxPrincipal.visibility = View.VISIBLE
                            } else {
                                binding.relPaxStaff.visibility = View.GONE
                                binding.relPaxCustomerExisting.visibility = View.GONE
                                binding.relPaxCustomerPotential.visibility = View.GONE
                                binding.relPaxPrincipal.visibility = View.GONE
                            }

                            // Custom field recyclerview visibility
                            binding.relCustomfield.visibility = if (selectedItem.fields.customFields.isNotEmpty()) View.VISIBLE else View.GONE

                            // Adding adapter
                            rvCustomAdapter = CustomFieldAdapter(context, claimGroupList[groupPosition]) {}
                            binding.recyclerCustomfield.layoutManager = LinearLayoutManager(context)
                            binding.recyclerCustomfield.adapter = rvCustomAdapter
                        }
                    }
                }
            }
    }

    private fun populateProgressFields(progressData: AddItemRequest.PendingItem) {
        binding.apply {
            claimGroupSpinner.setSelection(viewModel.claimGroupName.indexOf(progressData.items[0].claimGroupName))
            spinner.setSelection(viewModel.claimItemName.indexOf(progressData.items[0].claimItem))
            etProject.setText(progressData.items[0].reason)
            remarksEt.setText(progressData.items[0].remarks)
            amountTax.setText(progressData.items[0].requestedAmount)
            tax.setText(progressData.items[0].tax)
            date.setText(progressData.items[0].transactionData)
            viewModel.fileSizeString = progressData.items[0].fileSize.toString()
            viewModel.filePathString = progressData.items[0].filePath.toString()
            relImageUploadedFile.visibility = View.VISIBLE
            imgName.text = viewModel.filePathString
            deleteImg.visibility = View.GONE
            imgSize.text = viewModel.fileSizeString
            AddCaimApi(
                progressData.items[0].claimGroupName,
                progressData.items[0].claimItem,null
            )
        }
    }

    private fun populateItemFields(itemData: AddItemRequest.PendingItem.Item) {
        binding.apply {
            claimGroupSpinner.setSelection(viewModel.claimGroupName.indexOf(itemData.claimGroupName))
            spinner.setSelection(viewModel.claimItemName.indexOf(itemData.claimItem))
            etProject.setText(itemData.reason)
            remarksEt.setText(itemData.remarks)
            amountTax.setText(itemData.requestedAmount)
            tax.setText(itemData.tax)
            date.setText(itemData.transactionData)
            viewModel.fileSizeString = itemData.fileSize.toString()
            viewModel.filePathString = itemData.filePath.toString()
            relImageUploadedFile.visibility = View.VISIBLE
            imgName.text = viewModel.filePathString
            deleteImg.visibility = View.GONE
            imgSize.text = viewModel.fileSizeString
            AddCaimApi(itemData.claimGroupName, itemData.claimItem,null)
        }
    }

    private fun createNewItem(): AddItemRequest.PendingItem.Item {
        binding.apply {
             return AddItemRequest.PendingItem.Item(
                claimGroupId = viewModel.claimGroupId,
                claimGroupName = claimGroupSpinner.selectedItem.toString(),
                claimItemId = viewModel.claimItemId,
                claimItem = spinner.selectedItem.toString(),
                fileUrls = viewModel.fileImageUri,
                filePath = viewModel.filePath,
                fileSize = viewModel.fileSize,
                reason = customTextField.getText(etProject).trim(),
                remarks = customTextField.getText(remarksEt).trim(),
                requestedAmount = customTextField.getText(amountTax).trim(),
                tagName = "",
                tax = customTextField.getText(tax).trim(),
                transactionData = date.text.toString().trim(),
                paxStaff = customTextField.getText(etPaxStaff).trim(),
                paxClientExisting = customTextField.getText(etPaxCustomerExisting).trim(),
                paxClientPotential = customTextField.getText(etPaxCustomerPotential).trim(),
                paxPrincipal = customTextField.getText(etPaxPrincipal).trim(),
            )
        }
    }
    private fun addNewClaim(groupName: String, newItem: AddItemRequest.PendingItem.Item, editRequest: Boolean, itemData: AddItemRequest.PendingItem.Item?) {
        // Retrieve the existing data from SharedPreferences
        val existingDataJson = appPref!!.getClaimRequest()
        val existingData: AddItemRequest = if (!existingDataJson.isNullOrEmpty()) {
            gson!!.fromJson(existingDataJson, object : TypeToken<AddItemRequest>() {}.type)
        } else {
            AddItemRequest()
        }
        val editReqExistGroup = existingData.pendingItems.find { it.groupName != groupName }

        //If group name same to existing then pending item add respective groupname
        val existingGroup = existingData.pendingItems.find { it.groupName == groupName }
        if (existingGroup != null) {
            if (editRequest) {
                // Find the existing item within the group by item name
                //If same item name then other value need to change means it will update with same item
                val existingItemIndex = existingGroup.items.indexOfFirst { it.claimItem == newItem.claimItem }
                if (existingItemIndex != -1) {
                    // Replace the existing item with the new item
                    existingGroup.items[existingItemIndex] = newItem
                    Log.d("newItem", "Updated Item: $newItem")
                } else {
                    existingGroup.items.remove(itemData)
                    // Add the new item
                    existingGroup.items.add(newItem)
                }
            } else {
                // Add the new item since it's not an edit request
                existingGroup.items.add(newItem)
                Log.d("newItem", "Added New Item: $newItem")
            }
        } else {
            // Create a new group and add the item
            val newGroup = AddItemRequest.PendingItem(
                currency = "",
                groupName = groupName,
                items = mutableListOf(newItem),
                total = newItem.requestedAmount

            )
            existingData.pendingItems.add(newGroup)
            Log.d("newGroup", "Created New Group with Item: $newItem")
        }

        // Save the updated data back to SharedPreferences
        val updatedDataJson = gson!!.toJson(existingData)
        appPref!!.setClaimRequest(updatedDataJson)

    }

    private fun validateFields(): Boolean {
        var isValid = true
        binding.apply {
            if (etProject.text.toString().trim().isEmpty()) {
                etProject.error = "Reason should not be empty"
                isValid = false
            }
            if (amountTax.text.toString().trim().isEmpty()) {
                amountTax.error = "Amount should not be empty"
                isValid = false
            }
            if (remarksEt.text.toString().trim().isEmpty()) {
                remarksEt.error = "Remarks should not be empty"
                isValid = false
            }
        }
        return isValid
    }

    private fun setupBottomSheetBehavior() {
        val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            val screenHeight = Resources.getSystem().displayMetrics.heightPixels
            val customPeekHeight = (screenHeight * 0.99).toInt()
            behavior.peekHeight = customPeekHeight
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
    private fun startDatePicker() {
        ShowDatePickerUtils.startDatePicker(context, binding, viewModel, true)
    }
}
