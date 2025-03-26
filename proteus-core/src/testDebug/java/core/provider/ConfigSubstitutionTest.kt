package core.provider

import core.mock.FeatureTestGuide
import core.mock.MemoryFeatureConfigProvider
import core.mock.MemoryMockConfigStorage
import core.mock.MockFeatureConfigOwner
import io.proteus.core.data.MockConfigRepositoryImpl
import io.proteus.core.data.MockConfigStorage
import io.proteus.core.domain.Feature
import io.proteus.core.provider.FeatureConfigProvider
import io.proteus.core.provider.FeatureConfigProviderFactory
import io.proteus.core.provider.FeatureConfigProviderImpl
import io.proteus.core.provider.MockConfigProvider
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
        val feature = Feature(
            "featureA",
            false,
            Boolean::class,
            MockFeatureConfigOwner.Firebase.serviceOwner
        )

        val expectedValue = true
        memoryMockConfigStorage.save(feature.key, expectedValue)

        val featureTestGuideA = FeatureTestGuide(
            feature = feature,
            mockValue = false,
            remoteValue = false,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuideA)
        )

        // When
        val mockedValueOfFeatureA = configProvider.getBoolean(feature)

        // Then
        assert(mockedValueOfFeatureA == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureA]"
        }
    }

    @Test
    fun `verify that remote boolean config is used when mocked config is not available`() {
        // Given
        val feature = Feature(
            "featureA",
            false,
            Boolean::class,
            MockFeatureConfigOwner.Firebase.serviceOwner
        )

        val expectedValue = true

        val featureTestGuideA = FeatureTestGuide(
            feature = feature,
            mockValue = false,
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuideA)
        )

        // When
        val mockedValueOfFeatureA = configProvider.getBoolean(feature)

        // Then
        assert(mockedValueOfFeatureA == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureA]"
        }
    }

    @Test
    fun `verify that mocked string config is used when available`() {
        // Given
        val feature = Feature(
            "featureB",
            "",
            String::class,
            MockFeatureConfigOwner.Firebase.serviceOwner
        )

        val expectedValue = "this is a string"
        memoryMockConfigStorage.save(feature.key, expectedValue)

        val featureTestGuideB = FeatureTestGuide(
            feature = feature,
            mockValue = "",
            remoteValue = "",
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuideB)
        )

        // When
        val mockedValueOfFeatureB = configProvider.getString(feature)

        // Then
        assert(mockedValueOfFeatureB == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureB]"
        }
    }

    @Test
    fun `verify that remote string config is used when mocked config is not available`() {
        // Given
        val feature = Feature(
            "featureB",
            "",
            String::class,
            MockFeatureConfigOwner.Firebase.serviceOwner
        )

        val expectedValue = "this is a string"

        val featureTestGuideB = FeatureTestGuide(
            feature = feature,
            mockValue = "",
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuideB)
        )

        // When
        val mockedValueOfFeatureB = configProvider.getString(feature)

        // Then
        assert(mockedValueOfFeatureB == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureB]"
        }
    }

    @Test
    fun `verify that mocked long config is used when available`() {
        // Given
        val feature = Feature(
            "featureC",
            0L,
            Long::class,
            MockFeatureConfigOwner.Firebase.serviceOwner
        )

        val expectedValue = 5L
        memoryMockConfigStorage.save(feature.key, expectedValue)

        val featureTestGuideC = FeatureTestGuide(
            feature = feature,
            mockValue = 0L,
            remoteValue = 0L,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuideC)
        )

        // When
        val mockedValueOfFeatureC = configProvider.getLong(feature)

        // Then
        assert(mockedValueOfFeatureC == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureC]"
        }
    }

    @Test
    fun `verify that remote long config is used when mocked config is not available`() {
        // Given
        val feature = Feature(
            "featureC",
            0L,
            Long::class,
            MockFeatureConfigOwner.Firebase.serviceOwner
        )

        val expectedValue = 5L

        val featureTestGuideC = FeatureTestGuide(
            feature = feature,
            mockValue = 0L,
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuideC)
        )

        // When
        val mockedValueOfFeatureC = configProvider.getLong(feature)

        // Then
        assert(mockedValueOfFeatureC == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureC]"
        }
    }

    @Test
    fun `verify that mocked double config is used when available`() {
        // Given
        val feature = Feature(
            "featureD",
            0.0,
            Double::class,
            MockFeatureConfigOwner.Firebase.serviceOwner
        )

        val expectedValue = 5.0
        memoryMockConfigStorage.save(feature.key, expectedValue)

        val featureTestGuideD = FeatureTestGuide(
            feature = feature,
            mockValue = 0.0,
            remoteValue = 0.0,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuideD)
        )

        // When
        val mockedValueOfFeatureD = configProvider.getDouble(feature)

        // Then
        assert(mockedValueOfFeatureD == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureD]"
        }
    }

    @Test
    fun `verify that remote double config is used when mocked config is not available`() {
        // Given
        val feature = Feature(
            "featureD",
            0.0,
            Double::class,
            MockFeatureConfigOwner.Firebase.serviceOwner
        )

        val expectedValue = 5.0

        val featureTestGuideD = FeatureTestGuide(
            feature = feature,
            mockValue = 0.0,
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuideD)
        )

        // When
        val mockedValueOfFeatureD = configProvider.getDouble(feature)

        // Then
        assert(mockedValueOfFeatureD == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureD]"
        }
    }

    private fun provideMemoryConfigFactory(vararg featuresGuide: FeatureTestGuide<*>): FeatureConfigProviderFactory {
        return object : FeatureConfigProviderFactory {
            override fun getProvider(owner: String): FeatureConfigProvider {
                return MemoryFeatureConfigProvider(*featuresGuide)
            }
        }
    }
}
