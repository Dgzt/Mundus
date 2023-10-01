package com.mbrlabs.mundus.commons.utils;

/**
 * An interface for managing Level of Detail (LoD) for a object.
 * @author JamesTKhan
 * @version September 30, 2023
 */
public interface LoDManager {

    /**
     * Updates the LoD.
     */
    void update(float delta);

    /**
     * Marks the LoD as dirty, indicating a
     */
    void markDirty();

    /**
     * Enables LoD.
     */
    void enable();

    /**
     * Disables LoD. Implementations should set the LoD to the lowest (base) level.
     * when disabled.
     */
    void disable();
}
