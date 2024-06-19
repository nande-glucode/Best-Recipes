package com.example.bestrecipes.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.bestrecipes.Data.Responses.RecipeEntity
import com.example.bestrecipes.databinding.ItemRecipeBinding


class RecipeAdapter(
    private val onClick: (RecipeEntity) -> Unit
) : ListAdapter<RecipeEntity, RecipeAdapter.ViewHolder>(DiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemRecipeBinding, private val onClick: (RecipeEntity) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipeEntity) {
            binding.recipeName.text = recipe.title
            binding.recipeImage.load(recipe.image)
            binding.root.setOnClickListener {
                onClick(recipe)
            }
        }
    }

    class DiffCallBack : DiffUtil.ItemCallback<RecipeEntity>() {
        override fun areItemsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: RecipeEntity, newItem: RecipeEntity): Boolean {
            return oldItem == newItem
        }
    }
}