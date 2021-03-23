package id.idn.fahru.covid19info

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import id.idn.fahru.covid19info.databinding.ActivityChartCountryBinding
import id.idn.fahru.covid19info.pojo.CountriesItem
import id.idn.fahru.covid19info.pojo.ResponseCountry
import id.idn.fahru.covid19info.retrofit.CovidInterface
import id.idn.fahru.covid19info.retrofit.RetrofitService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ChartCountryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityChartCountryBinding
    private lateinit var dataCountry : CountriesItem
    private val dayCases = mutableListOf<String>()
    private val dataConfirmed = mutableListOf<BarEntry>()
    private val dataDeath = mutableListOf<BarEntry>()
    private val dataRecovered = mutableListOf<BarEntry>()
    private val dataActive = mutableListOf<BarEntry>()

    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS'Z'", Locale.getDefault())
    val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = layoutInflater
        binding = ActivityChartCountryBinding.inflate(inflater)
        setContentView(binding.root)

        dataCountry = intent.getParcelableExtra("DATA_COUNTRY") as CountriesItem

        binding.run {
            txtCountryChart.text = dataCountry.countryCode
            txtCurrent.text = dataCountry.totalConfirmed.toString()
            txtNewConfirmedCurrent.text = dataCountry.newConfirmed.toString()
            txtNewDeathsCurrent.text = dataCountry.newDeaths.toString()
            txtNewRecoveredCurrent.text = dataCountry.newRecovered.toString()
            txtTotalRecoveredCurrent.text = dataCountry.totalRecovered.toString()
            txtTotalDeathsCurrent.text = dataCountry.totalDeaths.toString()
            txtTotalConfirmedCurrent.text = dataCountry.totalConfirmed.toString()

            Glide.with(root)
                .load("https://www.countryflags.io/${dataCountry.countryCode}/flat/64.png")
                .into(imgFlagChart)
        }

        dataCountry.slug?.let { slug ->
            getCountry(slug)
        }
    }

    private fun getCountry(countryName : String){
        val retrofit = RetrofitService.buildService(CovidInterface::class.java)
        lifecycleScope.launch {
            val countryData = retrofit.getCountryData(countryName)
            if (countryData.isSuccessful){
                // buat variabel baru dari data tsb
                val dataCovid = countryData.body() as List<ResponseCountry>

                //lakukan perulangan item dari data covid
                dataCovid.forEachIndexed { index, responseCountry ->
                    val barConfirmed = BarEntry(index.toFloat(), responseCountry.Confirmed?.toFloat() ?: 0f )
                    val barDeath = BarEntry(index.toFloat(), responseCountry.Confirmed?.toFloat() ?: 0f )
                    val barRecovered = BarEntry(index.toFloat(), responseCountry.Confirmed?.toFloat() ?: 0f )
                    val barActive = BarEntry(index.toFloat(), responseCountry.Confirmed?.toFloat() ?: 0f )

                    dataConfirmed.add(barConfirmed)
                    dataRecovered.add(barRecovered)
                    dataActive.add(barActive)
                    dataDeath.add(barDeath)

                    responseCountry.Date?.let {itemDate ->
                        val date = inputDateFormat.parse(itemDate)
                        val formattedDate = outputDateFormat.format(date as Date)
                        dayCases.add(formattedDate)
                    }
                }

                binding.chartView.axisLeft.axisMinimum = 0f
                val labelSumbuX = binding.chartView.xAxis
                labelSumbuX.run {
                    valueFormatter = IndexAxisValueFormatter(dayCases)
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                    setCenterAxisLabels(true)
                    isGranularityEnabled = true
                }

                val barDataConfirmed = BarDataSet(dataConfirmed, "Confirmed")
                val barDataRecovered = BarDataSet(dataConfirmed, "Recovered")
                val barDataDeath = BarDataSet(dataConfirmed, "Death")
                val barDataActive = BarDataSet(dataConfirmed, "Active")

                barDataConfirmed.setColors(Color.parseColor("#F44336"))
                barDataRecovered.setColors(Color.parseColor("#FFEB3B"))
                barDataDeath.setColors(Color.parseColor("#03DAC5"))
                barDataActive.setColors(Color.parseColor("#2196F3"))

                //membuat variabel data berisi semua barData
                val dataChart = BarData(barDataConfirmed, barDataRecovered, barDataActive, barDataDeath)

                //buat variabel berisi spasi
                val barSpace = 0.02f
                val groupSpace = 0.3f
                val groupCount = 4f

                //modifikasi chartView programatically
                binding.chartView.run {
                    // tambahkan datachart kedalam ChartView
                    data = dataChart
                    // invalidate untuk mengganti data sebelumnya (jika ada) dengan data yang baru
                    invalidate()
                    setNoDataTextColor(R.color.dkGrey)
                    // chartview bisa ditap atau di zoom
                    setTouchEnabled(true)
                    description.isEnabled = false
                    xAxis.axisMinimum = 0f
                    setVisibleXRangeMaximum(
                            0f + barData.getGroupWidth(
                                groupSpace, barSpace

                            ) * groupCount
                    )
                    groupBars(0f, groupSpace, barSpace)

                }


            }
        }
    }
}