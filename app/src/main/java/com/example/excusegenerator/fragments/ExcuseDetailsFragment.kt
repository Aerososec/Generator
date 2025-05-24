package com.example.excusegenerator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.excusegenerator.VO.Excuse
import com.example.excusegenerator.databinding.FragmentExcuseDetailsBinding
import com.example.excusegenerator.databinding.FragmentFavoritesBinding
import com.example.excusegenerator.main.MainActivity
import com.example.excusegenerator.main.MainPresentorImpl
import com.example.excusegenerator.service.GeneratorApiService

class ExcuseDetailsFragment : Fragment() {
    var excuses : ArrayList<Excuse> = ArrayList()
    lateinit var favoritesAdapter: FavoritesFragmentAdapter
    lateinit var binding: FragmentExcuseDetailsBinding
    private lateinit var goodExcuse : Excuse
    private var position = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExcuseDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.excuseTextView.text = "Нажмите для получения"
        binding.excuseTextView.setOnClickListener {
            if (excuses.isNotEmpty()){
                binding.favoriteButton.visibility = View.VISIBLE
                chooseNext()
                goodExcuse = excuses[position]
                binding.excuseTextView.text = goodExcuse.Text

                binding.favoriteButton.setOnClickListener {
                    (activity as MainActivity).addExcuseToFavorite(goodExcuse.ID.toString())
                }
            }
            else{
                binding.excuseTextView.text = "Отмазок не найдено"
            }
        }

    }

    fun chooseNext(){
        position++
        if (position >= excuses.size){
            position = 0
        }
    }

    companion object {
        fun newInstance() = FavoritesFragment()
    }
}