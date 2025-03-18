package io.proteus.core.exceptions

/**
 * Signals that a mocked config is not available for the specific feature.
 * In other words, mocked config is not set up for the feature or the overridden
 * mocked value is not match to the expected feature data type.
 *
 * @author  Maxim Petlyuk
 * @since   1.0
 */
class MockConfigUnavailableException : Exception {

    constructor() : super("Mock config is not available for the specific feature")

    constructor(message: String) : super(message)
}
