package io.proteus.ui.di

import io.proteus.ui.data.FeatureBookRepository

internal object ProteusPresentationInjection {

    val featureBookRepository: FeatureBookRepository by lazy {
        FeatureBookRepository()
    }
}
