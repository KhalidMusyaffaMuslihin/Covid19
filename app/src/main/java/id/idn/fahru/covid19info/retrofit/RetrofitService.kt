package id.idn.fahru.covid19info.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Imam Fahrur Rofi on 30/07/2020.
 */
object RetrofitService {

    // buat client dari OkHttpClinet
    private val client = OkHttpClient.Builder().build()

    // buat retrofitClient
    private val retrofitClient = Retrofit.Builder()
        // client OkhttpClient dimasukkan ke dalam client
        .client(client)
        // base URL untuk API Covid 19
        .baseUrl("https://api.covid19api.com/")
        // GSON Converter untuk mengubah data JSON dari aPI jadi data class
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // buat fungsi yang bisa diakses oleh kelas lain untuk memanggil retrofitClinet
    fun <T> buildService(service: Class<T>): T = retrofitClient.create(service)
}
