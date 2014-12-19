/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rxweb.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import rxweb.http.MediaType;
import rxweb.http.Method;
import rxweb.server.ServerRequestHeaders;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
// TODO: Let's try Mapping instead RequestMapping -> shorter
public @interface Mapping {

	String name() default "";

	String[] value() default {};

	/**
	 * @see Method
	 */
	String[] method() default {};

	String[] params() default {};

	/**
	 * @see ServerRequestHeaders
	 */
	String[] headers() default {};

	/**
	 * @see MediaType
	 */
	String[] consumes() default {};

	/**
	 * @see MediaType
	 */
	String[] produces() default {};

}
