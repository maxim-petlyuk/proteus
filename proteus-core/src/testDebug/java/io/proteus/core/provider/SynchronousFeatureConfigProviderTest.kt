package io.proteus.core.provider

import io.proteus.core.data.MockConfigRepositoryImpl
import io.proteus.core.data.MockConfigStorage
import io.proteus.core.mock.FeatureTestGuide
import io.proteus.core.mock.MemoryFeatureConfigProvider
import io.proteus.core.mock.MemoryMockConfigStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.milliseconds

internal class SynchronousFeatureConfigProviderTest {

    private val memoryMockConfigStorage: MockConfigStorage = MemoryMockConfigStorage()
    private val mockConfigProvider: MockConfigProvider =
        MockConfigProvider(MockConfigRepositoryImpl(memoryMockConfigStorage))

    @Before
    fun setUp() = runTest {
        memoryMockConfigStorage.clear()
    }

    @Test
    fun `verify synchronous getBoolean returns correct value`() {
        // Given
        val featureKey = "sync_boolean_test"
        val expectedValue = true

        val featureTestGuide = FeatureTestGuide(
            featureKey = featureKey,
            mockValue = false,
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val suspendProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuide)
        )

        val syncProvider = SynchronousFeatureConfigProvider(suspendProvider)

        // When
        val result = syncProvider.getBoolean(featureKey)

        // Then
        assertTrue(result)
    }

    @Test
    fun `verify synchronous getString returns correct value`() {
        // Given
        val featureKey = "sync_string_test"
        val expectedValue = "sync test value"

        val featureTestGuide = FeatureTestGuide(
            featureKey = featureKey,
            mockValue = "",
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val suspendProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuide)
        )

        val syncProvider = SynchronousFeatureConfigProvider(suspendProvider)

        // When
        val result = syncProvider.getString(featureKey)

        // Then
        assertEquals(expectedValue, result)
    }

    @Test
    fun `verify synchronous getLong returns correct value`() {
        // Given
        val featureKey = "sync_long_test"
        val expectedValue = 12345L

        val featureTestGuide = FeatureTestGuide(
            featureKey = featureKey,
            mockValue = 0L,
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val suspendProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuide)
        )

        val syncProvider = SynchronousFeatureConfigProvider(suspendProvider)

        // When
        val result = syncProvider.getLong(featureKey)

        // Then
        assertEquals(expectedValue, result)
    }

    @Test
    fun `verify synchronous getDouble returns correct value`() {
        // Given
        val featureKey = "sync_double_test"
        val expectedValue = 3.14159

        val featureTestGuide = FeatureTestGuide(
            featureKey = featureKey,
            mockValue = 0.0,
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val suspendProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuide)
        )

        val syncProvider = SynchronousFeatureConfigProvider(suspendProvider)

        // When
        val result = syncProvider.getDouble(featureKey)

        // Then
        assertEquals(expectedValue, result)
    }

    @Test
    fun `verify custom coroutine context is used`() {
        // Given
        val featureKey = "custom_context_test"
        val expectedValue = true
        val customContext = Dispatchers.Default

        val featureTestGuide = FeatureTestGuide(
            featureKey = featureKey,
            mockValue = false,
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val suspendProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuide)
        )

        val syncProvider = SynchronousFeatureConfigProvider(
            suspendProvider = suspendProvider
        )

        // When
        val result = syncProvider.getBoolean(featureKey)

        // Then
        assertTrue(result)
    }

    @Test
    fun `verify custom timeout is respected`() {
        // Given
        val featureKey = "timeout_test"
        val expectedValue = "timeout test"
        val shortTimeout = 100.milliseconds

        val featureTestGuide = FeatureTestGuide(
            featureKey = featureKey,
            mockValue = "",
            remoteValue = expectedValue,
            givenSource = FeatureTestGuide.Source.Remote
        )

        val suspendProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuide)
        )

        val syncProvider = SynchronousFeatureConfigProvider(
            suspendProvider = suspendProvider,
            timeout = shortTimeout
        )

        // When
        val result = syncProvider.getString(featureKey)

        // Then
        assertEquals(expectedValue, result)
    }

    @Test
    fun `verify mocked config is used when available through sync API`() {
        // Given
        val featureKey = "sync_mock_test"
        val expectedValue = true

        // Setup mock storage
        runTest {
            memoryMockConfigStorage.save(featureKey, expectedValue)
        }

        val featureTestGuide = FeatureTestGuide(
            featureKey = featureKey,
            mockValue = false,
            remoteValue = false,
            givenSource = FeatureTestGuide.Source.Mock
        )

        val suspendProvider: FeatureConfigProvider = FeatureConfigProviderImpl(
            mockConfigProvider,
            provideMemoryConfigFactory(featureTestGuide)
        )

        val syncProvider = SynchronousFeatureConfigProvider(suspendProvider)

        // When
        val result = syncProvider.getBoolean(featureKey)

        // Then
        assertTrue(result)
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