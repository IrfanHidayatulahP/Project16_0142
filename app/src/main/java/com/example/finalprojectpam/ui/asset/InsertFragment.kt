package com.example.finalprojectpam.ui.asset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.example.finalprojectpam.R
import com.example.finalprojectpam.ui.ViewModel.PenyediaViewModel
import kotlinx.coroutines.launch

class InsertFragment : Fragment() {

    private lateinit var viewModel: InsertAssetViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Using the custom factory to create the AssetViewModel
        viewModel = ViewModelProvider(this, PenyediaViewModel.Factory).get(InsertAssetViewModel::class.java)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    EntryAsetScreen(
                        navigateBack = { navigateToAssetFragment() },
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    private fun navigateToAssetFragment() {
        // Navigasi menggunakan NavController
        findNavController().navigate(R.id.navigation_asset)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryAsetScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertAssetViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Tambah Aset") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            EntryBody(
                insertUiState = viewModel.uiState,
                onAsetValueChange = viewModel::updateInsertAsetState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.insertAset()
                        navigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }
    }
}

@Composable
fun EntryBody(
    insertUiState: InsertUiState,
    onAsetValueChange: (InsertUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = Modifier.padding(12.dp)
    ) {
        FormInput(
            insertUiEvent = insertUiState.insertUiEvent,
            onValueChange = onAsetValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInput(
    insertUiEvent: InsertUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertUiEvent.nama_aset,
            onValueChange = { onValueChange(insertUiEvent.copy(nama_aset = it)) },
            label = { Text("Nama Aset") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        if (enabled) {
            Text(
                text = "Isi Semua Data!",
                modifier = Modifier.padding(12.dp)
            )
        }
        Divider(
            thickness = 8.dp,
            modifier = Modifier.padding(12.dp)
        )
    }
}
