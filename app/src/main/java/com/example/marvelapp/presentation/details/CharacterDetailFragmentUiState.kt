package com.example.marvelapp.presentation.details

sealed class CharacterDetailFragmentUiState {
    object Init : CharacterDetailFragmentUiState()
    data class Loading(val isLoading: Boolean) : CharacterDetailFragmentUiState()
    data class ShowToast(val message: String) : CharacterDetailFragmentUiState()
}
