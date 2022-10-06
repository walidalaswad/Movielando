package com.example.movielando.ui.screen.movie

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movielando.data.remote.IMDBContants
import com.example.movielando.data.remote.Item
import com.example.movielando.data.repo.MovieRepository
import com.example.movielando.data.repo.SearchMovie
import com.example.movielando.util.Resource
import com.github.theapache64.twyper.SwipedOutDirection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class SearchUiState(

    val loadingMsg: String? = null,
    val blockingMsg: String? = "Go into settings and choose your language.",
    val subTitle: String? = null,
    val query: String = "",
    val items: SnapshotStateList<Item> = mutableStateListOf()
)

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val searchMovie: SearchMovie,
    private val movieRepository: MovieRepository
) : ViewModel() {
    /**
     * Random movie algorithm
     */
    private var r_num_votes = (0..1000).random().toString()
    private var r_runtime = (50..60).random().toString()
    private var r_sort_list: List<String> = listOf("user_rating,desc","num_votes,desc","num_votes,asc",
    "runtime,asc","runtime,desc","moviemeter,desc")
    private var r_sort = r_sort_list[(0..5).random()]


    /**
     * To support request debounce
     */
    private var searchJob: Job? = null
    private var pageNo = 0

    /**
     * -1 = first API not done yet
     */
    private var totalPages = -1
    var uiState by mutableStateOf(SearchUiState())
        private set

    fun onQueryChanged(newQuery: String) {
        // To update textField UI
        uiState = uiState.copy(query = newQuery)
        // reset page no
        pageNo = 1
        totalPages = -1

        // make first API call
        loadPage(debounce = 300)
    }

    fun onItemSwipedOut(item: Item, direction: SwipedOutDirection) {
        println("Swiped out: $direction")

        if(direction.toString() == "RIGHT") {
            Log.d("LIKEY","SUCESS")
            //addToFavorites(item)
            addMovie(item)
        }

        uiState.items.remove(item)

    }

    fun onPageEndReached() {
        pageNo++
        loadPage(debounce = 0)
    }

    private fun loadPage(
        debounce: Long
    ) {
        searchJob?.cancel()

        if (totalPages != -1 && pageNo > totalPages) {
            uiState = uiState.copy(
                blockingMsg = "No more results",
                loadingMsg = null
            )
            onQueryChanged(uiState.query)
            return
        }

        val query = uiState.query
        if (query.isBlank()) {
            uiState = uiState.copy(
                loadingMsg = null,
                blockingMsg = "Loading new stack.",
            ).apply {
                items.clear()
            }
            return
        }
        searchJob = viewModelScope.launch {
            delay(debounce)
            r_num_votes = (0..1000).random().toString() + ","
            r_runtime = (50..60).random().toString() + ","
            r_sort = r_sort_list[(0..5).random()]
            Log.d("RANDOMSORT:",r_sort)
            Log.d("RANDOMVOTES:",r_num_votes)
            Log.d("RANDOMRUNTIME:",r_runtime)
            searchMovie.search(
                num_votes = r_num_votes,
                runtime = r_runtime,
                count = IMDBContants.count,
                languages = "$query,",
                sort = r_sort
            ).collect {
                when (it) {
                    is Resource.Error -> {
                        Timber.d("onPageEndReached: Error: '${it.errorData}'")
                        uiState = uiState.copy(
                            loadingMsg = null,
                            blockingMsg = it.errorData
                        )
                    }
                    is Resource.Loading -> {
                        val message = if (totalPages == -1) {
                            "Loading Movie stack"
                        } else {
                            "Loading new Movie stack"
                        }
                        uiState = uiState.copy(
                            loadingMsg = message,
                            blockingMsg = null,
                            subTitle = if (pageNo == 1) {
                                null
                            } else {
                                uiState.subTitle
                            }
                        )
                    }
                    is Resource.Success -> {
                        val remoteItems = it.data.items
                        with(uiState) {
                            this.items.clear()
                            if (remoteItems.isNotEmpty()) {
                                totalPages = it.data.totalCount / remoteItems.size
                                this.items.addAll(remoteItems)
                                var lang = ""
                                if(query == "en"){
                                    lang = "english"
                                } else if(query == "de") {
                                    lang = "german"
                                } else if(query == "fr") {
                                    lang = "french"
                                }
                                uiState = copy(
                                    loadingMsg = null,
                                    blockingMsg = null,
                                    //subTitle = "Found ${it.data.totalCount} movie(s)"
                                    subTitle = "Movies in $lang language"
                                )
                                this.items.shuffle()

                            } else {
                                totalPages = -1
                                uiState = copy(
                                    loadingMsg = null,
                                    blockingMsg = "No movies found for '$query'",
                                    subTitle = null
                                )
                            }
                        }

                    }
                }
            }
        }
    }



    /*
    Favorite Handling without Room
     */

    private val _favoriteMovies = mutableStateListOf<Item>() // mutableListOf<Movie>()

    val favoriteMovies: List<Item>
        get() = _favoriteMovies



    fun addToFavorites(movie: Item) {
        if(!isFavorite(movie = movie)){
            _favoriteMovies.add(movie)
        }
    }

    fun removeFromFavorites(movie: Item){
        _favoriteMovies.remove(movie)
    }

    fun isFavorite(movie: Item) : Boolean {
        return _favoriteMovies.any {m -> m.id == movie.id }
    }

    /*
    Favorite Handling with Room
     */

    private var _movies = MutableStateFlow<List<Item>>(emptyList())//mutableStateListOf<Note>()
    val movies = _movies.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) { // launch a coroutine in IO thread
            movieRepository.getAllMovies().distinctUntilChanged()
                .collect{ listOfMovies ->
                    if(listOfMovies.isNullOrEmpty()){
                        Log.d("FavViewModel", "Empty movie list")
                        _movies.value = listOfMovies
                    } else {
                        _movies.value = listOfMovies
                    }
                }
        }
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.getAllMovies().collect{ listOfMovies ->
                if(listOfMovies.isNullOrEmpty()){
                    Log.d("FavViewModel", "No movies")
                    _movies.value = listOfMovies
                } else {
                    _movies.value = listOfMovies
                }
            }
        }

    }

    fun addMovie(movie: Item){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.addMovie(movie = movie)
        }
    }

    fun removeMovie(movie: Item){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.deleteMovie(movie)
        }

    }

    fun getAllMovies(){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.getAllMovies()
        }
    }

    fun exists(id: String): Boolean {
        var a = false
        viewModelScope.launch(Dispatchers.IO) {
            a = movieRepository.exists(id)

        }
        return a
    }

    fun deleteAll() = viewModelScope.launch { movieRepository.deleteAll() }
    fun editMovie(movie: Item){
        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.editMovie(movie = movie)
        }
    }

    fun sortMovies(){

    }

    fun filterMovies(){

    }

}
