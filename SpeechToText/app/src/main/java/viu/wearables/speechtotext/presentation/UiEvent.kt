package viu.wearables.speechtotext.presentation

sealed interface UiEvent {
    data class ShowMessage(val message: String) : UiEvent
}