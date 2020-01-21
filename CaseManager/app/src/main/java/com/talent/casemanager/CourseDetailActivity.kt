package com.talent.casemanager

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.course_detail_row.view.*
import okhttp3.*
import java.io.IOException

class CourseDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
     // recyclerview_main.setBackgroundColor(Color.RED)
        recyclerview_main.layoutManager=LinearLayoutManager(this)
      //  recyclerview_main.adapter=CourseDetailAdapter()

        val navBarTitle=intent.getStringExtra(CustomViewHolder.VIDEO_TITLE_KEY)

        supportActionBar?.title=navBarTitle


        //println(courseDetailUrl)

        fetchJSON()
    }

    private fun fetchJSON() {
        val videoId=intent.getIntExtra(CustomViewHolder.VIDEO_ID_KEY,-1)
        val courseDetailUrl="https://api.letsbuildthatapp.com/youtube/course_detail?id="+videoId

        val  client=OkHttpClient()
        val request=Request.Builder().url(courseDetailUrl).build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute REQUEST")
            }

            override fun onResponse(call: Call, response: Response) {

                val body= response.body?.string()
                println(body)
                val gson=GsonBuilder().create()
                var courseLessons=gson.fromJson(body,Array<CourseLessons>::class.java)
                runOnUiThread()
                {
                    recyclerview_main.adapter=CourseDetailAdapter(courseLessons)
                }

               // println(body)

            }
            })
    }


    private class CourseDetailAdapter(val courseLessons: Array<CourseLessons>) : RecyclerView.Adapter<CourseViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CourseViewHolder {
            val layoutInflater=LayoutInflater.from(parent.context)
            val customView=layoutInflater.inflate(R.layout.course_detail_row,parent,false)



           /*val blueView= View(parent.context)
            blueView.setBackgroundColor(Color.BLUE)
            blueView.minimumHeight=50*/
            return CourseViewHolder(customView)
        }

        override fun getItemCount(): Int {
            return courseLessons.size
        }

        override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
           val courseLessons=courseLessons.get(position)
            holder.customView.textView_course_lesson_title.text=courseLessons.name
            holder.customView.textView_duration.text=courseLessons.duration
            val imageview=holder.customView.imageView_thumbnail
            Picasso.get().load(courseLessons.imageUrl).into(imageview)
            holder.courseLesson=courseLessons
        }

    }
     class CourseViewHolder(val customView:View,var courseLesson:CourseLessons?=null):RecyclerView.ViewHolder(customView){
        companion object
        {
            val COURSE_LESSON_LINK_KEY="COURSE_LESSON_LINK"
        }
        init {
            customView.setOnClickListener{
             //   println("Attempting to load Webview somehow")
                val intent = Intent(customView.context,CourseLessonActivity::class.java)
                intent.putExtra(COURSE_LESSON_LINK_KEY,courseLesson?.link)
                customView.context.startActivity(intent)

            }
        }
    }
}
