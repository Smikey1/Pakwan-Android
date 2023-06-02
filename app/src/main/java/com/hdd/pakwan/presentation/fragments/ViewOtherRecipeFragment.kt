package com.hdd.pakwan.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hdd.pakwan.R
import com.hdd.pakwan.data.models.User
import com.hdd.pakwan.domain.adapter.RecipeAdapter


class ViewOtherRecipeFragment(private val user: User) : Fragment() {

    private lateinit var frvo_profileRecipeRV:RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipe_view_other, container, false)
        frvo_profileRecipeRV = view.findViewById(R.id.frvo_profileRecipeRV)
        val adapter = RecipeAdapter(user.recipe!!)
        frvo_profileRecipeRV.layoutManager = LinearLayoutManager(requireActivity())
        frvo_profileRecipeRV.adapter = adapter
        return view
    }
}