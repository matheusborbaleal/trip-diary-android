package com.matheus.diariodeviagens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class GenericButtonFragment : Fragment() {

    private var btnGeneric: Button? = null
    private var pendingText: String? = null
    private var pendingAction: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_generic_button, container, false)
        btnGeneric = view.findViewById(R.id.btnGeneric)

        pendingText?.let { btnGeneric?.text = it }
        pendingAction?.let { action -> btnGeneric?.setOnClickListener { action() } }

        return view
    }

    fun setup(text: String, action: () -> Unit) {
        if (btnGeneric != null) {
            btnGeneric?.text = text
            btnGeneric?.setOnClickListener { action() }
        } else {
            pendingText = text
            pendingAction = action
        }
    }
}