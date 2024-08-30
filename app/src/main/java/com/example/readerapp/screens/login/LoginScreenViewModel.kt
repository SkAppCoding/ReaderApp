package com.example.readerapp.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginScreenViewModel : ViewModel() {
    //val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun signInUserWithEmailAndPassword(email: String, password: String, home: () -> Unit)
    = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        //Todo: navigate to home
                        Log.d("FB", "signInWithEmailAndPassword: Success! ${task.result}")
                        home()
                    } else {
                        Log.d("FB", "signInWithEmailAndPassword: ${task.result}")
                    }
                }
        } catch (e: Exception) {
            Log.d("FB", "signInWithEmailAndPassword: ${e.message}")
        }
    }


    fun createUserWithEmailAndPassword() {

    }
}