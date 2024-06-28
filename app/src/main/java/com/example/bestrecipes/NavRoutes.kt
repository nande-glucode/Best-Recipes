package com.example.bestrecipes

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home_screen")
    object RecipeList : NavRoutes("recipe_list_screen")
    object RecipeDetail : NavRoutes("recipe_detail_screen/{id}") {
        fun createRoute(id: Long) = "recipe_detail_screen/$id"
    }
    object FavoriteList : NavRoutes("favorite_recipe_list_screen")
}
