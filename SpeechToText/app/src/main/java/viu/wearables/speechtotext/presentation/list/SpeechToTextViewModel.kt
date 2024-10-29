package viu.wearables.speechtotext.presentation.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import viu.wearables.speechtotext.presentation.models.SpeechToText
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.http.Part
import viu.wearables.speechtotext.presentation.UiEvent
import viu.wearables.speechtotext.repository.Repository
import viu.wearables.speechtotext.utils.Resultado

class SpeechToTextViewModel(repository: Repository,@Part file: MultipartBody.Part)  : ViewModel() {
    private val _speechToText: MutableState<SpeechToText> = mutableStateOf(SpeechToText())
    var speechToText: State<SpeechToText> = _speechToText

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            val res = repository.subirAudio(file)
            when(res) {
                is Resultado.Error -> _eventFlow.emit(UiEvent.ShowMessage("Error api rest"))
                is Resultado.Loading -> _eventFlow.emit(UiEvent.ShowMessage("Loading data"))
                is Resultado.Success -> {
                    _speechToText.value = res.data ?: SpeechToText()
                    println(res.data)
                }

            }

        }
    }
}