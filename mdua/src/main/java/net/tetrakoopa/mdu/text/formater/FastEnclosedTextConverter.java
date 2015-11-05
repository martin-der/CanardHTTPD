package net.tetrakoopa.mdu.text.formater;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;

public class FastEnclosedTextConverter<CONTEXT> implements EnclosedTextConverter<CONTEXT> {

	private final static char MUSTACHE_IN[] = "{{".toCharArray();
	private final static char MUSTACHE_OUT[] = "}}".toCharArray();
	private final static char EXTRA_MUSTACHE_IN = '{';
	private final static char EXTRA_MUSTACHE_OUT = '}';
//
//
//	public static class StackElement {
//		enum Type {
//			COLLECTION_LOOP, CONDITIONNAL_VIEW;
//		};
//
//		public final Type type;
//
//		public final Collection<?> collection;
//
//		public StackElement() {
//			this.type = Type.CONDITIONNAL_VIEW;
//			this.collection = null;
//		}
//
//		public StackElement(Collection<?> collection) {
//			this.type = Type.CONDITIONNAL_VIEW;
//			this.collection = collection;
//		}
//
//	}
//
//
	public void process(Reader source, Writer destination, CONTEXT context, ConverterTools<CONTEXT> tools) throws IOException {

	/*		final Vector<StackElement> stackElements = new Vector<StackElement>();

		final char buffer[] =  new char[50];

		boolean insideMustache = false;
		int l;
		StringBuffer key = new StringBuffer();
		while ((l = source.read(buffer)) > 0) {
			int position = 0;
			do {
				if (insideMustache) {
					int p = findInStartingFrom(MUSTACHE_OUT, buffer, position, l);
					if (p < 0) {
						key.append(buffer, position, l);
						position = l;
						break;
					} else {
						key.append(buffer, position, p);
						position = p + MUSTACHE_OUT.length;
						applyTool(key.toString(), source, destination, context, tools, stackElements);
						insideMustache = false;
					}
				} else {
					int p = findInStartingFrom(MUSTACHE_IN, buffer, position, l);
					if (p<0) {
						destination.write(buffer, position, l);
						position = l;
						break;
					} else {
						position = p + MUSTACHE_IN.length;
						position = p;
						insideMustache = true;
					}
				}
			} while (position < l);
			key.setLength(0);
		}*/
	}
//
//	private void applyTool(String key, Reader source, Writer destination, CONTEXT context, ConverterTools<CONTEXT> tools, Vector<StackElement> stackElements) throws IOException {
//
//		key = key.trim();
//		if ("".equals(key)) {
//			tools.convert(context, "", destination, 0);
//			return;
//		}
//
//		// count how many extra mustaches are enclosing the text
//
//		final int l = key.length();
//		final int ld2 = l / 2;
//		int extraMustaches = 0;
//		while (extraMustaches < ld2) {
//			if (key.charAt(extraMustaches) == EXTRA_MUSTACHE_IN && key.charAt(l - 1 - extraMustaches) == EXTRA_MUSTACHE_OUT) {
//				extraMustaches++;
//			} else {
//				break;
//			}
//		}
//		if (extraMustaches > 0) {
//			key = key.substring(extraMustaches, l - extraMustaches);
//		}
//
//		// dispatch the real key to dedicated handler
//
//		if (key.charAt(0) == EVAL) {
//			key = key.substring(1);
//			key = key.trim();
//			handleEval(key, source, destination, context, tools, stackElements, extraMustaches);
//			return;
//		}
//		if (key.startsWith("/")) {
//			key = key.substring(1);
//			key = key.trim();
//			handleEndTag(key, destination, context, tools, stackElements);
//			return;
//		}
//
//		tools.convert(context, key, destination, extraMustaches);
//	}
//
//	private void handleEndTag(String key, Writer destination, CONTEXT context, ConverterTools<CONTEXT> tools, Vector<StackElement> stackElements) {
//
//	}
//
//	private void handleEval(String key, Reader source, Writer destination, CONTEXT context, ConverterTools<CONTEXT> tools, Vector<StackElement> stackElements, int extraMustaches) throws IOException {
//
//		final boolean extraEval;
//
//		if ( key.charAt(0)==EVAL ) {
//			extraEval = true;
//			key = key.substring(1);
//			key = key.trim();
//		} else {
//			extraEval = true;
//		}
//
//		final Object comment;
//
//		try {
//			comment = tools.comment(context, key);
//		} catch (Exception ex) {
//			// FixMe notify user
//			return;
//		}
//		if (comment == null) {
//			return;
//		}
//		if (comment instanceof Collection) {
//			handleCollection((Collection<?>) comment, destination, context, tools, stackElements, extraMustaches);
//			return;
//		}
//		if (comment instanceof Boolean) {
//			if (extraEval) {
//				tools.convert(context, key, destination, extraMustaches);
//			} else {
//				handleConditionnalView((Boolean) comment, key, source, destination, context, tools, stackElements, extraMustaches);
//			}
//			return;
//		}
//	}
//
//	private void handleConditionnalView(Boolean showView, String showViewName, Reader source, Writer destination, CONTEXT context, ConverterTools<CONTEXT> tools, Vector<StackElement> stackElements, int extraMustaches) throws IOException {
//		if (Boolean.TRUE.equals(showView)) {
//			process(source, destination, context, tools);
//			if (!showViewName.equals(null)) {
//
//			}
//		} else {
//			Reader input = null;
//			absorb(input);
//		}
//
//	}
//
//	private void absorb(Reader input) {
//		// TODO
//	}
//
//	private void handleCollection(Collection<?> collection, Writer destination, CONTEXT context, ConverterTools<CONTEXT> tools, Vector<StackElement> stackElements, int extraMustaches) {
//		stackElements.add(new StackElement());
//
//	}
//
	/**
	 * @param pattern
	 *            <b>must be an array with at least one char</b>
	 */
	private static int findInStartingFrom(char pattern[], char buffer[], int position, final int endIndex) {

		final int pl = pattern.length;

		while (position + pl <= endIndex) {
			int p = Arrays.binarySearch(buffer, position, endIndex, pattern[0]);

			if (p < 0) {
				// Did not even find pattern first character
				return -1;
			}

			// got the fisrt

			position++;
			int pp = 1;

			while (pp < pl) {
				if (pattern[pp] != buffer[position]) {
					break;
				}
				position++;
				pp++;
			}

			if (pp >= pl) {
				return position;
			}

			return position;

		}

		return -1;
	}
}
