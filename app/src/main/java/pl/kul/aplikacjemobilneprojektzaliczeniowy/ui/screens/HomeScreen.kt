package pl.kul.aplikacjemobilneprojektzaliczeniowy.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pl.kul.aplikacjemobilneprojektzaliczeniowy.R
import pl.kul.aplikacjemobilneprojektzaliczeniowy.data.AppDatabase
import pl.kul.aplikacjemobilneprojektzaliczeniowy.data.Transaction
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeScreen(
    onAddClick: () -> Unit,
    onEditClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val scope = rememberCoroutineScope()

    val transactions by db.transactionDao()
        .getAll()
        .collectAsState(initial = emptyList())

    val balance = transactions.sumOf { it.amount }
    val formatter = NumberFormat.getCurrencyInstance(Locale("pl", "PL"))

    var transactionToDelete by remember { mutableStateOf<Transaction?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = stringResource(R.string.home_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(R.string.balance))
            Text(formatter.format(balance))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onAddClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.add_transaction))
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(transactions) { transaction ->
                TransactionItem(
                    transaction = transaction,
                    onEdit = { onEditClick(transaction.id) },
                    onDeleteClick = {
                        transactionToDelete = transaction
                    }
                )
            }
        }
    }

    transactionToDelete?.let { transaction ->
        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            title = { Text("Potwierdzenie") },
            text = { Text(stringResource(R.string.are_you_sure)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            db.transactionDao().delete(transaction)
                        }
                        transactionToDelete = null
                    }
                ) {
                    Text(stringResource(R.string.yes))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { transactionToDelete = null }
                ) {
                    Text(stringResource(R.string.no))
                }
            }
        )
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    onEdit: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("pl", "PL"))

    val amountColor =
        if (transaction.amount < 0)
            MaterialTheme.colorScheme.error
        else
            MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable { onEdit() }
        ) {
            Text(transaction.description)
            Text(
                text = formatter.format(transaction.amount),
                color = amountColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = "Usuń")
        }
    }
}


