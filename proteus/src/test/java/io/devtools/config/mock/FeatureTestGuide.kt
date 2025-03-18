package io.devtools.config.mock

import io.devtools.config.provider.Feature

internal data class FeatureTestGuide<DataType : Any>(
    val feature: Feature<DataType>,
    val mockValue: DataType,
    val remoteValue: DataType,
    val givenSource: Source
) {

    fun getValue(): DataType {
        return if (givenSource == Source.Mock) {
            mockValue
        } else {
            remoteValue
        }
    }

    sealed class Source {

        data object Mock : Source()

        data object Remote : Source()
    }
}
