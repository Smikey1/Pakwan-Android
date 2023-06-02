package com.hdd.pakwan.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hdd.pakwan.R
import com.hdd.pakwan.data.models.Recipe
import com.hdd.pakwan.data.remoteDataSource.ServiceBuilder
import com.hdd.pakwan.domain.adapter.RecipeDirectionAdapter
import com.hdd.pakwan.repository.RecipeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class AddRecipeDirectionsFragment(private val isEditMode:Boolean=false,private val directionList: MutableList<String> =mutableListOf()) : Fragment() {
    private var isDone: Boolean = false
    private lateinit var adapter : RecipeDirectionAdapter
    private lateinit var recipeDirectionList : MutableList<String>

    private lateinit var fdr_btnAddStep : Button
    private lateinit var fdr_etAddStepName : EditText
    private lateinit var fdr_recyclerView : RecyclerView
    private lateinit var fdr_btnDone : Button
    private lateinit var snackBarFDRLinearLayout : LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_recipe_directions, container, false)
        fdr_btnAddStep=view.findViewById(R.id.fdr_btnAddStep)
        fdr_etAddStepName=view.findViewById(R.id.fdr_etAddStepName)
        fdr_recyclerView=view.findViewById(R.id.fdr_recyclerView)
        fdr_btnDone=view.findViewById(R.id.fdr_btnDone)
        snackBarFDRLinearLayout=view.findViewById(R.id.snackBarFDRLinearLayout)

        recipeDirectionList = mutableListOf<String>()

        val linearLayoutManager = object : LinearLayoutManager(requireContext()) {
            override fun canScrollVertically(): Boolean {
                return true
            }
        }

        if (isEditMode){
            recipeDirectionList=directionList
            adapter = RecipeDirectionAdapter(recipeDirectionList)
            fdr_recyclerView.layoutManager = linearLayoutManager
            fdr_recyclerView.adapter=adapter
            adapter.notifyDataSetChanged()
            fdr_btnDone.visibility=View.INVISIBLE
            isDone=true
        }

        adapter = RecipeDirectionAdapter(recipeDirectionList)
        fdr_btnAddStep.setOnClickListener {
            if (isDone) {
                if (fdr_etAddStepName.text.isNotEmpty()){
                    fdr_btnDone.visibility=View.VISIBLE
                    recipeDirectionList.add(fdr_etAddStepName.text.toString())
                    adapter.notifyDataSetChanged()
                    fdr_etAddStepName.setText("")
                } else {
                    fdr_etAddStepName.error = "This is required"
                }
            }   else {
                if (fdr_etAddStepName.text.isNotEmpty()) {
                    fdr_btnDone.visibility=View.VISIBLE
                    recipeDirectionList.add(fdr_etAddStepName.text.toString())
                    adapter.notifyDataSetChanged()
                    fdr_etAddStepName.setText("")
                } else {
                    fdr_etAddStepName.error = "This is required"
                }
            }

            fdr_recyclerView.layoutManager = linearLayoutManager
            fdr_recyclerView.adapter = adapter
            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(fdr_recyclerView)
            fdr_btnDone.setOnClickListener {
                updateRecipeDirection(ServiceBuilder.recipeId, recipeDirectionList)
                isDone = true
                fdr_btnAddStep.text = "Edit Details"
                fdr_btnDone.visibility = View.GONE
            }
        }
        return view
    }

    private val itemTouchHelperCallback = object :
        ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN,0) {
        override fun onMove(recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,target: RecyclerView.ViewHolder): Boolean {
            when {
                isDone -> {
                    val fromPosition = viewHolder.adapterPosition
                    val toPosition =target.adapterPosition
                    Collections.swap(recipeDirectionList,fromPosition,toPosition)
                    fdr_btnDone.visibility=View.VISIBLE
                    recyclerView.adapter!!.notifyItemMoved(fromPosition,toPosition)
                    return false
                }
                else -> {
                    val fromPosition = viewHolder.adapterPosition
                    val toPosition =target.adapterPosition
                    Collections.swap(recipeDirectionList,fromPosition,toPosition)
                    recyclerView.adapter!!.notifyItemMoved(fromPosition,toPosition)
                    return false
                }
            }
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

        }

    }
    private fun updateRecipeDirection(recipeId: String?, recipeDirectionList: MutableList<String>) {
        val recipe = Recipe(direction = recipeDirectionList)
        try {
            val recipeRepository = RecipeRepository()
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    recipeRepository.updateRecipeDirection(recipeId!!,recipe)
                }
            }
        } catch (ex: Exception) {
            println(ex)
        }
    }
}