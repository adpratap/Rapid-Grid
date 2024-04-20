package com.noreplypratap.rapidgrid.presentation.fragments

import ImageAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.noreplypratap.rapidgrid.databinding.FragmentImageBinding
import com.noreplypratap.rapidgrid.presentation.viewmodels.RemoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageFragment : Fragment() {

    private val remoteViewModel: RemoteViewModel by viewModels()
    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageAdapter = ImageAdapter(remoteViewModel)
        binding.rvImages.adapter = imageAdapter

        remoteViewModel.images.observe(viewLifecycleOwner) {
            imageAdapter.differ.submitList(it)
            imageAdapter.attachScrollListener(binding.rvImages)
            imageAdapter.notifyDataSetChanged()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}