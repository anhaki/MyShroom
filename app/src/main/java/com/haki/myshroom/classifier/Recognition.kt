package com.haki.myshroom.classifier

import com.haki.myshroom.utils.mushrooms

data class Recognition(
    val label: Int,
    val confidence: Float,
    val timeCost: Long
)

fun Recognition.toNamedLabel(): RecognitionWithNamedLabel {
    val namedLabel = if (label in mushrooms.indices) mushrooms[label] else "Unknown"
    return RecognitionWithNamedLabel(namedLabel, confidence, timeCost)
}

data class RecognitionWithNamedLabel(
    val namedLabel: String,
    val confidence: Float,
    val timeCost: Long
)

