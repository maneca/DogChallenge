package com.example.dogchallenge.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.annotation.ExperimentalCoilApi
import com.example.dogchallenge.R
import com.example.dogchallenge.api.models.Breed
import com.example.dogchallenge.ui.models.BreedUI
import com.example.dogchallenge.ui.widgets.ErrorDialog
import com.example.dogchallenge.ui.widgets.LoadPicture
import com.example.dogchallenge.ui.widgets.LoadingView
import com.example.dogchallenge.ui.widgets.TextDetailWidget
import com.example.dogchallenge.utils.UiState
import com.example.dogchallenge.viewmodels.BreedListViewModel
import com.example.dogchallenge.viewmodels.PAGE_SIZE
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalCoilApi
class BreedListFragment : Fragment() {
    private val breedListViewModel by viewModel<BreedListViewModel>()
    private lateinit var breedList: List<Breed>
    private lateinit var filteredBreedList: List<Breed>
    private var ascendingOrder: Boolean = true
    private var page: Int = 0
    private var loading: Boolean = false
    private var uiState : UiState? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val selectedTab = remember { mutableStateOf(0) }
                val displayList = remember { mutableStateOf(true) }
                val listState = rememberLazyListState()
                ascendingOrder = breedListViewModel.ascendingOrder
                breedList = breedListViewModel.breedList
                filteredBreedList = breedListViewModel.filteredBreedList
                uiState = breedListViewModel.state
                page = breedListViewModel.page

                Scaffold(
                    bottomBar = {
                        AppBottomBar(selectedTab)
                    }) { innerPadding ->

                    when(uiState){
                        is UiState.Loading ->{
                            if ((uiState as UiState.Loading).show){
                                LoadingView()
                            }

                        }

                        is UiState.Error -> {
                            val error = (uiState as UiState.Error).message
                            if (error != null) {
                                ErrorDialog(
                                    message = stringResource(error),
                                    title = getString(R.string.error_title),
                                    dismissText = getString(R.string.dismiss_button)
                                )
                            }
                        }

                        is UiState.Warning -> {
                            val warning = (uiState as UiState.Warning).message
                            if (warning != null) {
                                Toast.makeText(
                                    context,
                                    warning,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    Box(
                        modifier = Modifier.padding(
                            bottom = innerPadding.calculateBottomPadding()
                        )
                    ) {
                        if (selectedTab.value == 0) {
                            BreedListScreen(breedList, displayList, ascendingOrder, listState)
                        } else {
                            SearchScreen()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun BreedListScreen(
        breeds: List<Breed>,
        displayList: MutableState<Boolean>,
        ascendingOrder: Boolean,
        state: LazyListState
    ) {
        val coroutineScope = rememberCoroutineScope()

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
                    Text(
                        stringResource(id = R.string.breed_list),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .weight(0.7f)
                            .align(CenterVertically)
                    )
                    IconButton(modifier = Modifier.weight(0.15f),
                        onClick = {
                            displayList.value = !displayList.value
                        }) {
                        Icon(
                            painter = painterResource(id = if (displayList.value) R.drawable.icon_list else R.drawable.icon_grid),
                            contentDescription = ""
                        )
                    }
                    IconButton(
                        modifier = Modifier.weight(0.15f),
                        onClick = {
                            breedListViewModel.changeSortingOrder()
                            breedListViewModel.getBreeds(0, true)
                            coroutineScope.launch {
                                state.animateScrollToItem(0)
                            }
                        }) {
                        Icon(
                            painter = painterResource(id = if (ascendingOrder) R.drawable.icon_ascending else R.drawable.icon_descending),
                            contentDescription = ""
                        )
                    }
                }
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
                if (!displayList.value) {
                    LazyVerticalGrid(cells = GridCells.Fixed(2)) {
                        itemsIndexed(items = breeds) { index, breed ->
                            DisplayBreed(index = index, breed = breed)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        state = state
                    ) {
                        itemsIndexed(items = breeds) { index, breed ->
                            DisplayBreed(index = index, breed = breed)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun DisplayBreed(index: Int, breed: Breed) {
        if (index > 0 && page > 0 && index >= ((page * PAGE_SIZE) - 1) && !loading) {
            breedListViewModel.nextPage()
        }
        Breed(breed)
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
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
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
            if (filteredBreedList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.nothing_to_show),
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    itemsIndexed(items = filteredBreedList) { _, breed ->
                        SearchBreedItem(breed)
                    }
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
                    detailValue = breed.breed_group ?: "N/A"
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
    fun AppBottomBar(selectedTab: MutableState<Int>) {

        BottomAppBar(
            elevation = 10.dp,
            backgroundColor = Color.Blue
        ) {
            BottomNavigationItem(
                icon = {
                    Icon(painter = painterResource(id = R.drawable.icon_dog), "")
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


}