package com.example.usergithubrepos.di

import com.example.usergithubrepos.data.UserUseCase
import org.koin.dsl.module

/**
 * Use case DI module.
 * Provide Use case dependency.
 */
val UseCaseDependency = module {

    factory {
        UserUseCase()
    }
}