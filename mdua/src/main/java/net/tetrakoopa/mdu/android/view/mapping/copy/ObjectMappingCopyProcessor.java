package net.tetrakoopa.mdu.android.view.mapping.copy;

import java.lang.reflect.Field;

import net.tetrakoopa.mdu.android.view.mapping.exception.BadValueException;
import net.tetrakoopa.mdu.android.view.mapping.exception.SecurityMappingException;
import net.tetrakoopa.mdu.android.view.mapping.exception.accessor.IllegalVOMappingException;

public class ObjectMappingCopyProcessor<TYPE> {

	public final <VO> void setIntoVO(VO vo, Field field, TYPE value) throws IllegalVOMappingException, BadValueException {
		try {
			setIntoVOUnchecked(vo, field, value);
		} catch (IllegalArgumentException e) {
			throw new IllegalVOMappingException(e);
		} catch (IllegalAccessException e) {
			field.setAccessible(true);
			try {
				setIntoVOUnchecked(vo, field, value);
			} catch (IllegalArgumentException e1) {
				throw new IllegalVOMappingException(e);
			} catch (IllegalAccessException e1) {
				throw new SecurityMappingException(e);
			}
		}
	}

	public final <VO> TYPE getFromVO(VO vo, Field field) throws IllegalVOMappingException, BadValueException {
		try {
			return getFromVOUnchecked(vo, field);
		} catch (IllegalArgumentException e) {
			throw new IllegalVOMappingException(e);
		} catch (IllegalAccessException e) {
			field.setAccessible(true);
			try {
				return getFromVOUnchecked(vo, field);
			} catch (IllegalArgumentException e1) {
				throw new IllegalVOMappingException(e);
			} catch (IllegalAccessException e1) {
				throw new SecurityMappingException(e);
			}
		}
	}

	protected <VO> void setIntoVOUnchecked(VO vo, Field field, TYPE value) throws IllegalArgumentException, IllegalAccessException, BadValueException {
		setIntoVORaw(vo, field, value);
	}

	protected <VO> TYPE getFromVOUnchecked(VO vo, Field field) throws IllegalArgumentException, IllegalAccessException, BadValueException {
		return getFromVORaw(vo, field);
	}

	@SuppressWarnings("unchecked")
	protected final <VO> TYPE getFromVORaw(VO vo, Field field) throws IllegalArgumentException, IllegalAccessException, BadValueException, IllegalAccessException {
		return (TYPE) field.get(vo);
	}
	protected final <VO> void setIntoVORaw(VO vo, Field field, TYPE value) throws IllegalArgumentException, IllegalAccessException, BadValueException, IllegalAccessException {
		field.set(vo, value);
	}

}
