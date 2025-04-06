package io.proteus.core.mock

internal data class FeatureTestGuide<DataType : Any>(
    val featureKey: String,
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
