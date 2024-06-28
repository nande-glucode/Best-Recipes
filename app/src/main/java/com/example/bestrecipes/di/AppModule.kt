package com.example.bestrecipes.di

import android.app.Application
import androidx.room.Room
import com.example.bestrecipes.data.remote.ApiKeyIntercepter
import com.example.bestrecipes.data.remote.SpoonApi
import com.example.bestrecipes.data.database.RecipeDao
import com.example.bestrecipes.data.database.RecipeDatabase
import com.example.bestrecipes.spoonRepository.SpoonRepository
import com.example.bestrecipes.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSpoonRepository(
        api: SpoonApi,
        recipeDao: RecipeDao
    ) = SpoonRepository(api, recipeDao)

    @Singleton
    @Provides
    fun providesSpoonApi(): SpoonApi {
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiKeyIntercepter())
            .build()

        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(SpoonApi::class.java)
    }

      @Provides
      @Singleton
      fun provideRecipeDatabase(app: Application): RecipeDatabase {
          return Room.databaseBuilder(
              app,
              RecipeDatabase::class.java,
              "favorite_recipes"
          ).build()

      }


      @Provides
      @Singleton
     fun provideRecipeDao(db: RecipeDatabase): RecipeDao {
         return db.recipeDao()
     }
}