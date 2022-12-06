package com.example.marvelapp.presentation.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelapp.domain.common.SortOrder
import com.example.marvelapp.domain.usecases.FetchStateOrderUseCase
import com.example.marvelapp.domain.usecases.SaveStateOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val saveStateOrderUseCase: SaveStateOrderUseCase,
    private val fetchStateOrderUseCase: FetchStateOrderUseCase
) : ViewModel() {

    private val sortOrderState = fetchStateOrderUseCase.fetchState()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = SortOrder.NON
        )
    private val chosenSortOrderState = MutableStateFlow(sortOrderState.value)

    val stateSelectedFilterModel: StateFlow<StateFilterModel> = fetchSelectedFilterModel()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = StateFilterModel()
        )

    private val _savedStateResult =
        MutableStateFlow<StateFilterSave>(StateFilterSave.Empty)
    val savedStateResult: StateFlow<StateFilterSave> get() = _savedStateResult

    init {
        sortOrderState.onEach {
            chosenSortOrderState.value = it
        }
            .launchIn(viewModelScope)
    }

    fun choseSortOrder(sortOrder: SortOrder) {
        viewModelScope.launch {
            when (sortOrder) {
                SortOrder.NON -> chosenSortOrderState.value = sortOrder
                SortOrder.BY_NAME -> chosenSortOrderState.value = sortOrder
                SortOrder.BY_MODIFIED -> chosenSortOrderState.value = sortOrder
                SortOrder.BY_NAME_DESCENDING -> chosenSortOrderState.value = sortOrder
                SortOrder.BY_MODIFIED_DESCENDING -> chosenSortOrderState.value = sortOrder
            }
        }
    }

    private fun fetchSelectedFilterModel(): Flow<StateFilterModel> {
        return chosenSortOrderState.map {
            when (it) {
                SortOrder.NON -> StateFilterModel()
                SortOrder.BY_NAME -> StateFilterModel(sortedByName = true)
                SortOrder.BY_MODIFIED -> StateFilterModel(sortedByModified = true)
                SortOrder.BY_NAME_DESCENDING -> StateFilterModel(reverseSortedByName = true)
                SortOrder.BY_MODIFIED_DESCENDING -> StateFilterModel(reverseSortedByModified = true)
            }
        }
    }

    fun applyStateFilter() {
        saveStateSortOrder(sortOrder = chosenSortOrderState.value)
    }

    private fun saveStateSortOrder(sortOrder: SortOrder) {
        viewModelScope.launch(Dispatchers.IO) {
            _savedStateResult.value = StateFilterSave.Loading
            saveStateOrderUseCase.saveState(value = sortOrder)
        }.invokeOnCompletion {
            _savedStateResult.value = StateFilterSave.Success
        }
    }
}