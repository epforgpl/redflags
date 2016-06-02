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
package hu.petabyte.redflags.engine.gear.indicator;

import hu.petabyte.redflags.engine.gear.AbstractGear;
import hu.petabyte.redflags.engine.model.IndicatorMeta;
import hu.petabyte.redflags.engine.model.IndicatorResult;
import hu.petabyte.redflags.engine.model.IndicatorResult.IndicatorResultType;
import hu.petabyte.redflags.engine.model.Notice;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * @author Zsolt Jurányi
 */
public abstract class AbstractIndicator extends AbstractGear implements
IndicatorMeta {

	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractIndicator.class);
	private static final double DEFAULT_WEIGHT = 1.0;

	private @Autowired Environment env;
	private String category;
	private double weight = DEFAULT_WEIGHT;

	@Override
	public void beforeSession() throws Exception {
		String prefix = this.getClass().getName();
		category = env.getProperty(prefix + ".category", category);
		weight = env.getProperty(prefix + ".weight", Double.class, weight);
		LOG.trace("{} initialized", toString());
	}

	protected String fetchAdditionalInfo(Notice notice) {
		String s = "";
		if (null != notice.getCompl()
				&& null != notice.getCompl().getAdditionalInfo()) {
			s = notice.getCompl().getAdditionalInfo();
		}
		return s;
	}

	protected String fetchAwardCriteria(Notice notice) {
		try {
			return notice.getData().getAwardCriteria().getId();
		} catch (NullPointerException e) {
			return "";
		}
	}

	protected String fetchContractType(Notice notice) {
		try {
			return notice.getData().getContractType().getId();
		} catch (NullPointerException e) {
			return "";
		}
	}

	protected long fetchEstimatedValue(Notice notice) {
		for (ObjOfTheContract obj : notice.getObjs()) {
			if (0 < obj.getEstimatedValue()) {
				return obj.getEstimatedValue();

			}
		}
		return 0L;
	}

	protected String fetchEstimatedValueCurrency(Notice notice) {
		for (ObjOfTheContract obj : notice.getObjs()) {
			if (0 < obj.getEstimatedValue()) {
				return obj.getEstimatedValueCurr();
			}
		}
		return null;
	}

	protected String fetchFinancialAbility(Notice notice) {
		String s = "";
		if (null != notice.getLeft()
				&& null != notice.getLeft().getFinancialAbility()) {
			s = notice.getLeft().getFinancialAbility();
		}
		return s;
	}

	protected String fetchFrameworkAgreement(Notice notice) {
		StringBuilder s = new StringBuilder();
		for (ObjOfTheContract obj : notice.getObjs()) {
			if (null != obj.getFrameworkAgreement()) {
				s.append(obj.getFrameworkAgreement());
				s.append("\n");
			}
		}
		return s.toString().trim();
	}

	protected String fetchObtainingSpecs(Notice notice) {
		String s = "";
		if (null != notice.getProc()
				&& null != notice.getProc().getObtainingSpecs()) {
			s = notice.getProc().getObtainingSpecs();
		}
		return s;
	}

	protected String fetchPersonalSituation(Notice notice) {
		String s = "";
		if (null != notice.getLeft()
				&& null != notice.getLeft().getPersonalSituation()) {
			s = notice.getLeft().getPersonalSituation();
		}
		return s;
	}

	protected String fetchProcedureType(Notice notice) {
		try {
			return notice.getData().getProcedureType().getId();
		} catch (NullPointerException e) {
			return "";
		}
	}

	protected String fetchProcedureTypeInfo(Notice notice) {
		String s = "";
		if (null != notice.getProc()
				&& null != notice.getProc().getProcedureTypeInfo()) {
			s = notice.getProc().getProcedureTypeInfo();
		}
		return s;
	}

	protected String fetchTechnicalCapacity(Notice notice) {
		String s = "";
		if (null != notice.getLeft()
				&& null != notice.getLeft().getTechnicalCapacity()) {
			s = notice.getLeft().getTechnicalCapacity();
		}
		return s;
	}

	public abstract IndicatorResult flag(Notice notice);

	@Override
	public String getCategory() {
		return category;
	}

	/**
	 * Generates an identifier for the given indicator class from the name of
	 * the direct parent package and the simple class name. So
	 * <code>hu.petabyte.redflags.engine.gear.indicator.hu.AwCritLacksIndicator</code>
	 * will have <code>hu.AwCritLacksIndicator</code> as its identifier.
	 *
	 * @param clazz
	 *            Indicator class to generate identifier for
	 * @return Identifier for the indicator generated from the name of the
	 *         direct parent package and the simple class name.
	 */
	public static String getIdForIndicatorClass(
			Class<? extends AbstractIndicator> clazz) {
		Matcher m = Pattern.compile("(^|.*\\.)([^.]+\\.[^.]+)$").matcher(
				clazz.getName());
		return m.find() ? m.group(2) : clazz.getSimpleName();
	}

	/**
	 * Calls {@link #getIdForIndicatorClass} to generates an identifier for this
	 * indicator.
	 *
	 * @see #getIdForIndicatorClass(Class)
	 * @return Identifier for the indicator.
	 */
	@Override
	public final String getIndicatorId() {
		return getIdForIndicatorClass(this.getClass());
	}

	@Override
	public double getWeight() {
		return weight;
	}

	protected IndicatorResult irrelevantData() {
		return new IndicatorResult(this, IndicatorResultType.IRRELEVANT_DATA);
	}

	protected String label(String label, String... args) {
		StringBuilder s = new StringBuilder();
		s.append("flag.");
		s.append(getIndicatorId());
		s.append(".");
		s.append(label);
		for (String arg : args) {
			s.append("|");
			s.append(arg.replaceAll("\\|", "::"));
		}
		return s.toString();
	}

	protected IndicatorResult missingData() {
		return new IndicatorResult(this, IndicatorResultType.MISSING_DATA);
	}

	@Override
	protected Notice processImpl(Notice notice) throws Exception {
		IndicatorResult r = null;
		if (!notice.isCancelled()) {
			r = flag(notice);
			r = null == r ? new IndicatorResult(this) : r;
			LOG.debug("{} << {}", notice.getId().toString(), r.toString());
			// toString prints Indicator name too
			notice.getIndicatorResults().put(getIndicatorId(), r);
		} else {
			LOG.debug("{} is a cancelled notice, no flagging", notice.getId()
					.toString());
		}
		return notice;
	}

	protected IndicatorResult returnFlag() {
		return returnFlag(weight, "info");
	}

	protected IndicatorResult returnFlag(double weight, String label,
			String... args) {
		String description = label(label, args);
		return new IndicatorResult(this).flag().weight(weight)
				.description(description);
	}

	protected IndicatorResult returnFlag(String label, String... args) {
		return returnFlag(weight, label, args);
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return getIndicatorId() + " [category=" + category + ", weight="
				+ weight + "]";
	}

}
