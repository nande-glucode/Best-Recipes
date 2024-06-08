package com.example.bestrecipes.Di

import com.example.bestrecipes.Data.Remote.SpoonApi
import com.example.bestrecipes.SpoonRepository.SpoonRepository
import com.example.bestrecipes.Utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSpoonRepository(
        api: SpoonApi
    ) = SpoonRepository(api)

    @Singleton
    @Provides
    fun providesSpoonApi(): SpoonApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(SpoonApi::class.java)
    }
}