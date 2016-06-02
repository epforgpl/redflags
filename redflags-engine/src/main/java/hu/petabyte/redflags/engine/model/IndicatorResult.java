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
package hu.petabyte.redflags.engine.model;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Zsolt Jurányi
 */
public class IndicatorResult {

	public static enum IndicatorResultType {
		MISSING_DATA, IRRELEVANT_DATA, NO_FLAG, FLAG;
	}

	private final String indicator;
	private IndicatorResultType type;
	private String flagCategory;
	private double weight = 0.0;
	private String description;

	public IndicatorResult(IndicatorMeta indicator) {
		this(indicator, IndicatorResultType.NO_FLAG);
	}

	public IndicatorResult(IndicatorMeta indicator, IndicatorResultType type) {
		this.indicator = checkNotNull(indicator).getIndicatorId();
		this.type = type;
		this.flagCategory = indicator.getCategory();
		this.weight = indicator.getWeight();
	}

	public IndicatorResult description(String description) {
		setDescription(description);
		return this;
	}

	public IndicatorResult flag() {
		setType(IndicatorResultType.FLAG);
		return this;
	}

	public IndicatorResult flagCategory(String flagCategory) {
		setFlagCategory(flagCategory);
		return this;
	}

	public String getDescription() {
		return description;
	}

	public String getFlagCategory() {
		return flagCategory;
	}

	public String getIndicator() {
		return indicator;
	}

	public IndicatorResultType getType() {
		return type;
	}

	public double getWeight() {
		return weight;
	}

	public IndicatorResult irrelevantData() {
		setType(IndicatorResultType.IRRELEVANT_DATA);
		return this;
	}

	public IndicatorResult missingData() {
		setType(IndicatorResultType.MISSING_DATA);
		return this;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFlagCategory(String flagCategory) {
		this.flagCategory = flagCategory;
	}

	public void setType(IndicatorResultType type) {
		this.type = type;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public String toString() {
		return "IndicatorResult [indicator=" + indicator + ", type=" + type
				+ ", flagCategory=" + flagCategory + ", weight=" + weight
				+ ", description=" + description + "]";
	}

	public IndicatorResult type(IndicatorResultType type) {
		setType(type);
		return this;
	}

	public IndicatorResult weight(double weight) {
		setWeight(weight);
		return this;
	}

}
