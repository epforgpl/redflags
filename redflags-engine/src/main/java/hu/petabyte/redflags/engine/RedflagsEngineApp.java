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
package hu.petabyte.redflags.engine;

import hu.petabyte.redflags.engine.boot.RedflagsEngineBoot;

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

/**
 * Start class of the Redflags Engine. Its main method starts the app inside
 * Spring Framework.
 *
 * @author Zsolt Jurányi
 *
 */
@SpringBootApplication
@EnableConfigurationProperties
public class RedflagsEngineApp {

	// TODO indicators - value pattern 2 helper class
	// TODO Parser cleanup: general->params, hun->gears
	// TODO redo parser logging
	// TODO ??? private -> protected, except log
	// TODO JAVADOC EVERYTHING. (gear adding hint: @Component("name")
	// TODO short manual to add gears
	// TODO UNIT TEST EVERYTHING.

	private static final Logger LOG = LoggerFactory
			.getLogger(RedflagsEngineApp.class);

	private static void exportConfig(String fn) {
		try {
			StreamUtils.copyToString(
					new ClassPathResource("application.yml").getInputStream(),
					Charset.forName("UTF-8"));
			System.out.println("Internal application.yml copied out to " + fn);
		} catch (IOException e) {
			System.out.println("Operation failed: " + e.getMessage());
		}
	}

	/**
	 * Main method of the Redflags Engine. Start the app with "--help" argument
	 * to see information about arguments. This method handles some basic modes,
	 * but if none of them is called, it launches the real thing in Spring
	 * Framework.
	 *
	 * @param args
	 *            Command line arguments.
	 * @see RedflagsEngineBoot
	 */
	public static void main(String[] args) {
		if (args.length > 0) {
			if ("--help".equalsIgnoreCase(args[0])) {
				printArgsHelp();
				return;
			} else if ("--get-config".equalsIgnoreCase(args[0])) {
				if (args.length >= 2) {
					String fn = args[1];
					exportConfig(fn);
				} else {
					printArgsHelp();
				}
				return;
			}
			// TODO --generate-template noticeid lang outfile
		}

		LOG.info("*** REDFLAGS ENGINE - Initializing framework");
		SpringApplication.run(RedflagsEngineApp.class, args);
	}

	public static void printArgsHelp() {
		try {
			System.out
					.println(StreamUtils.copyToString(new ClassPathResource(
							"args-help.txt").getInputStream(), Charset
							.forName("UTF-8")));
		} catch (IOException e) {
		}
	}
}
