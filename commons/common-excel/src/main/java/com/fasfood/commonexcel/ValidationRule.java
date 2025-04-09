package com.fasfood.commonexcel;

/**
 * Interface defining a validation rule for a cell
 */
public interface ValidationRule<T> {
    boolean isValid(T value);

    String getErrorMessage();
}