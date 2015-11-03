package net.tetrakoopa.mdu.android.view.mapping;

import java.lang.reflect.Field;

import net.tetrakoopa.mdu.android.util.ReflexionUtil;
import net.tetrakoopa.mdu.android.view.mapping.annotation.UIElement;
import net.tetrakoopa.mdu.android.view.mapping.copy.BooleanMappingCopyProcessor;
import net.tetrakoopa.mdu.android.view.mapping.copy.ByteMappingCopyProcessor;
import net.tetrakoopa.mdu.android.view.mapping.copy.CharMappingCopyProcessor;
import net.tetrakoopa.mdu.android.view.mapping.copy.DoubleMappingCopyProcessor;
import net.tetrakoopa.mdu.android.view.mapping.copy.FloatMappingCopyProcessor;
import net.tetrakoopa.mdu.android.view.mapping.copy.IntegerMappingCopyProcessor;
import net.tetrakoopa.mdu.android.view.mapping.copy.LongMappingCopyProcessor;
import net.tetrakoopa.mdu.android.view.mapping.copy.ObjectMappingCopyProcessor;
import net.tetrakoopa.mdu.android.view.mapping.copy.ShortMappingCopyProcessor;
import net.tetrakoopa.mdu.android.view.mapping.exception.BadValueException;
import net.tetrakoopa.mdu.android.view.mapping.exception.MappingException;
import net.tetrakoopa.mdu.android.view.mapping.exception.accessor.IllegalVOMappingException;
import net.tetrakoopa.mdu.android.view.mapping.exception.accessor.IllegalViewMappingException;
import net.tetrakoopa.mdu.android.view.mapping.exception.accessor.NoSuchElementMappingException;

public class ViewAnnotationsCopyProcessor<VIEW, VIEW_ELEMENT> extends AbstractScanner {

	private final ViewAnnotationsViewAccessor<VIEW, VIEW_ELEMENT> viewAccessor;

	private final ObjectMappingCopyProcessor<Object> objectCC = new ObjectMappingCopyProcessor<Object>();

	private final BooleanMappingCopyProcessor booleanCC = new BooleanMappingCopyProcessor();
	private final ByteMappingCopyProcessor byteCC = new ByteMappingCopyProcessor();
	private final CharMappingCopyProcessor charCC = new CharMappingCopyProcessor();
	private final DoubleMappingCopyProcessor doubleCC = new DoubleMappingCopyProcessor();
	private final FloatMappingCopyProcessor floatCC = new FloatMappingCopyProcessor();
	private final IntegerMappingCopyProcessor integerCC = new IntegerMappingCopyProcessor();
	private final LongMappingCopyProcessor longCC = new LongMappingCopyProcessor();
	private final ShortMappingCopyProcessor shortCC = new ShortMappingCopyProcessor();

