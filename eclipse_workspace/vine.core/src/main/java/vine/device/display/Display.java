package vine.device.display;

/**
 * Abstracts the monitor devices connected to the system. Only one monitor is
 * active at a time, that is the monitor , that will be used to contain the
 * application window.
 * 
 * @author Steffen
 *
 */
public interface Display
{

    /**
     * @return The system name of the currently active monitor.
     */
    String getMonitorName();

    /**
     * 
     * @return The primary monitor of the system.
     */
    long getPrimaryMonitor();

    /**
     * @return An array with all to the system connected monitors names.
     */
    String[] getMonitorNames();

    /**
     * @return The screen width of the currently used monitor.
     */
    int getWidth();

    /**
     * @return The screen height of the currently used monitor.
     */
    int getHeight();

    /**
     * @return The refresh rate of the currently used monitor.
     */
    int getRefreshRate();

    /**
     * @return The bits used to display red color.
     */
    int getRedBits();

    /**
     * @return The bits used to display blue color.
     */
    int getBlueBits();

    /**
     * @return The bits used to display green color.
     */
    int getGreenBits();

}