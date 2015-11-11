package net.tetrakoopa.mdu.android.view.mapping.copy;

import java.lang.reflect.Field;

import net.tetrakoopa.mdu.android.view.mapping.exception.BadValueException;

public class IntegerMappingCopyProcessor extends PrimitiveMappingCopyProcessor<Integer> {

	@Override
	public <VO> void setIntoVOPrimitive(VO vo, Field field, Integer value) throws IllegalArgumentException, IllegalAccessException, BadValueException {
		if (field.getType().isPrimitive())
			field.setInt(vo, value);
		else
			setIntoVORaw(vo, field, value);
	}

	@Override
	public <VO> Integer getFromVOPrimitive(VO vo, Field field) throws IllegalArgumentException, IllegalAccessException, BadValueException {
		if (field.getType().isPrimitive())
			return field.getInt(vo);
		else
			return getFromVORaw(vo, field);
	}
}
