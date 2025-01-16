package ro.pub.cs.systems.eim.practicaltest02v9

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import kotlin.concurrent.thread

class PracticalTest02MainActivityv9 : AppCompatActivity() {

    private lateinit var resultTextView: TextView
    private val BROADCAST_ACTION = "ro.pub.cs.systems.eim.practicaltest02v9.ANAGRAM_RESULTS"

    private val anagramReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val result = intent?.getStringExtra("anagram_results")
            resultTextView.text = result
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practical_test02v9_main)

        val wordEditText = findViewById<EditText>(R.id.wordEditText)
        val minLengthEditText = findViewById<EditText>(R.id.minLengthEditText)
        val searchButton = findViewById<Button>(R.id.searchButton)
        resultTextView = findViewById(R.id.resultTextView)

        registerReceiver(anagramReceiver, IntentFilter(BROADCAST_ACTION))

        searchButton.setOnClickListener {
            val word = wordEditText.text.toString()
            val minLength = minLengthEditText.text.toString().toIntOrNull() ?: 0
            fetchAnagrams(word, minLength)
        }
    }

    private fun fetchAnagrams(word: String, minLength: Int) {
        thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("http://www.anagramica.com/all/$word")
                    .build()
                val response = client.newCall(request).execute()
                val jsonData = response.body?.string()

                // a) Afișare răspuns complet în Logcat
                Log.d("API_RESPONSE", "Răspuns complet: $jsonData")

                val anagrams = JSONObject(jsonData).getJSONArray("all")

                // b) Parsare și afișare rezultate în Logcat
                val filteredAnagrams = mutableListOf<String>()
                for (i in 0 until anagrams.length()) {
                    val anagram = anagrams.getString(i)
                    if (anagram.length >= minLength) {
                        filteredAnagrams.add(anagram)
                    }
                }
                Log.d("FILTERED_ANAGRAMS", "Anagrame filtrate: $filteredAnagrams")

                // c) Trimiterea rezultatelor prin Broadcast
                val intent = Intent(BROADCAST_ACTION)
                intent.putExtra("anagram_results", filteredAnagrams.joinToString(", "))
                sendBroadcast(intent)

            } catch (e: Exception) {
                Log.e("API_ERROR", "Eroare la preluarea datelor: ${e.message}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(anagramReceiver)
    }
}
