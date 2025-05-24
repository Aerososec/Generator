package com.example.excusegenerator.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.excusegenerator.VO.Excuse
import com.example.excusegenerator.databinding.FragmentFavoritesBinding
import com.example.excusegenerator.main.MainActivity


class FavoritesFragment : Fragment() {
    var excuses : ArrayList<Excuse> = ArrayList()
    var favoritesAdapter: FavoritesFragmentAdapter? = null
    lateinit var binding: FragmentFavoritesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favoritesAdapter = FavoritesFragmentAdapter(requireContext(), excuses)
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).loadFavorites()
        binding.rvContacts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvContacts.adapter = favoritesAdapter
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}