package net.tetrakoopa.mdua.util;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

public class Map2StringProperties {

	public static <KEY, E extends Enum<E>> E getEnumeration(Class<E> enumClass, KEY key, Map<KEY, String> map, E defaultz) {
		return getEnumeration(enumClass, null, key, map, defaultz);
	}

	public static <KEY, E extends Enum<E>> E getEnumeration(Class<E> enumClass, KEY key, Map<KEY, String> map) {
		return getEnumeration(enumClass, null, key, map);
	}

	public static <KEY, E extends Enum<E>> E getEnumeration(Class<E> enumClass, String attributName, KEY key, Map<KEY, String> map, E defaultz) {
		E value = getEnumeration(enumClass, attributName, key, map);
		return value == null ? defaultz : value;
	}

	public static <KEY, E extends Enum<E>> E getEnumeration(Class<E> enumClass, String attributName, KEY key, Map<KEY, String> map) {

		if (!map.containsKey(key))
			return null;
		String stringValue = map.get(key);
		if (stringValue == null) {
			return null;
		}

		if (attributName != null) {
			Field field;
			try {
				field = enumClass.getDeclaredField(attributName);
			} catch (NoSuchFieldException nsfex) {
				throw new IllegalArgumentException("No such field '" + attributName + "' in " + enumClass.getName(), nsfex);
			}
			if (!field.getType().equals(key.getClass())) {
				throw new IllegalArgumentException("Field '" + attributName + "' in " + enumClass.getName() + " is not of type " + key.getClass());
			}
			field.setAccessible(true);
			for (E enuum : enumClass.getEnumConstants()) {
				final Object attribut;
				try {
					attribut = field.get(enuum);
				} catch (IllegalArgumentException ex) {
					throw new IllegalStateException("Could not access Enum(" + enumClass.getName() + ") field '" + attributName + "'", ex);
				} catch (IllegalAccessException ex) {
					throw new IllegalStateException("Could not access Enum(" + enumClass.getName() + ") field '" + attributName + "'", ex);
				}
				if (stringValue.equals(attribut))
					return enuum;
			}
		} else {
			for (E e : enumClass.getEnumConstants()) {
				if (e.name().equals(stringValue))
					return e;
			}
		}

		return null;
	}


	public static <KEY> boolean getBoolean(KEY key, Map<KEY, String> map, boolean defaultz) {
		Boolean value = getBoolean(key, map);
		return value == null ? defaultz : value;
	}

	public static <KEY> Boolean getBoolean(KEY key, Map<KEY, String> map) {
		if (!map.containsKey(key))
			return null;
		String stringValue = map.get(key);
		if (stringValue == null) {
			return null;
		}
		if (stringValue.toLowerCase(Locale.US).equals("true"))
			return true;
		if (stringValue.toLowerCase(Locale.US).equals("false"))
			return false;
		return null;
	}

	public static <KEY> int getInteger(KEY key, Map<KEY, String> map, int defaultz) {
		Integer value = getInteger(key, map);
		return value == null ? defaultz : value;
	}

	public static <KEY> Integer getInteger(KEY key, Map<KEY, String> map) {
		if (!map.containsKey(key))
			return null;
		String stringValue = map.get(key);
		if (stringValue == null) {
			return null;
		}
		try {
			return Integer.parseInt(stringValue);
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	public static <KEY> long getLong(KEY key, Map<KEY, String> map, long defaultz) {
		Long value = getLong(key, map);
		return value == null ? defaultz : value;
	}

	public static <KEY> Long getLong(KEY key, Map<KEY, String> map) {
		if (!map.containsKey(key))
			return null;
		String stringValue = map.get(key);
		if (stringValue == null) {
			return null;
		}
		try {
			return Long.parseLong(stringValue);
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	public static <KEY> String getString(KEY key, Map<KEY, String> map, String defaultz) {
		String value = getString(key, map);
		return value == null ? defaultz : value;
	}

	public static <KEY> String getString(KEY key, Map<KEY, String> map) {
		if (!map.containsKey(key))
			return null;
		return map.get(key);
	}
}
