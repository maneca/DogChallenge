package com.example.dogchallenge.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import com.example.dogchallenge.R
import com.example.dogchallenge.api.models.Breed
import com.example.dogchallenge.ui.widgets.ErrorDialog
import com.example.dogchallenge.ui.widgets.LoadingView
import com.example.dogchallenge.viewmodels.BreedListViewModel
import com.example.dogchallenge.viewmodels.PAGE_SIZE
import org.koin.android.viewmodel.ext.android.viewModel

@ExperimentalCoilApi
class BreedListFragment : Fragment() {
    private val breedListViewModel by viewModel<BreedListViewModel>()
    private var page: Int = 0
    private var loading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val breedList = breedListViewModel.completedChallenges
                loading = breedListViewModel.showLoading
                val error = breedListViewModel.showError
                page = breedListViewModel.page

                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(R.string.app_name)) }
                    )
                },
                    bottomBar = {
                        BottomAppBar {

                        }
                    }) {
                    if (loading) {
                        LoadingView()
                    }

                    if (error != null) {
                        ErrorDialog(
                            message = stringResource(error),
                            title = getString(R.string.error_title),
                            dismissText = getString(R.string.dismiss_button)
                        )
                    }
                    if (breedList.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.nothing_to_show),
                            )
                        }
                    } else {
                        BreedList(breedList)
                    }
                }
            }
        }
    }

    @Composable
    fun BreedList(breeds: List<Breed>) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            itemsIndexed(items = breeds) { index, breed ->

                if (index > 0 && page > 0 && index >= ((page * PAGE_SIZE) - 1) && !loading) {
                    //if (canLoad) {
                    breedListViewModel.nextPage()
                    //}
                }
                Breed(breed)
            }
        }
    }

    @ExperimentalCoilApi
    @Composable
    fun Breed(breed: Breed) {
        Card(
            modifier = Modifier
                .background(shape = RoundedCornerShape(10.dp), color = Color.LightGray)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(4.dp)
                .clickable {
                },
            elevation = 4.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoadPicture(url = breed.url)
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = if (breed.breeds.isNotEmpty()) breed.breeds[0].name else stringResource(R.string.no_name),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    @ExperimentalCoilApi
    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun LoadPicture(url: String, fullscreen: Boolean = false) {
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .build()

        CompositionLocalProvider(LocalImageLoader provides imageLoader) {
            val painter = rememberImagePainter(url)
            Image(
                painter = painter,
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RectangleShape),
                contentScale = ContentScale.Fit
            )
        }
    }
}