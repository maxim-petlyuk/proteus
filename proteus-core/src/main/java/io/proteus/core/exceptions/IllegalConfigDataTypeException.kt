package io.proteus.core.exceptions

/**
 * Signals that MockConfigRepository does not supporting saving & reading config values
 * of the specified type.
 *
 * @author  Maxim Petlyuk
 * @since   1.0
 */
class IllegalConfigDataTypeException(message: String) : RuntimeException(message)
