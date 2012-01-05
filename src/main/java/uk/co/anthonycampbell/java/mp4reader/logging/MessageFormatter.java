package uk.co.anthonycampbell.java.mp4reader.logging;

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

import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Message formatter for use with {@linkplain java.util.logging} framework.
 * 
 * @author Anthony Campbell - anthonycampbell.co.uk
 */
public class MessageFormatter extends Formatter {

	// Format
	private static final MessageFormat messageFormat = new MessageFormat(
			"[{2}|{3,date,h:mm:ss}]: {4} \n");

	@Override
	public String format(final LogRecord record) {
		final Object[] arguments = new Object[5];
		arguments[0] = record.getLoggerName();
		arguments[1] = record.getLevel();
		arguments[2] = Thread.currentThread().getName();
		arguments[3] = new Date(record.getMillis());
		arguments[4] = record.getMessage();
		return messageFormat.format(arguments);
	}
}
