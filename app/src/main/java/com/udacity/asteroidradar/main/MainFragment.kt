package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.ui.AsteroidClickListener
import timber.log.Timber

class MainFragment : Fragment() {
    private var asteroidListAdapter: AsteroidListAdapter? = null

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        asteroidListAdapter = AsteroidListAdapter(AsteroidClickListener {
            // Navigate to the detail fragment
            this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        })

        binding.root.findViewById<RecyclerView>(R.id.recyclerview_asteroid_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = asteroidListAdapter
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observer change of Asteroid list in view model and populate adapter
        viewModel.asteroidList.observe(viewLifecycleOwner, Observer<List<Asteroid>> { asteroidList ->
            asteroidList?.apply {
                asteroidListAdapter?.asteroidList = asteroidList
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
