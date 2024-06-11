package com.example.bestrecipes.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.bestrecipes.Data.Models.RecipeListEntry
import com.example.bestrecipes.databinding.ItemRecipeBinding


class RecipeAdapter(
    private val onClick: (RecipeListEntry) -> Unit
) : ListAdapter<RecipeListEntry, RecipeAdapter.ViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemRecipeBinding, private val onClick: (RecipeListEntry) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipeListEntry) {
            binding.recipeName.text = recipe.recipeName
            binding.recipeImage.load(recipe.imageUrl)
            binding.root.setOnClickListener {
                onClick(recipe)
            }
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<RecipeListEntry>() {
        override fun areItemsTheSame(oldItem: RecipeListEntry, newItem: RecipeListEntry): Boolean {
            return oldItem.id == newItem.id
    }
        override fun areContentsTheSame(oldItem: RecipeListEntry, newItem: RecipeListEntry): Boolean {
            return oldItem == newItem
        }
    }
}
