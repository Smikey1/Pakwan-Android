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
import com.hdd.pakwan.repository.RecipeRepository
import com.hdd.pakwan.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ArchivedRecipeFragment() : Fragment() {
    private lateinit var far_archivedRecipeRV: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_archived_recipe, container, false)
        far_archivedRecipeRV = view.findViewById(R.id.far_archivedRecipeRV)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val recipeRepository = RecipeRepository()
                val response = recipeRepository.viewArchivedRecipe()
                if (response.success == true) {
                    val archivedRecipeList = response.data!!
                    withContext(Dispatchers.Main) {
                        val adapter = RecipeAdapter(archivedRecipeList,true)
                        far_archivedRecipeRV.layoutManager = GridLayoutManager(requireContext(), 2)
                        far_archivedRecipeRV.adapter = adapter
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