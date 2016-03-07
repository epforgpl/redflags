/*
   Copyright 2014-2016 PetaByte Research Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package hu.petabyte.redflags.engine.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Utility methods for operating with beans, mapping properties.
 *
 * @author Zsolt Jurányi
 *
 */
public class MappingUtils {

	private static final Logger LOG = LoggerFactory.getLogger(MappingUtils.class);

	public static synchronized Object getDeepPropertyIfExists(BeanWrapper wrapper, String property) {
		if (wrapper.isReadableProperty(property)) {
			return wrapper.getPropertyValue(property);
		} else {
			return null;
		}
	}

	public static Object getDeepPropertyIfExists(Object rootObject, String property) {
		return getDeepPropertyIfExists(new BeanWrapperImpl(rootObject), property);
	}

	public static List<String> listAllProperties(Class<?> clazz, String rootName) {
		List<String> p = new ArrayList<String>();
		for (Field f : clazz.getDeclaredFields()) {
			String n = f.getName();
			Class<?> c = f.getType();
			if (null != rootName && !rootName.isEmpty()) {
				n = String.format("%s.%s", rootName, n);
			}
			p.add(n);
			if (!c.getName().startsWith("java.")) {
				p.addAll(listAllProperties(c, n));
			}
		}
		return p;
	}

	public static void normalizeMapKeys(Map<String, String> map) {
		Set<String> keys = new LinkedHashSet<String>(map.keySet());
		for (String key : keys) {
			String value = map.get(key);
			map.remove(key);
			key = key.replaceAll("0", ".");
			map.put(key, value);
		}
	}

	public static synchronized boolean setDeepProperties(BeanWrapper wrapper, Map<String, String> properties) {
		boolean r = true;
		for (String property : properties.keySet()) {
			String value = properties.get(property);
			r = r & setDeepProperty(wrapper, property, value);
		}
		return r;
	}

	public static boolean setDeepProperties(Object rootObject, Map<String, String> properties) {
		BeanWrapper wrapper = new BeanWrapperImpl(rootObject);
		return setDeepProperties(wrapper, properties);
	}

	/**
	 * Sets the value of a bean's property, using the given wrapper. The
	 * property can be a deeper one, e.g. "a.b.c.d". If any of a, b or c
	 * properties is null, the method will call an empty constructor for its
	 * static type. If any error occurs, setDeepProperty will write it to the
	 * LOG and returns false.
	 *
	 * @param wrapper
	 *            Wrapper of the root object.
	 * @param property
	 *            The property needs to be set.
	 * @param value
	 *            The new value of the property.
	 * @return True if property setting was successful, false on error.
	 */
	public static synchronized boolean setDeepProperty(BeanWrapper wrapper, String property, Object value) {
		try {
			// this will help calling a constructor:
			ExpressionParser parser = new SpelExpressionParser();

			// go thru property path elements:
			int offset = 0;
			while ((offset = property.indexOf(".", offset + 1)) > -1) {
				String currentProperty = property.substring(0, offset);

				// if current property is null:
				if (null == wrapper.getPropertyValue(currentProperty)) {
					String className = wrapper.getPropertyType(currentProperty).getName();

					// build up a constructor call:
					Expression exp = parser.parseExpression(String.format("new %s()", className));

					// LIMITATIONS:
					// 1) uses static type
					// 2) needs defined empty constructor

					// run it:
					Object newObject = exp.getValue();

					// and set the property:
					wrapper.setPropertyValue(currentProperty, newObject);
				}
			}

			// finally, set the destination property:
			wrapper.setPropertyValue(property, value);
			return true;
		} catch (Exception ex) {
			LOG.error("Could not set property '{}'", property);
			LOG.debug("Exception: ", ex);
			return false;
		}
	}

	/**
	 * Sets the value of a bean's property. The property can be a deeper one,
	 * e.g. "a.b.c.d". If any of a, b or c properties is null, the method will
	 * call an empty constructor for its static type.
	 *
	 * @param rootObject
	 *            The root object which has the given property.
	 * @param property
	 *            The property needs to be set.
	 * @param value
	 *            The new value of the property.
	 * @throws Exception
	 *             When something fails, mostly when there is no empty
	 *             constructor defined in one of the properties class.
	 */
	public static boolean setDeepProperty(Object rootObject, String property, Object value) {

		// this will help accessing fields by name:
		BeanWrapper wrapper = new BeanWrapperImpl(rootObject);

		// do the magic:
		return setDeepProperty(wrapper, property, value);
	}

}
