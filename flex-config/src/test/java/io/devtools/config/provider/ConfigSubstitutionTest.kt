package io.devtools.config.provider

import io.devtools.config.data.MockConfigRepositoryImpl
import io.devtools.config.data.MockConfigStorage
import io.devtools.config.mock.FeatureTestGuide
import io.devtools.config.mock.MemoryFeatureConfigProvider
import io.devtools.config.mock.MemoryMockConfigStorage
import io.devtools.config.mock.MockFeatureConfigOwner
import org.junit.Before
import org.junit.Test

class ConfigSubstitutionTest {

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
            MockFeatureConfigOwner.Firebase
        )

        val expectedValue = true
        memoryMockConfigStorage.save(feature.key, expectedValue)

        val featureTestGuideA = FeatureTestGuide(
            feature = feature,
            mockValue = false,
            remoteValue = false,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configFactory = object : FeatureConfigProviderFactory {
            override fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider {
                return MemoryFeatureConfigProvider(featureTestGuideA)
            }
        }

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            configFactory
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
            MockFeatureConfigOwner.Firebase
        )

        val expectedValue = true

        val featureTestGuideA = FeatureTestGuide(
            feature = feature,
            mockValue = false,
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val configFactory = object : FeatureConfigProviderFactory {
            override fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider {
                return MemoryFeatureConfigProvider(featureTestGuideA)
            }
        }

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            configFactory
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
            MockFeatureConfigOwner.Firebase
        )

        val expectedValue = "this is a string"
        memoryMockConfigStorage.save(feature.key, expectedValue)

        val featureTestGuideB = FeatureTestGuide(
            feature = feature,
            mockValue = "",
            remoteValue = "",
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configFactory = object : FeatureConfigProviderFactory {
            override fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider {
                return MemoryFeatureConfigProvider(featureTestGuideB)
            }
        }

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            configFactory
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
            MockFeatureConfigOwner.Firebase
        )

        val expectedValue = "this is a string"

        val featureTestGuideB = FeatureTestGuide(
            feature = feature,
            mockValue = "",
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val configFactory = object : FeatureConfigProviderFactory {
            override fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider {
                return MemoryFeatureConfigProvider(featureTestGuideB)
            }
        }

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            configFactory
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
            MockFeatureConfigOwner.Firebase
        )

        val expectedValue = 5L
        memoryMockConfigStorage.save(feature.key, expectedValue)

        val featureTestGuideC = FeatureTestGuide(
            feature = feature,
            mockValue = 0L,
            remoteValue = 0L,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configFactory = object : FeatureConfigProviderFactory {
            override fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider {
                return MemoryFeatureConfigProvider(featureTestGuideC)
            }
        }

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            configFactory
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
            MockFeatureConfigOwner.Firebase
        )

        val expectedValue = 5L

        val featureTestGuideC = FeatureTestGuide(
            feature = feature,
            mockValue = 0L,
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val configFactory = object : FeatureConfigProviderFactory {
            override fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider {
                return MemoryFeatureConfigProvider(featureTestGuideC)
            }
        }

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            configFactory
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
            MockFeatureConfigOwner.Firebase
        )

        val expectedValue = 5.0
        memoryMockConfigStorage.save(feature.key, expectedValue)

        val featureTestGuideD = FeatureTestGuide(
            feature = feature,
            mockValue = 0.0,
            remoteValue = 0.0,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val configFactory = object : FeatureConfigProviderFactory {
            override fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider {
                return MemoryFeatureConfigProvider(featureTestGuideD)
            }
        }

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            configFactory
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
            MockFeatureConfigOwner.Firebase
        )

        val expectedValue = 5.0

        val featureTestGuideD = FeatureTestGuide(
            feature = feature,
            mockValue = 0.0,
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val configFactory = object : FeatureConfigProviderFactory {
            override fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider {
                return MemoryFeatureConfigProvider(featureTestGuideD)
            }
        }

        val configProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            configFactory
        )

        // When
        val mockedValueOfFeatureD = configProvider.getDouble(feature)

        // Then
        assert(mockedValueOfFeatureD == expectedValue) {
            "Expected mocked value of feature is [$expectedValue], " +
                "but was [$mockedValueOfFeatureD]"
        }
    }
}
