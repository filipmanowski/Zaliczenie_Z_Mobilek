package pl.kul.aplikacjemobilneprojektzaliczeniowy.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pl.kul.aplikacjemobilneprojektzaliczeniowy.R
import pl.kul.aplikacjemobilneprojektzaliczeniowy.data.AppDatabase
import java.text.NumberFormat
import java.util.Locale


@Composable
fun HomeScreen(onAddClick: () -> Unit) {

    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val scope = rememberCoroutineScope()

    val transactions by db
        .transactionDao()
        .getAll()
        .collectAsState(initial = emptyList())

    val balance = transactions.sumOf { it.amount }

    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("pl", "PL"))

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
            Text(text = stringResource(R.string.balance))
            Text(text = currencyFormatter.format(balance))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onAddClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.add_transaction))
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(transactions) { transaction ->
                TransactionItem(
                    transaction = transaction,
                    onDelete = {
                        scope.launch {
                            db.transactionDao().delete(transaction)
                        }
                    }
                )
            }
        }
    }
}
@Composable
fun TransactionItem(
    transaction: pl.kul.aplikacjemobilneprojektzaliczeniowy.data.Transaction,
    onDelete: () -> Unit
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("pl", "PL"))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDelete() }
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = transaction.description,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = formatter.format(transaction.amount),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
