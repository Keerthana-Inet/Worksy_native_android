package com.worksy.hr.art.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.worksy.hr.art.R
import com.worksy.hr.art.databinding.DialogLogOutBinding

class LogOutDialogDialog : DialogFragment() {
    private lateinit var updateFunc: (String) -> Unit
    private var _binding: DialogLogOutBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun stupClick() {
        _binding?.apply {
            tvCancel.setOnClickListener {
                dismiss()
            }
            tvOk.setOnClickListener {
                dismiss()
                // tokenunlimited()
                updateFunc("success")
            }
        }

    }

    fun addSuccessListerner(update: (String) -> Unit) {
        updateFunc = update
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = DialogLogOutBinding.inflate(inflater, container, false)
        stupClick()
        return _binding?.root
    }

    companion object {
        fun newInstance(): LogOutDialogDialog {
            return LogOutDialogDialog().apply {
            }
        }
    }
}