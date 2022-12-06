package com.example.marvelapp.presentation.filters

data class StateFilterModel(
    val sortedByName: Boolean = false,
    val sortedByModified: Boolean = false,
    val reverseSortedByName: Boolean = false,
    val reverseSortedByModified: Boolean = false
)
