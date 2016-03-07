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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

/**
 * Some handy utility methods for file I/O.
 *
 * @author Zsolt Jurányi
 *
 */
public class IOUtils {

	public static String loadUTF8File(File file) throws IOException {
		return loadUTF8Stream(new FileInputStream(file));
	}

	public static String loadUTF8Resource(String name) throws IOException {
		return loadUTF8Stream(IOUtils.class.getClassLoader().getResourceAsStream(name));
	}

	public static String loadUTF8Stream(InputStream stream) throws IOException {
		if (null == stream) {
			throw new FileNotFoundException();
		}
		StringBuffer buf = new StringBuffer();
		Scanner scanner = new Scanner(stream, "UTF8");
		while (scanner.hasNextLine()) {
			buf.append(scanner.nextLine());
			buf.append("\n");
		}
		scanner.close();
		return buf.toString();
	}

	public static File resourceAsFile(String name) throws URISyntaxException {
		URL u = IOUtils.class.getClassLoader().getResource(name);
		return new File(u.toURI());
	}

	public static InputStream resourceAsStream(String name) {
		return IOUtils.class.getClassLoader().getResourceAsStream(name);
	}

	public static void saveUTF8File(File file, String content) throws IOException {
		Writer out = new OutputStreamWriter(new FileOutputStream(file), "UTF8");
		out.write(content);
		out.close();
	}

}
