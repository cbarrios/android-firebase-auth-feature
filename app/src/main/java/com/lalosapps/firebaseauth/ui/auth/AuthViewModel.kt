package com.lalosapps.firebaseauth.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.lalosapps.firebaseauth.data.AuthRepository
import com.lalosapps.firebaseauth.data.Resource
import com.lalosapps.firebaseauth.data.utils.await
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow = _loginFlow.asStateFlow()

    private val _signupFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signupFlow = _signupFlow.asStateFlow()

    // This acts like a state because of the getters
    val currentUser: FirebaseUser?
        get() = repository.currentUser

    var signedIn: Boolean? by mutableStateOf(null)
        private set

    init {
        if (currentUser != null) {
            viewModelScope.launch {
                try {
                    currentUser!!.reload().await()
                    _loginFlow.value = Resource.Success(currentUser!!)
                    signedIn = true
                } catch (e: FirebaseAuthInvalidUserException) {
                    _loginFlow.value = Resource.Failure(e)
                    signedIn = false
                }
            }
        } else {
            signedIn = false
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginFlow.value = Resource.Loading
            val result = repository.loginUser(email, password)
            _loginFlow.value = result
            if (result is Resource.Success) signedIn = true
        }
    }

    fun signup(name: String, email: String, password: String) {
        viewModelScope.launch {
            _signupFlow.value = Resource.Loading
            val result = repository.createUserAccount(name, email, password)
            _signupFlow.value = result
            if (result is Resource.Success) signedIn = true
        }
    }

    fun logout() {
        repository.logoutUser()
        _loginFlow.value = null
        _signupFlow.value = null
        signedIn = false
    }

    fun onLoginErrorShown() {
        _loginFlow.value = Resource.Failure(Exception())
    }

    fun onSignupErrorShown() {
        _signupFlow.value = Resource.Failure(Exception())
    }
}