	public ViewAnnotationsCopyProcessor(ViewAnnotationsViewAccessor<VIEW, VIEW_ELEMENT> viewAccessor) {
		this.viewAccessor = viewAccessor;
	}

	
	public <VO> void assignViewToVO(VIEW view, VO vo) {
		@SuppressWarnings("unchecked")
		Class<VO> voClass = (Class<VO>) vo.getClass();
		try {
			assignViewToVO ( view, vo, voClass );
		} catch (MappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected <VO> void assignViewToVO(VIEW view, VO vo, Class<VO> voClass) throws IllegalVOMappingException, BadValueException, NoSuchElementMappingException, IllegalViewMappingException {
		
		for (Field field : getViewMappedFields(voClass)) {
			UIElement uiElement = field.getAnnotation(UIElement.class);
			
			if (uiElement ==null)
				continue;
			
			VIEW_ELEMENT element = viewAccessor.findElement(view, uiElement);
			
			assignElementViewToVO (uiElement, vo, element, field);
			
		}
	}
	
	private <VO> void assignElementViewToVO(UIElement uiElement, VO vo, VIEW_ELEMENT element, Field field) throws IllegalVOMappingException, BadValueException, IllegalViewMappingException {
		Class<?> type = ReflexionUtil.inboxIfPrimitive(field.getType());
		
		if (type.equals(java.lang.String.class)) {

			String value = viewAccessor.getViewElementValue(field, element, java.lang.String.class);
			objectCC.setIntoVO(vo, field, value);
			
		} else if (type.equals(java.lang.Boolean.class)) {

			Boolean value = viewAccessor.getViewElementValue(field, element, java.lang.Boolean.class);
			booleanCC.setIntoVO(vo, field, value);

		} else if (type.equals(java.lang.Byte.class)) {

			Byte value = viewAccessor.getViewElementValue(field, element, java.lang.Byte.class);
			byteCC.setIntoVO(vo, field, value);

		} else if (type.equals(java.lang.Character.class)) {

			Character value = viewAccessor.getViewElementValue(field, element, java.lang.Character.class);
			charCC.setIntoVO(vo, field, value);

		} else if (type.equals(java.lang.Double.class)) {

			Double value = viewAccessor.getViewElementValue(field, element, java.lang.Double.class);
			doubleCC.setIntoVO(vo, field, value);

		} else if (type.equals(java.lang.Float.class)) {

			Float value = viewAccessor.getViewElementValue(field, element, java.lang.Float.class);
			floatCC.setIntoVO(vo, field, value);

		} else if (type.equals(java.lang.Integer.class)) {

			Integer value = viewAccessor.getViewElementValue(field, element, java.lang.Integer.class);
			integerCC.setIntoVO(vo, field, value);

		} else if (type.equals(java.lang.Long.class)) {

			Long value = viewAccessor.getViewElementValue(field, element, java.lang.Long.class);
			longCC.setIntoVO(vo, field, value);
				
		} else if (type.equals(java.lang.Short.class)) {

			Short value = viewAccessor.getViewElementValue(field, element, java.lang.Short.class);
			shortCC.setIntoVO(vo, field, value);

		} else {
			
			throw new IllegalVOMappingException("Don't know how handle type " + type);
		}
		
	}
	
	private <VO> void assignElementVOToView(UIElement uiElement, VIEW_ELEMENT element, VO vo, Field field) throws IllegalVOMappingException, BadValueException, IllegalViewMappingException {
		Class<?> type = ReflexionUtil.inboxIfPrimitive(field.getType());

		if (type.equals(java.lang.String.class)) {

			String value = viewAccessor.getViewElementValue(field, element, java.lang.String.class);
			objectCC.getFromVO(vo, field);

		} else if (type.equals(java.lang.Boolean.class)) {

			Boolean value = viewAccessor.getViewElementValue(field, element, java.lang.Boolean.class);
			booleanCC.setIntoVO(vo, field, value);

		} else if (type.equals(java.lang.Byte.class)) {

			Byte value = viewAccessor.getViewElementValue(field, element, java.lang.Byte.class);
			byteCC.setIntoVO(vo, field, value);

		} else if (type.equals(java.lang.Character.class)) {

			Character value = viewAccessor.getViewElementValue(field, element, java.lang.Character.class);
			charCC.setIntoVO(vo, field, value);

		} else if (type.equals(java.lang.Double.class)) {

			Double value = viewAccessor.getViewElementValue(field, element, java.lang.Double.class);
			doubleCC.setIntoVO(vo, field, value);

		} else if (type.equals(java.lang.Float.class)) {

			Float value = viewAccessor.getViewElementValue(field, element, java.lang.Float.class);
			floatCC.setIntoVO(vo, field, value);

		} else if (type.equals(java.lang.Integer.class)) {

			Integer value = viewAccessor.getViewElementValue(field, element, java.lang.Integer.class);
			integerCC.setIntoVO(vo, field, value);

		} else if (type.equals(java.lang.Long.class)) {

			Long value = viewAccessor.getViewElementValue(field, element, java.lang.Long.class);
			longCC.setIntoVO(vo, field, value);

		} else if (type.equals(java.lang.Short.class)) {

			Short value = viewAccessor.getViewElementValue(field, element, java.lang.Short.class);
			shortCC.setIntoVO(vo, field, value);

		} else {

			throw new IllegalVOMappingException("Don't know how handle type " + type);
		}

	}


}
