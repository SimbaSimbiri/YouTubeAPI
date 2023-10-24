package com.simbiri.youtubeapi

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

data class Video(
    val title: String,
    val videoUrl: String,
    val dateToday : String

)


object YoutubeKeyProvider {

    fun keyProvider(context: Context, intKey: Int): String {
        return (if (intKey == 0) {
            context.resources.getString(R.string.apikey)
        } else {
            context.resources.getString(R.string.channelID)
        }).toString()

    }
}


class LiveVideoAdapter(val context: Context, val listVids: ArrayList<Video>) :
    RecyclerView.Adapter<LiveVideoAdapter.LiveVideoViewHolder>() {

    inner class LiveVideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private var positionOfItem: Int = 1
        private var currentVideoItem: Video? = null
        private val videoThumbnailImage: ImageView = itemView.findViewById(R.id.imageViewNews)
        private val videoTitleTextView: TextView = itemView.findViewById(R.id.textViewHeadline)

        fun setDataToItem(video: Video, position: Int) {

            this.currentVideoItem = video
            this.positionOfItem = position

            var layoutParams = videoThumbnailImage.layoutParams

            val displayMetrics = DisplayMetrics()
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)

            val screenWidth = displayMetrics.widthPixels
            layoutParams.width = screenWidth
            layoutParams.height = screenWidth * 9/16

            videoThumbnailImage.layoutParams = layoutParams

            Picasso.get()
                .load(video.videoUrl)
                .into(videoThumbnailImage)

            videoTitleTextView.text = video.title

        }

        fun setOnClickListeners() {
            itemView.setOnClickListener(this@LiveVideoViewHolder)
        }

        override fun onClick(view: View?) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveVideoViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.newsitemsample, parent, false)

        return LiveVideoViewHolder(view)
    }

    override fun onBindViewHolder(videoViewHolder: LiveVideoViewHolder, position: Int) {
        videoViewHolder.setDataToItem(listVids[position], position)
        videoViewHolder.setOnClickListeners()
    }

    override fun getItemCount(): Int = listVids.size
}


