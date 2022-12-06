package com.example.marvelapp.presentation.filters

sealed class StateFilterSave {
    object Empty : StateFilterSave()
    object Success : StateFilterSave()
    object Loading : StateFilterSave()
}
