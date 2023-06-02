package com.hdd.pakwan.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hdd.pakwan.R
import com.hdd.pakwan.data.models.IngredientSchema
import com.hdd.pakwan.domain.adapter.RecipeIngredientsAdapter


class ViewRecipeIngredientsFragment(private val ingredientList: MutableList<IngredientSchema>) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_recipe_ingredients, container, false)

        val fvri_recyclerView: RecyclerView = view.findViewById(R.id.fvri_recyclerView)

        val adapter = RecipeIngredientsAdapter(ingredientList,true)
        fvri_recyclerView.layoutManager = LinearLayoutManager(requireContext())
        fvri_recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        return view
    }
}