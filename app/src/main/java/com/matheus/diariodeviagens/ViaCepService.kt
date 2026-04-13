package com.matheus.diariodeviagens

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

data class AddressResponse(
    val cep: String,
    val logradouro: String,
    val bairro: String,
    val localidade: String,
    val uf: String
)

interface ViaCepService {
    @GET("{cep}/json/")
    fun getAddress(@Path("cep") cep: String): Call<AddressResponse>
}