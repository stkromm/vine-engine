package vine.util.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import vine.util.Log;

/**
 * @author Steffen
 *
 * @param <T>
 *            The type of the managed class.
 */
public class VineClass<T>
{
    /**
     * The class that is wrapped by this object.
     */
    private final Class<T> type;

    /**
     * @param type
     *            The class, that should be wrapped with a VineClass object.
     */
    public VineClass(final Class<T> type)
    {
        this.type = type;
    }

    /**
     * @param methodName
     *            The name of the searched method
     * @return optional wrapper, that contains the method, if the class contains
     *         the a method with the given name.
     */
    public final Optional<Method> getMethod(final String methodName)
    {
        return Arrays.stream(this.type.getMethods()).filter(method -> method.getName().equals(methodName)).findFirst();
    }

    /**
     * @param params
     *            The parameters that the class constructor needs.
     * @return The constructor, if a constructor is defined, that takes the
     *         parameters.
     */
    public final Constructor<T> getConstructor(final Object... params)
    {
        final int size = params == null ? 0 : params.length;
        final Class<?>[] types = new Class<?>[size];
        if (params != null)
        {
            for (int i = size - 1; i >= 0; i--)
            {
                types[i] = params[i].getClass();
            }
        }
        try
        {
            return this.type.getConstructor(types);
        } catch (final NoSuchMethodException e)
        {
            Log.exception("", e);
        } catch (final SecurityException e)
        {
            Log.exception(
                    "You made the constructor private. You have to use a public constructor to instantiate a gameobject.",
                    e);
        }
        return null;
    }

    /**
     * @return A object of the class T
     */
    public final T instantiate()
    {
        try
        {
            return this.type.newInstance();
        } catch (final InstantiationException e)
        {
            Log.exception("Could not instantiate gameobject of class:" + this.type.getName()
                    + "\nMaybe you passed a class, that is abstract and or does not inherit Gameobject", e);
        } catch (final IllegalAccessException e)
        {
            Log.exception("Could not instantiate gameobject of class:" + this.type.getName()
                    + "\n Perhaps you made the constructor private?\n", e);
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
    public final boolean hasMethodImplemented(final String methodName, final Class<?>... params)
    {
        try
        {
            final Method method = this.type.getMethod(methodName, params);
            return method.getDeclaringClass().equals(this.type) && Arrays.equals(method.getParameterTypes(), params);
        } catch (NoSuchMethodException | SecurityException e)
        {
            Log.exception("Auto-generated catch block", e);
        }
        return false;
    }
}
