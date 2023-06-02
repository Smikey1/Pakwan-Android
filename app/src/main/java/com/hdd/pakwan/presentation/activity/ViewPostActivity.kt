package com.hdd.pakwan.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hdd.pakwan.R
import com.hdd.pakwan.domain.adapter.PostAdapter
import com.hdd.pakwan.repository.PostRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewPostActivity : AppCompatActivity() {
    private lateinit var postRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_post)
        postRecyclerView = findViewById(R.id.postRecyclerView)
        //Extract the data…
        val bundle = intent.extras
        val postId = bundle!!.getString("postId")!!
        getPost(postId)
    }
    private fun getPost(postId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val postRepository = PostRepository()
            val response = postRepository.getPostByIdInArray(postId)
            if (response.success == true) {
                withContext(Dispatchers.Main) {
                    if (response.data!!.isNotEmpty()){
                        val adapter = PostAdapter(response.data)
                        postRecyclerView.layoutManager = LinearLayoutManager(this@ViewPostActivity)
                        postRecyclerView.adapter = adapter
                    } else{
                    }
                }
            }
        }
    }
}