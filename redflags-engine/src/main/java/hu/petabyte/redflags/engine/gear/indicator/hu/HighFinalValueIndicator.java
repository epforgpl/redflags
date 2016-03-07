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

import hu.petabyte.redflags.engine.gear.indicator.AbstractTD7Indicator;
import hu.petabyte.redflags.engine.gear.parser.MetadataParser;
import hu.petabyte.redflags.engine.gear.parser.TemplateBasedDocumentParser;
import hu.petabyte.redflags.engine.gear.parser.hu.EstimatedValueParser;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.Notice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Zsolt Jurányi
 */
@Component
@ConfigurationProperties(prefix = "highFinalValueIndicator")
public class HighFinalValueIndicator extends AbstractTD7Indicator {

	private static final Logger LOG = LoggerFactory
			.getLogger(HighFinalValueIndicator.class);

	private long maxValueInWorks = 1500 * 1000000;
	private long maxValueInSupply = 1000 * 1000000;
	private long maxValueInService = 1000 * 1000000;

	private @Autowired TemplateBasedDocumentParser docParser;
	private @Autowired EstimatedValueParser estimParser;
	private @Autowired MetadataParser metadataParser;

	public long getMaxValueInWorks() {
		return maxValueInWorks;
	}

	public void setMaxValueInWorks(long maxValueInWorks) {
		this.maxValueInWorks = maxValueInWorks;
	}

	public long getMaxValueInSupply() {
		return maxValueInSupply;
	}

	public void setMaxValueInSupply(long maxValueInSupply) {
		this.maxValueInSupply = maxValueInSupply;
	}

	public long getMaxValueInService() {
		return maxValueInService;
	}

	public void setMaxValueInService(long maxValueInService) {
		this.maxValueInService = maxValueInService;
	}

	@Override
	protected IndicatorResult flagImpl(Notice notice) {
		String contractType = notice.getData().getContractType().getId();
		long fv = notice.getObjs().get(0).getTotalFinalValue();
		String fc = notice.getObjs().get(0).getTotalFinalValueCurr();
		LOG.trace("Final value: {}", fv);
		if (fc.matches("HUF|Ft|forint") && !td3HasEstimVal(notice)) {
			if ("NC-1".equals(contractType) && fv > maxValueInWorks) {
				return returnFlag("infoWorks", "final=" + (fv / 1000000),
						"max=" + (maxValueInWorks / 1000000));
			}
			if ("NC-2".equals(contractType) && fv > maxValueInSupply) {
				return returnFlag("infoSupply", "final=" + (fv / 1000000),
						"max=" + (maxValueInSupply / 1000000));
			}
			if ("NC-4".equals(contractType) && fv > maxValueInService) {
				return returnFlag("infoService", "final=" + (fv / 1000000),
						"max=" + (maxValueInService / 1000000));
			}
		}
		return null;
	}

	private boolean td3HasEstimVal(Notice notice) {
		try {
			for (Notice n : notice.getFamilyMembers()) {
				metadataParser.process(n);
				if (n.getData().getDocumentType().getId().equals("TD-3")) {
					docParser.process(n);
					estimParser.process(n);
					long ev = n.getObjs().get(0).getEstimatedValue();
					String ec = n.getObjs().get(0).getEstimatedValueCurr();
					LOG.trace("Estimated value: {}", ev);
					return 0 < ev && ec.matches("HUF|Ft|forint");
				}
			}
		} catch (Exception e) {
			LOG.warn(
					"Could not fetch estimated value from TD-3 of {} notice's",
					notice.getId());
			LOG.trace("Failed to fetch estimated value", e);
		}
		return false;
	}

}
