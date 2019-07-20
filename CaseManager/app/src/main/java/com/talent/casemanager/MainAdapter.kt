package com.talent.casemanager

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row.view.*

class MainAdapter(val homeFeed: HomeFeed): RecyclerView.Adapter<CustomViewHolder>() {
val videoTitles=listOf("Michelle Godoka", "Decent Jokonya","MaMuchiho")

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomViewHolder {
        val layoutInflater=LayoutInflater.from(p0?.context)
        val callForRow=layoutInflater.inflate(R.layout.row,p0,false)
        return  CustomViewHolder((callForRow))
    }

    override fun getItemCount(): Int {
        return homeFeed.videos.size
    }



    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
      //  val videoTitle=videoTitles.get(position)
        val video =homeFeed.videos.get(position)

        holder.view.text_video_title.text=video.name
        holder.view.textView_channel_name.text=video.channel.name
        val thumbnailImageView=holder.view.imageView_video_thumpnail
        Picasso.get().load(video.imageUrl).into(thumbnailImageView)

        val channelProfileImageView =holder.view.imageView_channel_profile
        Picasso.get().load(video.channel.profileImageUrl).into(channelProfileImageView)
    }

}
class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){

}