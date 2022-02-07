package com.example.dogchallenge.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.annotation.ExperimentalCoilApi
import com.example.dogchallenge.R
import com.example.dogchallenge.ui.models.BreedUI
import com.example.dogchallenge.ui.widgets.LoadPicture
import com.example.dogchallenge.ui.widgets.TextBox
import com.example.dogchallenge.ui.widgets.TextDetailWidget

@ExperimentalCoilApi
class BreedDetailsFragment: Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(data: BreedUI) = BreedDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable("breed", data)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            val breedDetails = arguments?.getParcelable<BreedUI>("breed")

            setContent {
                Scaffold(topBar = {
                    TopAppBar(
                        title = { Text(text = stringResource(R.string.breed_details)) },
                        navigationIcon = {
                            IconButton(onClick = {
                                findNavController().popBackStack()
                            }) {
                                Icon(Icons.Filled.ArrowBack, "")
                            }
                        }
                    )
                }) {

                    if (breedDetails == null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.nothing_to_show),
                            )
                        }
                    } else {
                        BreedDetails(breedDetails)
                    }

                }
            }
        }
    }

    @ExperimentalCoilApi
    @Composable
    fun BreedDetails(breedDetail: BreedUI) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            LoadPicture(url = breedDetail.url)
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = breedDetail.name,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.h4
            )
            Spacer(modifier = Modifier.size(4.dp))
            TextDetailWidget(
                detailName = stringResource(R.string.breed_category),
                detailValue = if (breedDetail.category.isEmpty()) "N/A" else breedDetail.category
            )
            Spacer(modifier = Modifier.size(4.dp))
            TextDetailWidget(
                detailName = stringResource(R.string.breed_origin),
                detailValue = if (breedDetail.origin.isNullOrEmpty()) "N/A" else breedDetail.origin
            )
            Spacer(modifier = Modifier.size(4.dp))
            TextDetailWidget(
                detailName = stringResource(R.string.breed_temperament) + "\n",
                detailValue = ""
            )
            breedDetail.temperament?.let { TextBox(it.split(","), Color(0xFF9CCC65), 10.dp) }
        }
    }

}