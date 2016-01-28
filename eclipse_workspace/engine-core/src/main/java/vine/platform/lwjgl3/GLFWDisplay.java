package vine.platform.lwjgl3;

import static org.lwjgl.glfw.GLFW.GLFW_DISCONNECTED;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwGetMonitorName;
import static org.lwjgl.glfw.GLFW.glfwGetMonitors;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetMonitorCallback;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWMonitorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import vine.display.Display;

/**
 * Implements a Display with the GLFW API. Doesn't work, if there is no GLFW
 * window existing.
 * 
 * @author Steffen
 *
 */
public final class GLFWDisplay implements Display {
    // Get the resolution of the primary monitor
    private GLFWVidMode vidmode;
    private long currentMonitor = glfwGetPrimaryMonitor();
    private GLFWMonitorCallback monitorCallback = GLFWMonitorCallback.create((l, e) -> changeMonitor(l, e));

    /**
     * Default display constructor, uses the system primary monitor.
     */
    public GLFWDisplay() {
        this(glfwGetPrimaryMonitor());
    }

    /**
     * @param preferredMonitor
     *            The monitor, used to contain windows.
     */
    public GLFWDisplay(long preferredMonitor) {
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (glfwInit() != GLFW_TRUE) {
            throw new IllegalStateException("Failed to init glfw");
        }
        glfwSetMonitorCallback(monitorCallback);
        GLFWVidMode prefVidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (prefVidMode != null) {
            currentMonitor = preferredMonitor;
            vidmode = prefVidMode;
        } else {
            currentMonitor = glfwGetPrimaryMonitor();
            vidmode = prefVidMode;

        }
    }

    private void changeMonitor(long monitor, int event) {
        if (monitor == currentMonitor && event == GLFW_DISCONNECTED) {
            vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        }
    }

    @Override
    public String[] getMonitorNames() {
        PointerBuffer monitors = glfwGetMonitors();
        String[] monitorNames = new String[monitors.capacity()];
        for (int i = 0; i < monitors.capacity(); i++) {
            monitorNames[i] = glfwGetMonitorName(monitors.get(i));
        }
        return monitorNames;
    }

    @Override
    public int getWidth() {
        return vidmode.width();
    }

    @Override
    public int getRedBits() {
        return vidmode.redBits();
    }

    @Override
    public int getHeight() {
        return vidmode.height();
    }

    @Override
    public int getRefreshRate() {
        return vidmode.refreshRate();
    }

    @Override
    public int getBlueBits() {
        return vidmode.blueBits();
    }

    @Override
    public int getGreenBits() {
        return vidmode.greenBits();
    }

    @Override
    public String getMonitorName() {
        return glfwGetMonitorName(glfwGetPrimaryMonitor());
    }

    @Override
    public long getPrimaryMonitor() {
        return glfwGetPrimaryMonitor();
    }
}