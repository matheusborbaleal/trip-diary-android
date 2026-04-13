package com.matheus.diariodeviagens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val tvUserEmail = view.findViewById<TextView>(R.id.tvUserEmail)
        val etCep = view.findViewById<EditText>(R.id.etCep)
        val btnSearchCep = view.findViewById<Button>(R.id.btnSearchCep)
        val tvAddressResult = view.findViewById<TextView>(R.id.tvAddressResult)
        val pbCep = view.findViewById<ProgressBar>(R.id.pbCep)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        tvUserEmail.text = FirebaseAuth.getInstance().currentUser?.email

        val retrofit = Retrofit.Builder()
            .baseUrl("https://viacep.com.br/ws/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ViaCepService::class.java)

        btnSearchCep.setOnClickListener {
            val cep = etCep.text.toString()
            if (cep.length == 8) {
                pbCep.visibility = View.VISIBLE
                service.getAddress(cep).enqueue(object : Callback<AddressResponse> {
                    override fun onResponse(call: Call<AddressResponse>, response: Response<AddressResponse>) {
                        pbCep.visibility = View.GONE
                        val addr = response.body()
                        if (addr != null && addr.cep != null) {
                            tvAddressResult.text = "${addr.logradouro}, ${addr.bairro}\n${addr.localidade} - ${addr.uf}"
                        }
                    }
                    override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                        pbCep.visibility = View.GONE
                    }
                })
            }
        }

        btnLogout.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                AppDatabase.getDatabase(requireContext()).tripDao().deleteAllTrips()

                FirebaseAuth.getInstance().signOut()

                withContext(Dispatchers.Main) {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }

        return view
    }
}