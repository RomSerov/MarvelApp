package com.example.marvelapp.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.marvelapp.R
import com.example.marvelapp.databinding.FragmentDetailsBinding
import com.example.marvelapp.domain.entity.CharacterEntity
import com.example.marvelapp.presentation.characters.CharactersFragmentUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CharacterDetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel by viewModels<CharacterDetailsViewModel>()
    private val bundleArgs: CharacterDetailsFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchCharacterDetails(characterId = bundleArgs.id)
        observeUiState()
        observeCharacter()
    }

    private fun observeUiState() {
        viewModel.uiState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleUiState(it) }
            .launchIn(lifecycleScope)
    }

    private fun observeCharacter() {
        viewModel.character.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleCharacter(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleCharacter(character: CharacterEntity) {
        binding.apply {
            tvDetailName.text = character.name
            tvDetailDescription.text = character.description
            Glide.with(this@CharacterDetailsFragment)
                .load(character.thumbnailPath + "." + character.thumbnailExtension)
                .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                .error(android.R.drawable.stat_notify_error).into(ivDetailThumbnail)
        }
    }

    private fun handleUiState(characterDetailFragmentUiState: CharacterDetailFragmentUiState) {
        when (characterDetailFragmentUiState) {
            is CharacterDetailFragmentUiState.ShowToast -> Toast.makeText(requireContext(), characterDetailFragmentUiState.message, Toast.LENGTH_LONG).show()
            is CharacterDetailFragmentUiState.Loading -> handleLoading(characterDetailFragmentUiState.isLoading)
            is CharacterDetailFragmentUiState.Init -> Unit
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