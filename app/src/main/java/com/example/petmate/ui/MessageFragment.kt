package com.example.petmate.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.petmate.R
import com.example.petmate.adapter.MessageUserAdapter
import com.example.petmate.databinding.FragmentMessageBinding
import com.example.petmate.ui.DatingFragment.Companion.lista

class MessageFragment : Fragment() {

    private lateinit var binding  : FragmentMessageBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(layoutInflater);

        binding.recyclerView.adapter = MessageUserAdapter(requireContext(), lista!!)

        return binding.root;
    }
}