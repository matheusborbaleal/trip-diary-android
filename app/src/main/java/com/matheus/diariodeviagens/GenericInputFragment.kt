package com.matheus.diariodeviagens

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class GenericInputFragment : Fragment() {

    private var etGeneric: EditText? = null
    private var tvLabel: TextView? = null

    private var pendingLabel: String? = null
    private var pendingType: Int? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_generic_input, container, false)

        etGeneric = view.findViewById(R.id.etGeneric)
        tvLabel = view.findViewById(R.id.tvLabelGeneric)

        pendingLabel?.let { tvLabel?.text = it }
        pendingType?.let { type ->
            etGeneric?.inputType = type
            if (type == 129 || type == 145 || (type and android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD) != 0) {
                etGeneric?.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        return view
    }

    fun setup(label: String, inputType: Int) {
        if (tvLabel != null && etGeneric != null) {
            tvLabel?.text = label
            etGeneric?.inputType = inputType
            if ((inputType and android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD) != 0) {
                etGeneric?.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        } else {
            pendingLabel = label
            pendingType = inputType
        }
    }

    fun getText(): String = etGeneric?.text?.toString() ?: ""
}