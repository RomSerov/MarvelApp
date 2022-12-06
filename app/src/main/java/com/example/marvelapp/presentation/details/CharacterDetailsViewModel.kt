package com.example.marvelapp.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelapp.domain.common.BaseResult
import com.example.marvelapp.domain.entity.CharacterEntity
import com.example.marvelapp.domain.usecases.FetchCharacterByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val fetchCharacterByIdUseCase: FetchCharacterByIdUseCase
) : ViewModel() {

    private val _character = MutableStateFlow(CharacterEntity())
    val character: StateFlow<CharacterEntity> get() = _character

    private val _uiState =
        MutableStateFlow<CharacterDetailFragmentUiState>(CharacterDetailFragmentUiState.Init)
    val uiState: StateFlow<CharacterDetailFragmentUiState> get() = _uiState

    fun fetchCharacterDetails(characterId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCharacterByIdUseCase.invoke(characterId = characterId)
                .onStart {
                    setLoading()
                }.catch { e ->
                    hideLoading()
                    showToast(e.message.toString())
                }.collect {
                    hideLoading()
                    when (it) {
                        is BaseResult.Error -> {
                            showToast(msg = it.message.toString())
                        }
                        is BaseResult.Loading -> {
                            setLoading()
                        }
                        is BaseResult.Success -> {
                            _character.value = it.data ?: CharacterEntity()
                        }
                    }
                }
        }
    }

    private fun setLoading() {
        _uiState.value = CharacterDetailFragmentUiState.Loading(true)
    }

    private fun hideLoading() {
        _uiState.value = CharacterDetailFragmentUiState.Loading(false)
    }

    private fun showToast(msg: String) {
        _uiState.value = CharacterDetailFragmentUiState.ShowToast(message = msg)
    }
}