package com.etsproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.etsproject.R
import com.etsproject.data.ApplicationDatabase
import com.etsproject.data.Profile
import com.etsproject.data.ProfileRepository
import com.etsproject.databinding.FragmentHomeBinding
import com.etsproject.utils.ClickCallback
import com.etsproject.utils.SwipeCallback
import com.etsproject.utils.SwipeController
import com.etsproject.viewmodel.MainViewModel
import com.etsproject.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var index = 0
    private var deletedProfile: Profile? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            ProfileRepository.getInstance(
                ApplicationDatabase
                    .getApplicationDatabase(requireContext()).profileDao()
            )
        )
    }

    private val adapter = ProfileAdapter(object : ClickCallback {
        override fun onItemClick(profile: Profile) {
            val bundle = Bundle()
            bundle.putSerializable("profile", profile)
            findNavController().navigate(R.id.action_home_fragment_to_profile_fragment, bundle)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.profileList.observe(this, {
            adapter.submitList(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentHomeBinding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_home, container, false)
        val swipeCallback: SwipeCallback = object : SwipeCallback {
            override fun onSwipe(position: Int) {
                viewModel.deleteProfile(adapter.currentList[position])
                deletedProfile = adapter.currentList[position]
                showUndoDialog()
            }
        }

        val itemTouchHelper = ItemTouchHelper(SwipeController(swipeCallback))
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)

        binding.apply {
            linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.adapter = adapter

            addButton.setOnClickListener {
                findNavController().navigate(R.id.action_home_fragment_to_profile_fragment)
            }

            editText.addTextChangedListener {
                viewModel.setQuery(it.toString())
            }

            if (index > 0) constraintLayout.transitionToEnd()
        }

        return binding.root
    }

    override fun onPause() {
        index = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
        super.onPause()
    }

    private fun showUndoDialog() {
        Snackbar.make(requireView(), "Başarıyla silindi", Snackbar.LENGTH_LONG)
            .setAction("Geri Al") {
                deletedProfile?.let { viewModel.insertProfile(it) }
            }
            .show()
    }
}