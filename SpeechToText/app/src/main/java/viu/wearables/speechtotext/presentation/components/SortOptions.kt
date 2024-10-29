package viu.wearables.speechtotext.presentation.components

import viu.wearables.speechtotext.data.HistoryData
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun SortOptions ( historyOrder: SortOrder = SortByTitle,
onSortOrderChange: (SortOrder) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        HistoryRadioButton(text = "Title",
            selected = historyOrder is SortByTitle,
            onSelect = { onSortOrderChange(SortByTitle) })

    }
}

sealed class SortOrder()
data object SortByTitle : SortOrder()

data class NotesState(
    val historyDataList: List<HistoryData> = emptyList(),
    val historyOrder: SortOrder = SortByTitle
)

sealed class HistoryEvent {
    data class Order(val order: SortOrder) : HistoryEvent()
    data class Delete(val historyData: HistoryData) : HistoryEvent()
}