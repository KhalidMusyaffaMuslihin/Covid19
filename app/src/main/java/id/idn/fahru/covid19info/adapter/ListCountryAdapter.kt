package id.idn.fahru.covid19info.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import id.idn.fahru.covid19info.ChartCountryActivity
import id.idn.fahru.covid19info.databinding.ListCountryBinding
import id.idn.fahru.covid19info.pojo.CountriesItem

/**
 * Created by Imam Fahrur Rofi on 29/07/2020.
 */
class ListCountryAdapter : RecyclerView.Adapter<ListCountryVH>(), Filterable {
    // buat variabel list/arraylist untuk menyimpan data di dalam adapter
    private val dataCountry = mutableListOf<CountriesItem>()
    private var dataFiltered = mutableListOf<CountriesItem>()

    // buat fungsi addData agar kelas lain bisa mengisi data kedalam recyclerview adapter
    fun addData(listCountry: List<CountriesItem>) {
        // bersihkan data lama jika ada menggunakan clear()
        dataCountry.clear()
        // tambahkan data set baru menggunakan addAll
        dataCountry.addAll(listCountry)
        dataFiltered.addAll(listCountry)
        // beritahu RecyclerView Adapter karena ada perubahan Data Set
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCountryVH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListCountryBinding.inflate(inflater, parent, false)
        return ListCountryVH(binding)
    }

    override fun getItemCount(): Int {
        // ukuran dari dataCountry
        return dataFiltered.size
    }

    // buat fungsi cek angka genap
    private fun isEven(number: Int): Boolean {
        // jika angka yang diinputkan dibagi 2 bersisa 0 atau tidak ada sisa,
        // maka hasilnya true / genap
        // semisal angka 0 dibagi 2, maka hasilnya 0 sisanya 0 dan dianggap genap
        // semisal angka 1 dibagi 2, maka hasilnya 0 sisanya 1 dan dianggap ganjil
        // semisal angka 5 dibagi 2, maka hasilnya 2 sisanya 1 dan dianggap ganjil
        // semisal angka 6 dibagi 2, maka hasilnya 3 sisanya 0 dan dianggap genap
        return number % 2 == 0
    }

    override fun onBindViewHolder(holder: ListCountryVH, position: Int) {
        // memilih data sesuai posisi item recyclerview
        val data = dataFiltered[position]

        // data tersebut ditempelkan ke dalam view menggunakan holder / ViewHolder
        // tambahkan isEven berisi position untuk mengetahui posisinya genap atau ganjil
        holder.bind(data, isEven(position))

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, ChartCountryActivity::class.java).apply {
                putExtra("DATA_COUNTRY", data)
            }
            it.context.startActivity(intent)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults? {
                val filterResult = FilterResults()
                Log.e("constraint", constraint.toString())
                // hapus dulu isi dataFiltered
                dataFiltered.clear()
                // mengisi dataFiltered dengan semua data yang ada di dataCountry jika tidak ada keyword
                if (constraint.isNullOrEmpty()) {
                    dataFiltered.clear()
                    // jadikan dataCountry sebagai isi dari dataFiltered
                    dataFiltered.addAll(dataCountry)
                } else {  // Jika ada keyword, maka lakukan perulangan untuk menyaring data berdasarkan keyword
                    // lakukan perulangan data pada dataCountry untuk mencari data berisi keyword
                    dataCountry.forEach { data ->
                        val countryName = data.country
                        // val keyword = constraint
                        countryName?.let { country ->  // jika countryName tidak null maka berinama country
                            // jika nama negara berisi keyword (setel true agar huruf kecil besar tidak berpengaruh)
                            if (country.contains(constraint, true)) {
                                // tambahkan data ke dalam resultFilter
                                dataFiltered.add(data)
                            }
                        }
                    }
                }
                // jadikan resultFilter sebagai isi dari dataFiltered
                filterResult.values = dataFiltered
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                // beritahu adapter recyclerview jika data set telah berubah
                notifyDataSetChanged()
            }
        }
    }
}
