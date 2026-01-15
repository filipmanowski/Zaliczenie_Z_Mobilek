package pl.kul.aplikacjemobilneprojektzaliczeniowy.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.kul.aplikacjemobilneprojektzaliczeniowy.R
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import pl.kul.aplikacjemobilneprojektzaliczeniowy.data.AppDatabase
import pl.kul.aplikacjemobilneprojektzaliczeniowy.data.Transaction




@Composable
fun AddTransactionScreen(onBack: () -> Unit){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val db = AppDatabase.getDatabase(context)


    var amount by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier.
        fillMaxSize().
        padding(16.dp)
    ) {
        Text(
            text = stringResource(id= R.string.add_transaction),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text(stringResource(id = R.string.amount)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(id = R.string.description)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (amount.isBlank() || description.isBlank()) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.fill_all_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    scope.launch {
                        db.transactionDao().insert(
                            Transaction(
                                amount = amount.toDouble(),
                                description = description
                            )
                        )
                    }

                    Toast.makeText(
                        context,
                        context.getString(R.string.transaction_saved),
                        Toast.LENGTH_SHORT
                    ).show()

                    onBack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        {
            Text(text = stringResource(id = R.string.save))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddTransactionPreview() {
//    AddTransactionScreen()
}