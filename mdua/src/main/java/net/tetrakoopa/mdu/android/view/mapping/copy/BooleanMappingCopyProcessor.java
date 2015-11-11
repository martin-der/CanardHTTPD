package net.tetrakoopa.mdu.android.view.mapping.copy;

import java.lang.reflect.Field;

import net.tetrakoopa.mdu.android.view.mapping.exception.BadValueException;

public class BooleanMappingCopyProcessor extends PrimitiveMappingCopyProcessor<Boolean> {

	@Override
	public <VO> void setIntoVOPrimitive(VO vo, Field field, Boolean value) throws IllegalArgumentException, IllegalAccessException, BadValueException {
		if (field.getType().isPrimitive())
			field.setBoolean(vo, value);
		else
			setIntoVORaw(vo, field, value);
	}

	@Override
	public <VO> Boolean getFromVOPrimitive(VO vo, Field field) throws IllegalArgumentException, IllegalAccessException, BadValueException {
		if (field.getType().isPrimitive())
			return field.getBoolean(vo);
		else
			return getFromVORaw(vo, field);
	}

}
