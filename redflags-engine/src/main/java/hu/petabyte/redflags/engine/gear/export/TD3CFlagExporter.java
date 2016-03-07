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
package hu.petabyte.redflags.engine.gear.export;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import hu.petabyte.redflags.engine.gear.indicator.AbstractIndicator;
import hu.petabyte.redflags.engine.gear.indicator.AbstractTD3CIndicator;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.Type;

/**
 * @author Zsolt Jurányi
 */
@Component("H1FlagExporter")
@Scope("prototype")
public class TD3CFlagExporter extends FlagExporter {

	@Override
	protected String filename() {
		return super.filename().replace(".", "-H1.");
	}

	@Override
	protected boolean shouldInclude(AbstractIndicator i) {
		return i instanceof AbstractTD3CIndicator;
	}

	@Override
	protected boolean shouldInclude(Notice notice) {
		Type td = notice.getData().getDocumentType();
		return (null != td && td.getId().matches("TD-[3C]"));
	}

}
