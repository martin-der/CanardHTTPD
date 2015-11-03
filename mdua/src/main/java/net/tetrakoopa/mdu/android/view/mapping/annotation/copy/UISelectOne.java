package net.tetrakoopa.mdu.android.view.mapping.annotation.copy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UISelectOne {
	
	String prompt() default "";
	
	String labelAttribut() default "";
	
	String orderAttribut() default "";
}
