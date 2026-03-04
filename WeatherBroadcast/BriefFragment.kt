package com.example.widgetsdemo2728

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.widgetsdemo2728.databinding.FragmentBriefBinding

class BriefFragment: Fragment() {

    private lateinit var binding: FragmentBriefBinding
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_brief, container, false)
        binding.weather = viewModel.weatherData
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

}