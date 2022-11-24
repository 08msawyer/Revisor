package com.llamalad7.revisor.services.module

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.llamalad7.revisor.services.AccountService
import com.llamalad7.revisor.services.impl.AccountServiceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds
    abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    companion object {
        @Provides
        fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth
    }
}