package io.proteus.core.provider

import io.proteus.core.data.MockConfigRepositoryImpl
import io.proteus.core.data.MockConfigStorage
import io.proteus.core.mock.FeatureTestGuide
import io.proteus.core.mock.MemoryFeatureConfigProvider
import io.proteus.core.mock.MemoryMockConfigStorage
import org.junit.Before
import org.junit.Test

internal class ConfigSubstitutionTest {

    private val memoryMockConfigStorage: MockConfigStorage = MemoryMockConfigStorage()
    private val mockConfigProvider: MockConfigProvider =
        MockConfigProvider(MockConfigRepositoryImpl(memoryMockConfigStorage))

    @Before
    fun setUp() {
        memoryMockConfigStorage.clear()
    }

    @Test
    fun `verify that mocked boolean config is used when available`() {
        // Given
        val featureKey = "featureA"
        val expectedValue = true
        memoryMockConfigStorage.save(featureKey, expectedValue)

        val featureTestGuideA = FeatureTestGuide(
            featureKey = featureKey,
            mockValue = false,
            remoteValue = false,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuideA)
        )

        // When
        val mockedValueOfFeatureA = configProvider.getBoolean(featureKey)

        // Then
        assert(mockedValueOfFeatureA == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureA]"
        }
    }

    @Test
    fun `verify that remote boolean config is used when mocked config is not available`() {
        // Given
        val featureKey = "featureA"
        val expectedValue = true

        val featureTestGuideA = FeatureTestGuide(
            featureKey = featureKey,
            mockValue = false,
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuideA)
        )

        // When
        val mockedValueOfFeatureA = configProvider.getBoolean(featureKey)

        // Then
        assert(mockedValueOfFeatureA == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureA]"
        }
    }

    @Test
    fun `verify that mocked string config is used when available`() {
        // Given
        val featureKey = "featureB"
        val expectedValue = "this is a string"
        memoryMockConfigStorage.save(featureKey, expectedValue)

        val featureTestGuideB = FeatureTestGuide(
            featureKey = featureKey,
            mockValue = "",
            remoteValue = "",
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuideB)
        )

        // When
        val mockedValueOfFeatureB = configProvider.getString(featureKey)

        // Then
        assert(mockedValueOfFeatureB == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureB]"
        }
    }

    @Test
    fun `verify that remote string config is used when mocked config is not available`() {
        // Given
        val featureKey = "featureB"
        val expectedValue = "this is a string"

        val featureTestGuideB = FeatureTestGuide(
            featureKey = featureKey,
            mockValue = "",
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuideB)
        )

        // When
        val mockedValueOfFeatureB = configProvider.getString(featureKey)

        // Then
        assert(mockedValueOfFeatureB == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureB]"
        }
    }

    @Test
    fun `verify that mocked long config is used when available`() {
        // Given
        val featureKey = "featureC"
        val expectedValue = 5L
        memoryMockConfigStorage.save(featureKey, expectedValue)

        val featureTestGuideC = FeatureTestGuide(
            featureKey = featureKey,
            mockValue = 0L,
            remoteValue = 0L,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuideC)
        )

        // When
        val mockedValueOfFeatureC = configProvider.getLong(featureKey)

        // Then
        assert(mockedValueOfFeatureC == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureC]"
        }
    }

    @Test
    fun `verify that remote long config is used when mocked config is not available`() {
        // Given
        val featureKey = "featureC"
        val expectedValue = 5L

        val featureTestGuideC = FeatureTestGuide(
            featureKey = featureKey,
            mockValue = 0L,
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuideC)
        )

        // When
        val mockedValueOfFeatureC = configProvider.getLong(featureKey)

        // Then
        assert(mockedValueOfFeatureC == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureC]"
        }
    }

    @Test
    fun `verify that mocked double config is used when available`() {
        // Given
        val featureKey = "featureD"
        val expectedValue = 5.0
        memoryMockConfigStorage.save(featureKey, expectedValue)

        val featureTestGuideD = FeatureTestGuide(
            featureKey = featureKey,
            mockValue = 0.0,
            remoteValue = 0.0,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuideD)
        )

        // When
        val mockedValueOfFeatureD = configProvider.getDouble(featureKey)

        // Then
        assert(mockedValueOfFeatureD == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureD]"
        }
    }

    @Test
    fun `verify that remote double config is used when mocked config is not available`() {
        // Given
        val featureKey = "featureD"
        val expectedValue = 5.0

        val featureTestGuideD = FeatureTestGuide(
            featureKey = featureKey,
            mockValue = 0.0,
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuideD)
        )

        // When
        val mockedValueOfFeatureD = configProvider.getDouble(featureKey)

        // Then
        assert(mockedValueOfFeatureD == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureD]"
        }
    }

    private fun provideMemoryConfigFactory(vararg featuresGuide: FeatureTestGuide<*>): FeatureConfigProviderFactory {
        return object : FeatureConfigProviderFactory {
            override fun getProvider(featureKey: String): FeatureConfigProvider {
                return MemoryFeatureConfigProvider(*featuresGuide)
            }

            override fun getProviderTag(featureKey: String): String {
                return "MemoryMockConfigProvider"
            }
        }
    }
}
