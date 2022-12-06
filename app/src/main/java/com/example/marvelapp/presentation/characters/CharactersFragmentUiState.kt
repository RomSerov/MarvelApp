package com.example.marvelapp.presentation.characters

sealed class CharactersFragmentUiState {
    object Init : CharactersFragmentUiState()
    data class Loading(val isLoading: Boolean) : CharactersFragmentUiState()
    data class ShowToast(val message: String) : CharactersFragmentUiState()
}
