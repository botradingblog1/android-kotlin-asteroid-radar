package com.udacity.asteroidradar.main

import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
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

        /* val imageView = binding.activityMainImageOfTheDay

        // Observe changes to picture of day
        viewModel.pictureOfDay.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                // Set pic of the day
                Timber.d("url: "+it.url)
                Picasso.get()
                    .load(it.url)
                    .into(imageView);
            }
        })*/

        asteroidListAdapter = AsteroidListAdapter(AsteroidClickListener {
            // When a video is clicked this block or lambda will be called by DevByteAdapter

            // context is not around, we can safely discard this click since the Fragment is no
            // longer on the screen
            //val packageManager = context?.packageManager ?: return@AsteroidClickListener

            // todo
            Timber.d("click: "+it.codename);
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
