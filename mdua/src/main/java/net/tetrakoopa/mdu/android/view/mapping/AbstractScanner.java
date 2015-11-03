package net.tetrakoopa.mdu.android.view.mapping;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.List;

import net.tetrakoopa.mdu.android.view.mapping.annotation.UIElement;

public class AbstractScanner {
	
	public static class FieldList extends AbstractList<Field> {
		
		private final Field fields[];
		
		public FieldList(Field fields[]) {

			int count;

			count = 0;
			for (Field field : fields) {
				if (field.getAnnotation(UIElement.class) != null)
					count++;
			}

			this.fields = new Field[count];

			count = 0;
			for (Field field : fields) {
				if (field.getAnnotation(UIElement.class) != null) {
					this.fields[count] = field;
					count++;
				}
			}
		}

		@Override
		public Field get(int index) {
			return fields[index];
		}

		@Override
		public int size() {
			return fields.length;
		}
		
	}

	public static <VO> List<Field> getViewMappedFields(Class<VO> voClass) {
		return new FieldList(voClass.getDeclaredFields());
	}

}
