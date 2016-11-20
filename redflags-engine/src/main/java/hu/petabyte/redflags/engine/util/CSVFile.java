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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Easy-to-use CSV file writer which can be used besides <code>CSV2MySQL</code>.
 * It converts the basic types to an understandable format for MySQL.
 *
 * @author Zsolt Jurányi
 * @see CSV2MySQL
 *
 */
public class CSVFile {

	private final String filename;
	private final String columnDelimiter;
	private final String rowDelimiter;
	private final Writer w;

	/**
	 * Creates a new <code>CSVFile</code> instance and opens an output stream
	 * writer, which then should be closed manually.
	 *
	 * @param filename
	 *            The filename.
	 * @param columnDelimiter
	 *            Column delimiter to be used in the CSV file, e.g. "\t"
	 * @param rowDelimiter
	 *            Row delimiter to be used in the CSV file, e.g. "\n"
	 * @throws IOException
	 */
	public CSVFile(String filename, String columnDelimiter, String rowDelimiter) throws IOException {
		this.filename = checkNotNull(filename, "filename should not be null.");
		checkArgument(!filename.isEmpty(), "filename should not be empty.");
		this.columnDelimiter = checkNotNull(columnDelimiter, "columnDelimiter should not be null.");
		checkArgument(!columnDelimiter.isEmpty(), "columnDelimiter should not be empty.");
		this.rowDelimiter = checkNotNull(rowDelimiter, "rowDelimiter should not be null.");
		checkArgument(!rowDelimiter.isEmpty(), "rowDelimiter should not be empty.");

		this.w = new OutputStreamWriter(new FileOutputStream(filename), "UTF-8");
	}

	/**
	 * Closes the output stream writer.
	 *
	 * @throws Exception
	 */
	public void close() throws Exception {
		w.close();
	}

	/**
	 * Deletes the file. Be careful not to call this before <code>close()</code>
	 */
	public void deleteFile() {
		new File(filename).delete();
	}

	/**
	 * Appends the row delimiter to the file.
	 *
	 * @return This <code>CSVFile</code> instance for chaining.
	 * @throws IOException
	 */
	public CSVFile endLine() throws IOException {
		w.write(rowDelimiter);
		return this;
	}

	/**
	 * Retrieves the column delimiter to be used in the CSV file.
	 *
	 * @return The column delimiter to be used in the CSV file.
	 */
	public String getColumnDelimiter() {
		return columnDelimiter;
	}

	/**
	 * Retrieves the filename.
	 *
	 * @return The filename.
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Retrieves the row delimiter to be used in the CSV file.
	 *
	 * @return The row delimiter to be used in the CSV file.
	 */
	public String getRowDelimiter() {
		return rowDelimiter;
	}

	/**
	 * Appends the <code>String</code> representation of the given object to the
	 * file. <code>null</code> will be converted to <code>"\\N"</code>,
	 * <code>false</code> to <code>"0"</code>, <code>true</code> to
	 * <code>"1"</code> and dates to <code>"yyyy-MM-dd"</code> format. On any
	 * other object, the <code>toString()</code> method will be called.
	 *
	 * @param o
	 * @return This <code>CSVFile</code> instance for chaining.
	 * @throws IOException
	 */
	public CSVFile writeCell(Object o) throws IOException {
		if (null == o) {
			w.write("\\N");
		} else if (o instanceof Boolean) {
			w.write(o == Boolean.FALSE ? "0" : "1");
		} else if (o instanceof Date) {
			w.write(new SimpleDateFormat("yyyy-MM-dd").format((Date) o));
		} else if (o instanceof Double) {
			w.write(Double.toString((Double) o));
		} else if (o instanceof Integer) {
			w.write(Integer.toString((Integer) o));
		} else if (o instanceof Long) {
			w.write(Long.toString((Long) o));
		} else {
			w.write(o.toString().replace("\\", "\\\\"));
		}
		w.write(columnDelimiter);
		return this;
	}

}
