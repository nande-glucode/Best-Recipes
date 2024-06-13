package com.example.bestrecipes.Di

import android.app.Application
import androidx.room.Room
import com.example.bestrecipes.Data.Remote.ApiKeyIntercepter
import com.example.bestrecipes.Data.Remote.SpoonApi
import com.example.bestrecipes.Database.RecipeDao
import com.example.bestrecipes.Database.RecipeDatabase
import com.example.bestrecipes.SpoonRepository.SpoonRepository
import com.example.bestrecipes.Utils.Constants.BASE_URL
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
              "recipe_database"
          ).build()
      }

      @Provides
      @Singleton
     fun provideRecipeDao(db: RecipeDatabase): RecipeDao {
         return db.recipeDao()
     }
}