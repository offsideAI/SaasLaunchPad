package app.saaslaunchpad.saaslaunchpadapp.domain.model

import androidx.compose.ui.graphics.Color
import app.saaslaunchpad.saaslaunchpadapp.ui.theme.freshColor
import app.saaslaunchpad.saaslaunchpadapp.ui.theme.staleColor


enum class RateStatus (
    val title: String,
    val color: Color
) {
    Idle(
        title = "Rates",
        color = Color.White
    ),
    Fresh(
        title = "Fresh rates",
        color = freshColor
    ),
    Stale(
        title = "Rates are stale",
        color = staleColor
    )
}