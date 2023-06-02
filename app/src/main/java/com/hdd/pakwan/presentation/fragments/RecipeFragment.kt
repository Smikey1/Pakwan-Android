package com.hdd.pakwan.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hdd.pakwan.R
import com.hdd.pakwan.domain.adapter.RecipeAdapter
import com.hdd.pakwan.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecipeFragment() : Fragment() {
    private lateinit var fr_profileRecipeRV: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipe, container, false)
        fr_profileRecipeRV = view.findViewById(R.id.fr_profileRecipeRV)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userRepository = UserRepository()
                val response = userRepository.getUserRecipe()
                if (response.success == true) {
                    val recipeList = response.data!!
                    withContext(Dispatchers.Main) {
                        val adapter = RecipeAdapter(recipeList)
                        fr_profileRecipeRV.layoutManager = GridLayoutManager(requireContext(), 2)
                        fr_profileRecipeRV.adapter = adapter
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