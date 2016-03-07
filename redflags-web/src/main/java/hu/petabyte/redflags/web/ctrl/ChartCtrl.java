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

import hu.petabyte.redflags.web.svc.ChartSvc;
import hu.petabyte.redflags.web.svc.ChartSvc.FlaggedNotices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zsolt Jurányi
 */
@RestController
public class ChartCtrl {

	private @Autowired ChartSvc svc;

	@RequestMapping("/chart/flagCounts")
	public ChartSvc.FlagCounts flagCounts() {
		return svc.getResult();
	}

	@RequestMapping("/chart/flaggedNotices")
	public ChartSvc.FlaggedNotices flaggedNotices() {
		FlaggedNotices r = new FlaggedNotices();
		r.getCategories().addAll(svc.quarters());
		r.getNoticeCounts().addAll(svc.noticeCountsPerQuarter());
		r.getFlaggedNoticeCounts().addAll(svc.flaggedNoticeCountsPerQuarter());
		return r;
	}

	@RequestMapping("/chart/sumValues")
	public String sumValues() {
		return svc.sumValueCSV();
	}
}
