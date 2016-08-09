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
package hu.petabyte.redflags.api.controller;

import hu.petabyte.redflags.api.util.ApiHelper;
import hu.petabyte.redflags.api.util.ErrorResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Péter Szűcs
 */
@RestController
public class ErrorHandlingController implements ErrorController {
	 
   private static final String PATH = "/error";
   
   @Autowired
   private ErrorAttributes errorAttributes;

   @Autowired
   private ApiHelper apiHelper;
   
   @RequestMapping(value = PATH)
   public Map<String, Object> error(HttpServletRequest request, HttpServletResponse response, Locale locale) {
	   Map<String, Object> attributes = getErrorAttributes(request, true);
	   
	   List<ErrorResponse> errors = new ArrayList<ErrorResponse>();
	   errors.add(new ErrorResponse(null, (String) attributes.get("error"), (String) attributes.get("message"), (String) attributes.get("status").toString(), (String) attributes.get("path"), false));
	   return apiHelper.getErrorObject(errors, locale);
   }

   @Override
   public String getErrorPath() {
       return PATH;
   }
   
   private Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
       RequestAttributes requestAttributes = new ServletRequestAttributes(request);
       return errorAttributes.getErrorAttributes(requestAttributes, includeStackTrace);
   }
}
