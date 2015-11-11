package net.tetrakoopa.mdu.android.view.mapping.copy;

import java.lang.reflect.Field;

import net.tetrakoopa.mdu.android.view.mapping.exception.BadValueException;

public class DoubleMappingCopyProcessor extends PrimitiveMappingCopyProcessor<Double> {

	@Override
	public <VO> void setIntoVOPrimitive(VO vo, Field field, Double value) throws IllegalArgumentException, IllegalAccessException, BadValueException {
		if (field.getType().isPrimitive())
			field.setDouble(vo, value);
		else
			setIntoVORaw(vo, field, value);
	}

	@Override
	public <VO> Double getFromVOPrimitive(VO vo, Field field) throws IllegalArgumentException, IllegalAccessException, BadValueException {
		if (field.getType().isPrimitive())
			return field.getDouble(vo);
		else
			return getFromVORaw(vo, field);
	}
}
