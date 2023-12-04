package br.com.ifmg.musichunter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.view.View.OnClickListener
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.ifmg.musichunter.adapter.MusicItemAdapter
import br.com.ifmg.musichunter.databinding.ActivityMainBinding
import br.com.ifmg.musichunter.model.MusicModel
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dowloader:Runnable
    private lateinit var adapter: MusicItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = ActivityMainBinding.inflate(layoutInflater)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        binding.recycle.layoutManager = LinearLayoutManager(baseContext)
        setContentView(this.binding.root)

        this.handleEvents()
    }

    override fun onClick(component: View) {

    }

    private fun handleEvents(){
        this.binding.search.setEndIconOnClickListener { this.search(
            this.binding.search.editText?.text.toString()
        ) }
    }

    // service
    private fun search(content: String){
        backgroundDownload(content)
    }

    private fun backgroundDownload(content: String){
        val policyThread = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policyThread)

        val url = URL("https://itunes.apple.com/search?term=$content")
        println("URL $url")

        dowloader = Runnable {
            val req: HttpsURLConnection = url.openConnection() as HttpsURLConnection
            var buffer = BufferedReader(InputStreamReader(req.inputStream))

            val response = buffer.readText()

            val musics: List<MusicModel> = this.parserLines(response)

            adapter = MusicItemAdapter(musics)
            binding.recycle.adapter = adapter
        }
        dowloader.run()
    }

    // parser string json
    private fun parserLines(response: String): List<MusicModel> {
        val jsonObject = JsonParser.parseString(response).asJsonObject

        val resultsArray = jsonObject.getAsJsonArray("results")

        val musicList = mutableListOf<MusicModel>()

        for (jsonElement in resultsArray) {
            try {
                val jsonObject = jsonElement.asJsonObject
                val musicModel = MusicModel(
                    jsonObject.getAsJsonPrimitive("trackName").asString,
                    jsonObject.getAsJsonPrimitive("collectionName").asString,
                    jsonObject.getAsJsonPrimitive("releaseDate").asString.toString().split("T")[0],
                    )

                musicList.add(musicModel)
            }catch (e: Exception){
                println("EEERROR $e")
            }
        }

        return musicList
    }

}
