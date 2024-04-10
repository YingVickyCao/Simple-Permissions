package com.hades.utility.permission;

/**
 * Show a rationale to user.
 * If the system determines that your app should show a rationale, however, present the rationale to the user in a UI element.
 * In this rationale, clearly explain what data your app is trying to access and what benefits the app can provide to the user if they grant the runtime permission.
 * After the user acknowledges the rationale, continue to the next step.
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
