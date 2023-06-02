package com.hdd.pakwan.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hdd.pakwan.R
import com.hdd.pakwan.domain.adapter.PostAdapter
import com.hdd.pakwan.domain.adapter.RecipeAdapter
import com.hdd.pakwan.repository.PostRepository
import com.hdd.pakwan.repository.RecipeRepository
import com.hdd.pakwan.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ArchivedPostFragment() : Fragment() {
    private lateinit var fap_archivedPostRV: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_archived_post, container, false)
        fap_archivedPostRV = view.findViewById(R.id.fap_archivedPostRV)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val postRepository = PostRepository()
                val response = postRepository.viewArchivedPost()
                if (response.success == true) {
                    val postList = response.data!!
                    withContext(Dispatchers.Main) {
                        val adapter = PostAdapter(postList,true)
                        fap_archivedPostRV.layoutManager = LinearLayoutManager(requireActivity())
                        fap_archivedPostRV.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                }
            } catch (ex: Exception) {
                print(ex)
            }
        }
        return view
    }
}