package com.hdd.pakwan.presentation.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.hdd.pakwan.R
import com.hdd.pakwan.presentation.activity.AddPostActivity
import com.hdd.pakwan.presentation.activity.AddRecipeActivity
import com.hdd.pakwan.presentation.activity.AddRestaurantActivity


class AddFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)
        val addPost: ImageView = view.findViewById(R.id.fa_AddPost)
        val addRecipe: ImageView = view.findViewById(R.id.fa_AddRecipe)
        val addRestaurant: ImageView = view.findViewById(R.id.fa_AddRestaurant)

        addPost.setOnClickListener {
            startActivity(Intent(requireActivity(), AddPostActivity::class.java))
        }

        addRecipe.setOnClickListener {
            startActivity(Intent(requireActivity(), AddRecipeActivity::class.java))
        }

        addRestaurant.setOnClickListener {
            startActivity(Intent(requireActivity(), AddRestaurantActivity::class.java))
        }
        return view
    }
}