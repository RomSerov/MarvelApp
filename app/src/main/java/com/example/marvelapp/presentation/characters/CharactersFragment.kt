package com.example.marvelapp.presentation.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentCharactersBinding
import com.example.marvelapp.domain.entity.CharacterEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.fragment_characters) {

    private lateinit var binding: FragmentCharactersBinding
    private val charactersAdapter by lazy { CharactersAdapter() }
    private val viewModel by viewModels<CharactersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharactersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState()
        observeCharacters()
        setupCharactersAdapter()
        setBackPressed()

        binding.btnFilter.setOnClickListener {
            findNavController().navigate(R.id.action_charactersFragment2_to_filtersFragment)
        }
    }

    private fun setBackPressed() {
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    private fun setupCharactersAdapter() {
        binding.rvCharacters.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = charactersAdapter
            setHasFixedSize(true)
            itemAnimator = null
        }

        charactersAdapter.setOnItemClickListener { id ->
            CharactersFragmentDirections.actionCharactersFragment2ToDetailsFragment(id = id)
                .also { findNavController().navigate(it) }
        }
    }

    private fun observeCharacters() {
        viewModel.charactersList.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { setUpdateAdapterData(it) }
            .launchIn(lifecycleScope)
    }

    private fun setUpdateAdapterData(listCharacterEntity: List<CharacterEntity>) {
        charactersAdapter.differ.submitList(listCharacterEntity)
    }

    private fun observeUiState() {
        viewModel.uiState.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED)
            .onEach { handleUiState(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleUiState(charactersFragmentUiState: CharactersFragmentUiState) {
        when (charactersFragmentUiState) {
            is CharactersFragmentUiState.ShowToast -> Toast.makeText(
                requireContext(),
                charactersFragmentUiState.message,
                Toast.LENGTH_LONG
            ).show()
            is CharactersFragmentUiState.Loading -> handleLoading(charactersFragmentUiState.isLoading)
            is CharactersFragmentUiState.Init -> Unit
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