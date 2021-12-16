package com.elroid.dentmap.data

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.lang.reflect.Type


//todo this should be injected via Hilt
class DataManager(
    private val appCtx: Context
) {
    private val moshi: Moshi by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    /**
     * Niue is an outlier - we are only interested in European countries
     */
    suspend fun getEuropeanCountries():List<Country> = getCountries().filter { it.isEuropean()}

    @Suppress("BlockingMethodInNonBlockingContext") //it's appropriate for IO
    suspend fun getCountries():List<Country>{
        return withContext(Dispatchers.Default) {
            val fileName = "countries.json"
            val countriesJson = getStringFromAssets(fileName)
            val countryListType: Type = Types.newParameterizedType(MutableList::class.java, Country::class.java)
            val adapter: JsonAdapter<List<Country>> = moshi.adapter(countryListType)

            adapter.fromJson(countriesJson) ?: throw Exception("Error parsing $fileName")
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext") //it's appropriate for IO
    private suspend fun getStringFromAssets(assetFileName:String):String{
        return withContext(Dispatchers.IO) {
            val buf = StringBuilder()
            val json: InputStream = appCtx.assets.open(assetFileName)
            val reader = BufferedReader(InputStreamReader(json, "UTF-8"))
            var str: String?

            while (reader.readLine().also { str = it } != null) {
                buf.append(str)
                buf.append("\n")
            }

            reader.close()
            buf.toString()
        }
    }
}