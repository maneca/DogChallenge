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
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dogchallenge.R
import com.example.dogchallenge.ui.models.BreedUI
import com.example.dogchallenge.ui.widgets.TextDetailWidget

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

    @Composable
    fun BreedDetails(breedDetail: BreedUI) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            TextDetailWidget(
                detailName = stringResource(R.string.breed_name),
                detailValue = breedDetail.name
            )
            Spacer(modifier = Modifier.size(4.dp))
            TextDetailWidget(
                detailName = stringResource(R.string.breed_category),
                detailValue = breedDetail.category
            )
            Spacer(modifier = Modifier.size(4.dp))
            TextDetailWidget(
                detailName = stringResource(R.string.breed_origin),
                detailValue = breedDetail.origin ?: "N/A"
            )
            Spacer(modifier = Modifier.size(4.dp))
            TextDetailWidget(
                detailName = stringResource(R.string.breed_temperament),
                detailValue = breedDetail.temperament
            )
        }
    }

}