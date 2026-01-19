package pl.kul.aplikacjemobilneprojektzaliczeniowy.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import pl.kul.aplikacjemobilneprojektzaliczeniowy.R
import pl.kul.aplikacjemobilneprojektzaliczeniowy.data.AppDatabase
import pl.kul.aplikacjemobilneprojektzaliczeniowy.data.Transaction

@Composable
fun AddTransactionScreen(
    transactionId: Int?,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val scope = rememberCoroutineScope()

    val transactionFlow = remember(transactionId) {
        if (transactionId == null) {
            flowOf(null)
        } else {
            db.transactionDao().getById(transactionId)
        }
    }

    val transaction by transactionFlow.collectAsState(initial = null)

    var amount by rememberSaveable(transaction) {
        mutableStateOf(transaction?.amount?.toString() ?: "")
    }

    var description by rememberSaveable(transaction) {
        mutableStateOf(transaction?.description ?: "")
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text =
                if (transactionId == null)
                    stringResource(R.string.add_transaction)
                else
                    stringResource(R.string.edit_transaction),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.description)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text(stringResource(R.string.amount)) },
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val amountValue = amount.toDoubleOrNull()

                if (amount.isBlank() || description.isBlank() || amountValue == 0.0) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.fill_all_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@Button
                }

                scope.launch {
                    if (transactionId == null) {
                        db.transactionDao().insert(
                            Transaction(
                                amount = amount.toDouble(),
                                description = description
                            )
                        )
                    } else {
                        db.transactionDao().update(
                            Transaction(
                                id = transactionId,
                                amount = amount.toDouble(),
                                description = description
                            )
                        )
                    }
                }

                Toast.makeText(
                    context,
                    context.getString(R.string.transaction_saved),
                    Toast.LENGTH_SHORT
                ).show()

                onBack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save))
        }
    }
}
