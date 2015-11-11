package net.tetrakoopa.mdu.android.view.mapping.copy;

import java.lang.reflect.Field;

import net.tetrakoopa.mdu.android.view.mapping.exception.BadValueException;

public class FloatMappingCopyProcessor extends PrimitiveMappingCopyProcessor<Float> {

	@Override
	public <VO> void setIntoVOPrimitive(VO vo, Field field, Float value) throws IllegalArgumentException, IllegalAccessException, BadValueException {
		if (field.getType().isPrimitive())
			field.setFloat(vo, value);
		else
			setIntoVORaw(vo, field, value);
	}

	@Override
	public <VO> Float getFromVOPrimitive(VO vo, Field field) throws IllegalArgumentException, IllegalAccessException, BadValueException {
		if (field.getType().isPrimitive())
			return field.getFloat(vo);
		else
			return getFromVORaw(vo, field);
	}
}
