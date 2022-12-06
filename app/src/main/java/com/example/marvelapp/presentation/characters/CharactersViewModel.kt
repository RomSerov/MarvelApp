package com.example.marvelapp.presentation.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.marvelapp.domain.common.BaseResult
import com.example.marvelapp.data.workers.StateClearWorker
import com.example.marvelapp.domain.common.SortOrder
import com.example.marvelapp.domain.entity.CharacterEntity
import com.example.marvelapp.domain.usecases.FetchCharactersUseCase
import com.example.marvelapp.domain.usecases.FetchStateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val fetchCharactersUseCase: FetchCharactersUseCase,
    private val fetchStateOrderUseCase: FetchStateOrderUseCase,
    private val workManager: WorkManager
) : ViewModel() {

    private val _charactersList = MutableStateFlow(emptyList<CharacterEntity>())
    val charactersList: StateFlow<List<CharacterEntity>> get() = _charactersList

    private val _uiState =
        MutableStateFlow<CharactersFragmentUiState>(CharactersFragmentUiState.Init)
    val uiState: StateFlow<CharactersFragmentUiState> get() = _uiState

    private val savedStateSortOrder =
        fetchStateOrderUseCase.fetchState()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                replay = 1
            )

    init {
        viewModelScope.launch {
            fetchStateSortOrder()
        }
    }

    private suspend fun fetchStateSortOrder() {
        savedStateSortOrder.collect {
            when (it) {
                SortOrder.NON -> {
                    fetchCharacters(it)
                }
                SortOrder.BY_NAME -> {
                    fetchCharacters(it)
                }
                SortOrder.BY_MODIFIED -> {
                    fetchCharacters(it)
                }
                SortOrder.BY_NAME_DESCENDING -> {
                    fetchCharacters(it)
                }
                SortOrder.BY_MODIFIED_DESCENDING -> {
                    fetchCharacters(it)
                }
            }
        }
    }

    private fun fetchCharacters(sortOrder: SortOrder) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchCharactersUseCase.invoke(sortOrder = sortOrder)
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
                            _charactersList.value =
                                it.data as? MutableList<CharacterEntity> ?: emptyList()
                        }
                    }
                }
        }
    }

    private fun setLoading() {
        _uiState.value = CharactersFragmentUiState.Loading(true)
    }

    private fun hideLoading() {
        _uiState.value = CharactersFragmentUiState.Loading(false)
    }

    private fun showToast(msg: String) {
        _uiState.value = CharactersFragmentUiState.ShowToast(message = msg)
    }

    override fun onCleared() {
        initWorker()
        super.onCleared()
    }

    private fun initWorker() {
        workManager.enqueue(OneTimeWorkRequestBuilder<StateClearWorker>().build())
    }
}

