package laconiclizard.laconicclientlib;

public interface Enableable {

    /**
     * Whether this is enabled.
     *
     * @return whether this is enabled
     */
    boolean isEnabled();

    /** Enable this, assuming it is disabled. */
    void enableStrict();

    /** Disable this, assuming it is enabled. */
    void disableStrict();

    /**
     * Ensure this is enabled.
     *
     * @return whether this changed as a result of this call
     */
    default boolean enable() {
        if (!isEnabled()) {
            enableStrict();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Ensure this is disabled.
     *
     * @return whether this changed as a result of this call
     */
    default boolean disable() {
        if (isEnabled()) {
            disableStrict();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Set whether this is enabled.
     *
     * @param enabled whether this should be enabled
     * @return whether this changed as a result of this call
     */
    default boolean setEnabled(boolean enabled) {
        boolean isEnabled = isEnabled();
        if (enabled && !isEnabled) {
            enableStrict();
            return true;
        } else if (!enabled && isEnabled) {
            disableStrict();
            return true;
        } else {
            return false;
        }
    }

    /** Toggle whether this is enabled. */
    default void toggle() {
        if (isEnabled()) {
            disableStrict();
        } else {
            enableStrict();
        }
    }

}
