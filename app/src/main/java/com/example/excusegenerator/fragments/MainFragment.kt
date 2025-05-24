package com.example.excusegenerator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.excusegenerator.databinding.FragmentMainBinding
import com.example.excusegenerator.main.MainActivity

class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    var categories : ArrayList<String> = ArrayList()
    lateinit var mainFragmentAdapter: MainFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCategories.layoutManager = LinearLayoutManager(requireContext())
        mainFragmentAdapter = MainFragmentAdapter(requireContext(), categories) // тут мб
        binding.rvCategories.adapter = mainFragmentAdapter
        binding.fabFavorites.setOnClickListener {
            (activity as? MainActivity)?.showFavoritesScreen()
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}