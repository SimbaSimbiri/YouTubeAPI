package com.simbiri.youtubeapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import java.text.SimpleDateFormat

private lateinit var recyclerVideos: RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerVideos = findViewById(R.id.recyclerViewYoutubeLive)


        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerVideos.layoutManager = layoutManager

        val adapter = LiveVideoAdapter(this, YoutubeVideos())

        recyclerVideos.adapter = adapter

        val delayedTime: Long = 500

        val rootView = window.decorView.rootView
        rootView.postDelayed({ recyclerVideos.adapter!!.notifyDataSetChanged() }, delayedTime)


    }

    fun YoutubeVideos(): ArrayList<Video> {

        val API_KEY = YoutubeKeyProvider.keyProvider(this, 0)
        val channelD = YoutubeKeyProvider.keyProvider(this, 1)

        var videoList: ArrayList<Video> = ArrayList()

        val client = AsyncHttpClient()

        val params = RequestParams()
        params["limit"] = "40"
        params["page"] = "1"

        client["https://www.googleapis.com/youtube/v3/search?key=${API_KEY}&channelId=${channelD}&part=snippet,id&order=date&maxResults=30", params, object :
            JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                // Access the JSON object response to get your list of titles and videoIds which are picasso urls

                val items = json.jsonObject.getJSONArray("items")

                for (jsonElementPos in 0 until items.length()) {
                    val snippet = items.getJSONObject(jsonElementPos).getJSONObject("snippet")
                    val title = snippet.getString("title")
                    val publishTime = snippet.getString("publishedAt")

                    val parsedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(publishTime)
                    val formattedDate = SimpleDateFormat("yyyy-MM-dd").format(parsedDate)

                    val thumbnails = snippet.getJSONObject("thumbnails")
                    val defaultThumbnail = thumbnails.getJSONObject("high")
                    val imageUrl = defaultThumbnail.getString("url")
                    val video = Video(title, imageUrl, "Uploaded on $formattedDate")

                    videoList.add(video)
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String,
                throwable: Throwable?
            ) {
                Log.d("API FAILURE", response)
            }
        }]

        return videoList

    }


}