package com.hades.utility.permission;

/**
 * Rationale UI listener
 */
public interface OnContextUIListener {
    /**
     * Called when agree to continue request permission on Rationale UI
     */
    void ok();

    /**
     * Called when not agree continue request permission on Rationale UI
     */
    void cancel();
}
