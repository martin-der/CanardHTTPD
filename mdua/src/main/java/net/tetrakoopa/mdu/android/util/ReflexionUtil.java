package net.tetrakoopa.mdu.android.util;


public class ReflexionUtil {

	public static Class<?> inboxIfPrimitive(Class<?> clazz) {
		if (!clazz.isPrimitive())
			return clazz;

		if (clazz.equals(Boolean.TYPE))
			return Boolean.class;

		if (clazz.equals(Byte.TYPE))
			return Byte.class;
		if (clazz.equals(Character.TYPE))
			return Character.class;

		if (clazz.equals(Float.TYPE))
			return Float.class;
		if (clazz.equals(Double.TYPE))
			return Double.class;

		if (clazz.equals(Short.TYPE))
			return Short.class;
		if (clazz.equals(Integer.TYPE))
			return Integer.class;
		if (clazz.equals(Long.TYPE))
			return Long.class;

		throw new IllegalStateException("Found unhandled primitive type : " + clazz.getName());
	}

}
