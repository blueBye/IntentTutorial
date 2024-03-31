package info.navidlabs.intenttutorial

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import info.navidlabs.intenttutorial.ui.theme.IntentTutorialTheme
import java.io.File

class FileWriter(private val context: Context) {
    fun writeData(data: String) {
        val fileName = "temp.txt"
        val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//        val folder = context.filesDir
        val file = File(folder, fileName)
        try {
            file.createNewFile()
            file.appendText(data)
            Toast.makeText(context, "written to $folder/$fileName", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e("FileWriter", "Error writing to file: ${e.message}")
        }
    }
}


class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<ImageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fileWriter = FileWriter(this)

        setContent {
            IntentTutorialTheme {
                val bgviewModel = viewModel<BackgroundColorViewModel>(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return BackgroundColorViewModel(message="testing") as T
                        }
                    }
                )

                Column(
                    modifier = Modifier.fillMaxSize().background(bgviewModel.backgroundColor),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    viewModel.uri?.let {
                        AsyncImage(
                            model = viewModel.uri,
                            contentDescription = null
                        )
                    }

                    Button(onClick = {
                        fileWriter.writeData("hello world\n")
                    }) {
                        Text(text = "Write")
                    }

                    Button(onClick = {
                        bgviewModel.changeBackgroundColor()
                    }) {
                        Text(text = "Change background color to Red")
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
        } else {
            intent?.getParcelableExtra(Intent.EXTRA_STREAM)
        }

        viewModel.updateUri(uri)
    }
}
