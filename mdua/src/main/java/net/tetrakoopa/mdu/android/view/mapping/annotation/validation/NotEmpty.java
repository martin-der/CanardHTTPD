package net.tetrakoopa.mdu.android.view.mapping.annotation.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
/** 
 * <ul>
 * <li>check java.lang.Number : different from 0</li> 
 * <li>check java.lang.String : different from ""</li>
 * </ul> 
 */
public @interface NotEmpty {


}
