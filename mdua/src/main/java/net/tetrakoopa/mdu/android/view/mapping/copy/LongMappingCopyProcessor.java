package net.tetrakoopa.mdu.android.view.mapping.copy;

import java.lang.reflect.Field;

import net.tetrakoopa.mdu.android.view.mapping.exception.BadValueException;

public class LongMappingCopyProcessor extends JavaPrimitiveMappingCopyProcessor<Long> {

	@Override
	public <VO> void setIntoVOPrimitive(VO vo, Field field, Long value) throws IllegalArgumentException, IllegalAccessException, BadValueException {
		if (field.getType().isPrimitive())
			field.setLong(vo, value);
		else
			setIntoVORaw(vo, field, value);
	}

	@Override
	public <VO> Long getFromVOPrimitive(VO vo, Field field) throws IllegalArgumentException, IllegalAccessException, BadValueException {
		if (field.getType().isPrimitive())
			return field.getLong(vo);
		else
			return getFromVORaw(vo, field);
	}
}
