package com.example.dogchallenge.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import com.example.dogchallenge.R
import com.example.dogchallenge.api.models.Breed
import com.example.dogchallenge.ui.models.BreedUI
import com.example.dogchallenge.ui.widgets.ErrorDialog
import com.example.dogchallenge.ui.widgets.LoadingView
import com.example.dogchallenge.viewmodels.BreedListViewModel
import com.example.dogchallenge.viewmodels.PAGE_SIZE
import org.koin.android.viewmodel.ext.android.viewModel

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalCoilApi
class BreedListFragment : Fragment() {
    private val breedListViewModel by viewModel<BreedListViewModel>()
    private lateinit var displayList: MutableState<Boolean>
    private lateinit var breedList: List<Breed>
    private lateinit var filteredBreedList: List<Breed>
    private var page: Int = 0
    private var loading: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val selectedTab = remember { mutableStateOf(0) }
                displayList = remember { mutableStateOf(true) }
                breedList = breedListViewModel.breedList
                filteredBreedList = breedListViewModel.filteredBreedList
                loading = breedListViewModel.showLoading
                val error = breedListViewModel.showError
                val warning = breedListViewModel.showWarning
                page = breedListViewModel.page

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = stringResource(R.string.app_name)) },
                            actions = {
                                IconButton(onClick = {
                                    displayList.value = !displayList.value
                                }) {
                                    Icon(
                                        if (displayList.value) Icons.Filled.List else Icons.Filled.ShoppingCart,
                                        contentDescription = ""
                                    )
                                }
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "")
                                }
                            }
                        )
                    },
                    bottomBar = {
                        AppBottomBar(selectedTab)
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
                    if (warning != null) {
                        Toast.makeText(
                            context,
                            warning,
                            Toast.LENGTH_SHORT
                        ).show()
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
                        if (selectedTab.value == 0) {
                            BreedList(breedList)
                        } else {
                            SearchScreen()
                        }

                    }
                }
            }
        }
    }

    @ExperimentalFoundationApi
    @Composable
    fun BreedList(breeds: List<Breed>) {

        if (!displayList.value) {
            LazyVerticalGrid(cells = GridCells.Fixed(2)) {
                itemsIndexed(items = breeds) { index, breed ->

                    if (index > 0 && page > 0 && index >= ((page * PAGE_SIZE) - 1) && !loading) {
                        breedListViewModel.nextPage()
                    }
                    Breed(breed)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                itemsIndexed(items = breeds) { index, breed ->

                    if (index > 0 && page > 0 && index >= ((page * PAGE_SIZE) - 1) && !loading) {
                        breedListViewModel.nextPage()
                    }
                    Breed(breed)
                }
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
                    val bundle = Bundle()
                    val breedUI = BreedUI(breed)
                    bundle.putParcelable("breed", breedUI)
                    findNavController().navigate(R.id.breedDetailsFragment, bundle)
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
                LoadPicture(url = breed.image?.url)
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = breed.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    @ExperimentalComposeUiApi
    @Composable
    fun SearchScreen() {
        var query by remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                color = MaterialTheme.colors.primary,
                elevation = 8.dp,
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = query,
                        onValueChange = {
                            query = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        label = { Text(text = "Search") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search
                        ),
                        leadingIcon = { Icon(Icons.Filled.Search, "") },
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                keyboardController?.hide()
                                breedListViewModel.searchBreed(query)
                            }
                        ),
                        textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.surface
                        )
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
            ) {
                itemsIndexed(items = filteredBreedList) { index, breed ->
                    SearchBreedItem(breed)
                }
            }
        }
    }

    @Composable
    fun SearchBreedItem(breed: Breed) {
        Card(
            modifier = Modifier
                .background(shape = RoundedCornerShape(10.dp), color = Color.LightGray)
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(4.dp)
                .clickable {
                    val bundle = Bundle()
                    val breedUI = BreedUI(breed)
                    bundle.putParcelable("breed", breedUI)
                    findNavController().navigate(R.id.breedDetailsFragment, bundle)
                },
            elevation = 4.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(4.dp),
                horizontalAlignment = Alignment.Start
            ) {
                TextDetailWidget(
                    detailName = stringResource(R.string.breed_name),
                    detailValue = breed.name
                )
                Spacer(modifier = Modifier.size(4.dp))
                TextDetailWidget(
                    detailName = stringResource(R.string.breed_group),
                    detailValue = breed.breed_group
                )
                Spacer(modifier = Modifier.size(4.dp))
                TextDetailWidget(
                    detailName = stringResource(R.string.breed_origin),
                    detailValue = breed.origin ?: "N/A"
                )
            }
        }
    }

    @Composable
    fun TextDetailWidget(detailName: String, detailValue: String) {
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Blue)) {
                    append("$detailName ")
                }
                append(detailValue)
            },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = TextStyle(fontSize = 15.sp)
        )
    }


    @Composable
    fun AppBottomBar(selectedTab: MutableState<Int>) {

        BottomAppBar(
            elevation = 10.dp,
            backgroundColor = Color.Blue
        ) {
            BottomNavigationItem(
                icon = {
                    Icon(Icons.Filled.List, "")
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(alpha = 0.4f),
                onClick = {
                    selectedTab.value = 0
                    breedListViewModel.breedList
                },
                selected = selectedTab.value == 0
            )

            BottomNavigationItem(
                icon = {
                    Icon(Icons.Filled.Search, "")
                },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.White.copy(alpha = 0.4f),
                onClick = {
                    selectedTab.value = 1
                },
                selected = selectedTab.value == 1
            )
        }
    }

    @ExperimentalCoilApi
    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun LoadPicture(url: String?) {
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .build()

        CompositionLocalProvider(LocalImageLoader provides imageLoader) {
            val painter =
                if (url == null) painterResource(R.drawable.placeholder) else rememberImagePainter(
                    url
                )
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