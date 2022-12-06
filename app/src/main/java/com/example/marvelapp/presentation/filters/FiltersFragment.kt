package com.example.marvelapp.presentation.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentFiltersBinding
import com.example.marvelapp.domain.common.SortOrder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FiltersFragment : Fragment(R.layout.fragment_filters) {

    private lateinit var binding: FragmentFiltersBinding
    private val viewModel by viewModels<FiltersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFiltersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.apply {
            setNavigationIcon(R.drawable.ic_back)
            title = context.getString(R.string.btn_filter_label)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.btnApply.setOnClickListener {
            viewModel.applyStateFilter()
        }

        lifecycleScope.launch{
            viewModel.savedStateResult.collect {
                when (it) {
                    StateFilterSave.Loading -> {
                        handleLoading(loading = true)
                    }
                    StateFilterSave.Success -> {
                        handleLoading(loading = false)
                        findNavController().popBackStack()
                    }
                    StateFilterSave.Empty -> Unit
                }
            }
        }

        lifecycleScope.launch {
            viewModel.stateSelectedFilterModel.collect {
                binding.tvSortedByName.isActivated = it.sortedByName
                binding.tvSortedByModified.isActivated = it.sortedByModified
                binding.tvSortedByNameReverse.isActivated = it.reverseSortedByName
                binding.tvSortedByModifiedReverse.isActivated = it.reverseSortedByModified
            }
        }

        binding.tvSortedByName.setOnClickListener {
            if (it.isPressed) {
                viewModel.choseSortOrder(sortOrder = SortOrder.BY_NAME)
            }
        }

        binding.tvSortedByModified.setOnClickListener {
            if (it.isPressed) {
                viewModel.choseSortOrder(sortOrder = SortOrder.BY_MODIFIED)
            }
        }

        binding.tvSortedByNameReverse.setOnClickListener {
            if (it.isPressed) {
                viewModel.choseSortOrder(sortOrder = SortOrder.BY_NAME_DESCENDING)
            }
        }

        binding.tvSortedByModifiedReverse.setOnClickListener {
            if (it.isPressed) {
                viewModel.choseSortOrder(sortOrder = SortOrder.BY_MODIFIED_DESCENDING)
            }
        }
    }

    private fun handleLoading(loading: Boolean) {
        if (loading) {
            binding.loadingBar.visibility = View.VISIBLE
        } else {
            binding.loadingBar.visibility = View.GONE
        }
    }

}