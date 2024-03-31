package info.navidlabs.intenttutorial

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel

class BackgroundColorViewModel(
    val message: String
): ViewModel() {
    var backgroundColor by mutableStateOf(Color.White)
        private set

    fun changeBackgroundColor(newColor: Color = Color.Red) {
        backgroundColor = newColor
        Log.i("BackgroundViewModel", "message: $message")
    }
}