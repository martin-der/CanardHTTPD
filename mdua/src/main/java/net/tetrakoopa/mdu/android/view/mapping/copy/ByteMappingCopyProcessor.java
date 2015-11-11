package net.tetrakoopa.mdu.android.view.mapping.copy;

import java.lang.reflect.Field;

import net.tetrakoopa.mdu.android.view.mapping.exception.BadValueException;

public class ByteMappingCopyProcessor extends PrimitiveMappingCopyProcessor<Byte> {

	@Override
	public <VO> void setIntoVOPrimitive(VO vo, Field field, Byte value) throws IllegalArgumentException, IllegalAccessException, BadValueException {
		if (field.getType().isPrimitive())
			field.setByte(vo, value);
		else
			setIntoVORaw(vo, field, value);
	}

	@Override
	public <VO> Byte getFromVOPrimitive(VO vo, Field field) throws IllegalArgumentException, IllegalAccessException, BadValueException {
		if (field.getType().isPrimitive())
			return field.getByte(vo);
		else
			return getFromVORaw(vo, field);
	}
}
