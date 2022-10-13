package com.lalosapps.firebaseauth.data

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun loginUser(email: String, password: String): Resource<FirebaseUser>
    suspend fun createUserAccount(
        name: String,
        email: String,
        password: String
    ): Resource<FirebaseUser>

    fun logoutUser()
}