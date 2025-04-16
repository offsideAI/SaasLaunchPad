package app.saaslaunchpad.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val name: String,
    val description: String,
    val imageUrl: String
)