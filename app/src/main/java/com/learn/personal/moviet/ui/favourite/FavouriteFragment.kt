package com.learn.personal.moviet.ui.favourite


import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.learn.personal.moviet.R
import com.learn.personal.moviet.databinding.FragmentFavouriteBinding
import com.learn.personal.moviet.viewmodel.ViewModelFactory


class FavouriteFragment : Fragment(){

    lateinit var fragmentFavouriteBinding: FragmentFavouriteBinding

    private lateinit var viewmodel: FavouriteViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentFavouriteBinding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return fragmentFavouriteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if ( activity == null) {
            return
        }

        val viewModelFactory = ViewModelFactory.getInstance(requireActivity())
        viewmodel = ViewModelProvider(this, viewModelFactory)[FavouriteViewModel::class.java]

        fragmentFavouriteBinding.progressBar.visibility = View.VISIBLE

    }
}