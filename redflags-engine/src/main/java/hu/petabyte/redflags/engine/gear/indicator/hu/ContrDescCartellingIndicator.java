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
package hu.petabyte.redflags.engine.gear.indicator.hu;

import hu.petabyte.redflags.engine.gear.indicator.AbstractTD3CIndicator;
import hu.petabyte.redflags.engine.model.CPV;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "contrDescCartellingIndicator")
public class ContrDescCartellingIndicator extends AbstractTD3CIndicator {

	private static final Logger LOG = LoggerFactory
			.getLogger(ContrDescCartellingIndicator.class);

	private final List<String> keywords = new ArrayList<String>();
	private String filename = "indicator-data/description-cartel.txt";

	@Override
	public void beforeSession() throws Exception {
		keywords.clear();
		Scanner s = new Scanner(
				new ClassPathResource(filename).getInputStream(), "UTF-8");
		while (s.hasNextLine()) {
			keywords.add(s.nextLine());
		}
		s.close();
		LOG.debug("Loaded {} expressions for cartelling", keywords.size());
		setWeight(0.5);
		super.beforeSession();
	}

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		StringBuilder sb = new StringBuilder();
		if (null != notice.getData().getCpvCodes()) {
			for (CPV cpv : notice.getData().getCpvCodes()) {
				sb.append(cpv.getId());
				sb.append("\n");
				sb.append(cpv.getName());
				sb.append("\n");
			}
		}
		if (null != notice.getData().getOriginalCpvCodes()) {
			for (CPV cpv : notice.getData().getCpvCodes()) {
				sb.append(cpv.getId());
				sb.append("\n");
				sb.append(cpv.getName());
				sb.append("\n");
			}
		}
		for (ObjOfTheContract obj : notice.getObjs()) {
			sb.append(obj.getShortDescription());
			sb.append("\n");
		}

		String input = sb.toString().toLowerCase();
		sb.setLength(0);
		for (String kw : keywords) {
			if (input.contains(kw.toLowerCase())) {
				return returnFlag("info", "expr=" + kw);
			}
		}

		return null;
	}

	public String getFilename() {
		return filename;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
