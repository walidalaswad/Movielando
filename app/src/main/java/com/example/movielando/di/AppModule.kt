package com.example.movielando.di

import com.example.movielando.data.remote.IMDBApi
import com.example.movielando.util.calladapter.flow.FlowResourceCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://imdb-api.com/API/AdvancedSearch/")
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(FlowResourceCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideIMDBApi(retrofit: Retrofit): IMDBApi {
        return retrofit.create(IMDBApi::class.java)
    }

}
