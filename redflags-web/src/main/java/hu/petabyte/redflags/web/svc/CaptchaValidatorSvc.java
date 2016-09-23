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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Zsolt Jurányi
 *
 */
@Service
public class CaptchaValidatorSvc implements CaptchaValidator {

	@Override
	public boolean validateCaptcha(String response) {
		String url = "https://www.google.com/recaptcha/api/siteverify";
		String secret = "...";
		try {
			// send to Google
			HttpsURLConnection con = (HttpsURLConnection) new URL(url)
					.openConnection();
			String urlParameters = String.format("secret=%s&response=%s",
					secret, response);
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			// read response
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer s = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				s.append(inputLine);
			}
			in.close();

			// parse response
			Map<String, Object> map = new ObjectMapper().readValue(
					s.toString(), new TypeReference<HashMap<String, Object>>() {
					});
			return Boolean.TRUE.equals(map.get("success"));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
