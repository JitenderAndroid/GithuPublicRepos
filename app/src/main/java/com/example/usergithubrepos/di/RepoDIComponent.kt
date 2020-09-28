package com.example.usergithubrepos.di

import com.example.usergithubrepos.data.UserRepository
import org.koin.dsl.module

/**
 * Repository DI module.
 * Provides Repo dependency.
 */
val RepoDependency = module {

    factory {
        UserRepository()
    }
}