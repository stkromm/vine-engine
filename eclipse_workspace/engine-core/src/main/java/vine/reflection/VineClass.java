package vine.reflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Steffen
 *
 * @param <T>
 *            The type of the managed class.
 */
public class VineClass<T> {
    /**
     * Class logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(VineClass.class);
    /**
     * The class that is wrapped by this object.
     */
    private final Class<T> type;

    /**
     * @param type
     *            The class, that should be wrapped with a VineClass object.
     */
    public VineClass(final Class<T> type) {
        this.type = type;
    }

    /**
     * @param methodName
     *            The name of the searched method
     * @return optional wrapper, that contains the method, if the class contains
     *         the a method with the given name.
     */
    public final Optional<Method> getMethodByName(final String methodName) {
        return Arrays.stream(type.getMethods()).filter(method -> method.getName().equals(methodName)).findFirst();
    }

    /**
     * @return A object of the class T
     */
    public final T instantiateType() {
        try {
            return type.newInstance();
        } catch (InstantiationException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Could not instantiate gameobject of class:" + type.getName()
                        + "\nMaybe you passed a class, that is abstract and or does not inherit Gameobject", e);
            }
        } catch (IllegalAccessException e) {
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Could not instantiate gameobject of class:" + type.getName()
                        + "\n Perhaps you made the constructor private?\n", e);
            }
        }
        return null;
    }

    /**
     * @param methodName
     *            The name of the method, that is looked for.
     * @param params
     *            The parameters, the method requires
     * @return true, if the class implements the given method with parameters of
     *         the given type and order.
     */
    public final boolean hasMethodImplemented(final String methodName, final Class<?>... params) {
        try {
            final Method method = type.getMethod(methodName, params);
            return method.getDeclaringClass().equals(type) && Arrays.equals(method.getParameterTypes(), params);
        } catch (NoSuchMethodException | SecurityException e) {
            LOGGER.error("Auto-generated catch block", e);
        }
        return false;
    }
}