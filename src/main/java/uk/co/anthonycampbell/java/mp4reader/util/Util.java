package uk.co.anthonycampbell.java.mp4reader.util;

/**
 * Copyright 2011 Anthony Campbell (anthonycampbell.co.uk)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.co.anthonycampbell.java.mp4reader.box.common.Box;

/**
 * Utility class to assist with text formatting.
 *
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class Util {
	
	/** Default print list separator. */
	public static final String PRINT_LIST_SEPARATOR = ", ";
	
	/**
	 * Utility method to print all of the fields of the provided box instance.
	 * 
	 * @param boxInstance - box instance.
	 * @return list of fields.
	 */
	public static <T> String printFields(final Object instance) {
		return printFields(instance, instance.getClass());
	}

	/**
	 * Utility method to print all of the fields of the provided box instance.
	 * 
	 * @param instance - box instance.
	 * @param boxClass - box class.
	 * @return list of fields.
	 */
	public static <T> String printFields(final Object instance, final Class<T> boxClass) {
		// Initialise result
		final StringBuilder builder = new StringBuilder();
		
		// Validate
		if (instance != null && boxClass != null) {
			final Field[] fields = boxClass.getDeclaredFields();
			final Method[] methods = boxClass.getDeclaredMethods();
			
			for (final Field field : fields) {
				final String fieldName = field.getName();
				
				if (StringUtils.isNotEmpty(fieldName)) {
					final String fieldMethod = ("get" + fieldName).toLowerCase();
					
					for (final Method method : methods) {
						final String methodName = method.getName();
						final Class<?> returnType = method.getReturnType();
						boolean boxReturnType = false;
						
						// Determine whether this method returns a Box class
						if (returnType != null) {						
							final Class<?>[] returnTypeInterfaces = returnType.getInterfaces();
							
							for (final Class<?> interfaceClass : returnTypeInterfaces) {
								if (interfaceClass != null && interfaceClass == Box.class) {
									boxReturnType = true;
									break;
								}
							}
						}
	
						// If method is a getter for box property lets print
						if (StringUtils.isNotEmpty(methodName)) {
							if (fieldMethod.equals(methodName.toLowerCase())) {
								try {
									builder.append(field.getName());
									builder.append("=");		
									builder.append((boxReturnType) ? "[" : "");
									
									final String fieldValue = "" + method.invoke(instance, new Object[0]);
									if (StringUtils.isNotEmpty(fieldValue) && fieldValue.endsWith(" ")) {
										builder.append(fieldValue.substring(0, fieldValue.length() - 1));
									} else {
										builder.append(fieldValue);	
									}
									
									builder.append((boxReturnType) ? "]" : "");
									builder.append(" ");
									
								} catch (Exception ex) { }
							}
						}
					}
				}
			}
		} else {
			throw new IllegalArgumentException("Provided class details are invalid! " +
					"(instance=" + instance + ", boxClass=" + boxClass + ")");
		}
		
		return builder.toString();
	}
	
	/**
	 * Convert the provided big integer time stamp to a date instance.
	 * Time stamps in the MP4 specification resolve to the number of
	 * seconds since 1904.
	 * 
	 * @param bigInteger - big integer instance to convert.
	 * @return date instance.
	 */
	public static Date generateDate(final BigInteger bigInteger) {
		// Validate
		if (bigInteger != null) {
			final Calendar calendar = Calendar.getInstance();
			calendar.set(1904, Calendar.JANUARY, 1, 0, 0, 0);
			calendar.setTimeInMillis(calendar.getTimeInMillis() + (bigInteger.longValue() * 1000));			
			return calendar.getTime();
			
		} else {
			throw new IllegalArgumentException("Unable to generate date, provided argument is invalid! " +
					"(bigInteger=" + bigInteger + ")");
		}
	}
	
	/**
	 * Utility method to parse iTunes meta XML.
	 * 
	 * @param xml - the XML to parse.
	 */
	public static Map<String, List<String>> parseITunesMeta(final String xml) {
		// Initialise result
		final Map<String, List<String>> iTunesMap = new HashMap<>();
		
		// Validate
		if (StringUtils.isNotEmpty(xml)) {
			try {
				final InputSource inputSource = new InputSource();
				inputSource.setCharacterStream(new StringReader(xml));
			
				final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();			
				final DocumentBuilder builder = factory.newDocumentBuilder();
				final Document document = builder.parse(inputSource);
				
				final NodeList plists = document.getElementsByTagName("plist");
				for (int i = 0; i < plists.getLength(); ++i) {
					final Element plist = (Element) plists.item(i);
					
					if (plist != null && plist.hasChildNodes()) {						
						final NodeList nodes = plist.getChildNodes();
						
						if (nodes != null) {
							for (int j = 0; j < nodes.getLength(); ++j) {
								final Node node = nodes.item(j);
								
								if (node != null && node.getNodeType() == Node.ELEMENT_NODE &&
										"dict".equals(node.getNodeName())) {
									iTunesMap.putAll(parseDictionary(node));
								}
							}
						}
					}
				}
			}
			catch (ParserConfigurationException pce) { }
			catch (SAXException saxe) { }
			catch (IOException ioe) { }
			
		} else {
			throw new IllegalArgumentException("Provided XML is invalid! (xml=" + xml + ")");
		}
		
		return iTunesMap;
	}
	
	/**
	 * Utility method to parse a 'dict' node of the iTunes meta pList. Updates the
	 * provided iTunes meta data map.
	 * 
	 * @param dictionary - XML 'dict' element.
	 * @return Updated iTunes meta data map.
	 */
	public static Map<String, List<String>> parseDictionary(final Node dictionary) {
		// Initialise result
		final Map<String, List<String>> iTunesMap = new HashMap<>();
		
		// Validate
		if (dictionary != null && dictionary.getNodeType() == Node.ELEMENT_NODE &&
				"dict".equals(dictionary.getNodeName())) {
			final NodeList children = dictionary.getChildNodes();
			
			if (children != null) {
				String key = null;
				List<String> value = null;
				
				for (int i = 0; i < children.getLength(); ++i) {
					final Node node = children.item(i);
					
					if (node != null && node.getNodeType() == Node.ELEMENT_NODE) {
						switch (node.getNodeName()) {
							case "key":
								key = node.getFirstChild().getNodeValue();
								break;
								
							case "string":
								value = new ArrayList<>();
								value.add(node.getFirstChild().getNodeValue());
								break;
								
							case "array":
								value = parseArray(node);
								break;
						}
					}
					
					// When ready add new entry
					if (key != null && value != null) {
						iTunesMap.put(key, value);
						key = null;
						value = null;
					}
				}
			}
		} else {
			throw new IllegalArgumentException("Provided node is not a dictionary element! (dictionary=" +
					dictionary + ")");
		}
		
		return iTunesMap;
	}
	
	/**
	 * Utility method to parse a 'array' node of the iTunes meta pList.
	 * 
	 * @param dictionary - XML 'array' element.
	 * @return List representing the array element.
	 */
	public static List<String> parseArray(final Node array) {
		// Initialise result
		final List<String> arrayList = new ArrayList<>();
		
		// Validate
		if (array != null && array.getNodeType() == Node.ELEMENT_NODE &&
				"array".equals(array.getNodeName())) {
			final NodeList children = array.getChildNodes();
			
			if (children != null) {
				for (int i = 0; i < children.getLength(); ++i) {
					final Node node = children.item(i);
					
					if (node != null && node.getNodeType() == Node.ELEMENT_NODE &&
							"dict".equals(node.getNodeName())) {
						final Map<String, List<String>> dictionary = parseDictionary(node);
						
						if (dictionary != null && !dictionary.isEmpty()) {
							for (final List<String> valueList : dictionary.values()) {
								if (valueList != null) {
									arrayList.addAll(valueList);			
								}
							}
						}
					}
				}
			}
		} else {
			throw new IllegalArgumentException("Provided node is not an array element! (array=" +
					array + ")");
		}
		
		return arrayList;
	}
	
	/**
	 * Utility method to print the provided list of strings using the default
	 * string separator.
	 * 
	 * @param list - the list to print.
	 * @return Comma delimited list of string values.
	 */
	public static String printList(final List<String> list) {
		return printList(list, PRINT_LIST_SEPARATOR);
	}
	
	/**
	 * Utility method to print the provided list of strings.
	 * 
	 * @param list - the list to print.
	 * @param separator - string separator.
	 * @return Comma delimited list of string values.
	 */
	public static String printList(final List<String> list, final String separator) {
		// Initialise result
		final StringBuilder builder = new StringBuilder();
		
		// Validate
		if (list != null && separator != null && !list.isEmpty()) {
			boolean first = true;
			
			for (final String element : list) {
				if (StringUtils.isNotEmpty(element)) {
					if (first) {
						first = false;
					} else {
						builder.append(separator);
					}
					
					builder.append(element);
				}
			}
		} else {
			throw new IllegalArgumentException("Provided list is invalid! (list=" + list +
					", separator=" + separator + ")");
		}
		
		return builder.toString();
	}
	
	/**
	 * Helper method to convert the provided byte array into its numerical
	 * 16-bit unsigned value.
	 * 
	 * @param data - byte array to convert.
	 * @return 16-bit unsigned value.
	 */
	public static int convertToUint16(final byte[] data) {
		// Initialise result
		int result = 0;

		// Validate
		if (data != null && !(data.length > 2)) {
			for (int i = 0; i < 2; ++i) {
				result = (result << 8) + (data[i] & 0xFF);
			}	
		} else {
			throw new IllegalArgumentException("Provided byte array is invalid! (data=" + data + ")");
		}
		
		return result;
	}
}
