package io.devtools.config.exceptions

/**
 * Signals that there is no registered provider which can feature config for the given owner.
 * Check your MockConfigProviderFactory implementation, it might be missing the provider for the owner.
 *
 * @author  Maxim Petlyuk
 * @since   1.0
 */
class IllegalConfigOwnerException(message: String) : RuntimeException(message)
