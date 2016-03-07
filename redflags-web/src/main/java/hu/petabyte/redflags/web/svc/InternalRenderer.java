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
package hu.petabyte.redflags.web.svc;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 * @author Zsolt Jurányi
 */
@Service
public class InternalRenderer {
	// big thanks to:
	// http://stackoverflow.com/questions/18361566/execute-jsp-in-spring-controller-and-get-response-html

	private static class CustomHttpServletResponse extends
	HttpServletResponseWrapper {

		private ServletOutputStream outputStream;
		private PrintWriter printWriter;

		public CustomHttpServletResponse(HttpServletResponse response)
				throws UnsupportedEncodingException {
			super(response);

			this.outputStream = new CustomServletOutPutStream();
			this.printWriter = new PrintWriter(new OutputStreamWriter(
					outputStream, ENCODE));

		}

		@Override
		public ServletOutputStream getOutputStream() {
			return this.outputStream;
		}

		@Override
		public PrintWriter getWriter() throws IOException {
			return this.printWriter;
		}
	}

	private static class CustomServletOutPutStream extends ServletOutputStream {

		private StringBuilder stringBuilder = new StringBuilder();

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setWriteListener(WriteListener arg0) {
		}

		@Override
		public String toString() {
			return stringBuilder.toString();
		}

		@Override
		public void write(byte b[], int off, int len) throws IOException {
			stringBuilder.append(new String(b, off, len, ENCODE));
		}

		@Override
		public void write(int b) {
			stringBuilder.append(b);
		}
	}

	private final static String ENCODE = "UTF-8";

	private @Autowired ViewResolver viewResolver;

	public String evalView(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> model,
			Locale locale, String viewName) throws Exception {
		CustomHttpServletResponse customResponse = new CustomHttpServletResponse(
				response);
		View view = viewResolver.resolveViewName(viewName, locale);
		if (view != null) {
			view.render(model, request, customResponse);
			OutputStream os = customResponse.getOutputStream();
			return os.toString();
		}
		throw new Exception("no view found");
	}
}
