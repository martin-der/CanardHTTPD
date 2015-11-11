package net.tetrakoopa.mdu.android.view.mapping.copy;

import java.lang.reflect.Field;

import net.tetrakoopa.mdu.android.view.mapping.exception.BadValueException;

public abstract class PrimitiveMappingCopyProcessor<TYPE> extends ObjectMappingCopyProcessor<TYPE> {

	@Override
	protected final <VO> void setIntoVOUnchecked(VO vo, Field field, TYPE value) throws IllegalArgumentException, IllegalAccessException, BadValueException {
		if (value == null) {
			throw new BadValueException("Cannot insert null value into a primitive type");
		}
		setIntoVOPrimitive(vo, field, value);
	}

	protected abstract <VO> void setIntoVOPrimitive(VO vo, Field field, TYPE value) throws IllegalArgumentException, IllegalAccessException, BadValueException;

	protected abstract <VO> TYPE getFromVOPrimitive(VO vo, Field field) throws IllegalArgumentException, IllegalAccessException, BadValueException;
}
