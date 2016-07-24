/*
 * Copyright LWJGL. All rights reserved.
 * License terms: http://lwjgl.org/license.php
 */
package org.lwjgl.glfw;

import org.lwjgl.system.Callback;
import org.lwjgl.system.Checks;

import static org.lwjgl.system.Checks.*;
import static org.lwjgl.system.JNI.*;
import static org.lwjgl.system.MemoryUtil.*;

/** Utility class for GLFW callbacks. */
public final class Callbacks {

	private Callbacks() {}

	/**
	 * Resets all callbacks for the specified GLFW window to {@code NULL} and {@link Callback#free frees} all previously set callbacks.
	 *
	 * @param window the GLFW window
	 */
	public static void glfwFreeCallbacks(long window) {
		if ( Checks.CHECKS )
			checkPointer(window);

		for ( long callback : new long[] {
			GLFW.Functions.SetWindowPosCallback,
			GLFW.Functions.SetWindowSizeCallback,
			GLFW.Functions.SetWindowCloseCallback,
			GLFW.Functions.SetWindowRefreshCallback,
			GLFW.Functions.SetWindowFocusCallback,
			GLFW.Functions.SetWindowIconifyCallback,
			GLFW.Functions.SetFramebufferSizeCallback,
			GLFW.Functions.SetKeyCallback,
			GLFW.Functions.SetCharCallback,
			GLFW.Functions.SetCharModsCallback,
			GLFW.Functions.SetMouseButtonCallback,
			GLFW.Functions.SetCursorPosCallback,
			GLFW.Functions.SetCursorEnterCallback,
			GLFW.Functions.SetScrollCallback,
			GLFW.Functions.SetDropCallback
		} ) {
			long prevCB = invokePPP(callback, window, NULL);
			if ( prevCB != NULL )
				Callback.free(prevCB);
		}
	}

}