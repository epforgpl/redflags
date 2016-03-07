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
package hu.petabyte.redflags.web.ctrl;

import hu.petabyte.redflags.web.svc.FilterSvc;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Zsolt Jurányi
 */
@Controller
public class FilterCtrl {

	private @Autowired FilterSvc filter;

	@RequestMapping("/filter/delete/{id}")
	public String delete(@PathVariable long id) {
		filter.delete(id);
		return "redirect:/filters";
	}

	@RequestMapping(value = "/filter/{id}", method = RequestMethod.GET)
	public String editForm(Map<String, Object> m, @PathVariable long id) {
		m.put("filter", filter.get(id));
		return "filter";
	}

	@RequestMapping(value = "/filter/{id}", method = RequestMethod.POST)
	public String saveModifications(
			@PathVariable long id,
			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "subscribe", required = false) Boolean subscribe) {
		filter.saveSettings(id, name, Boolean.TRUE.equals(subscribe));
		return "redirect:/filters";
	}
}
