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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.petabyte.redflags.engine.model.noticeparts.Award;
import hu.petabyte.redflags.engine.model.noticeparts.ComplementaryInfo;
import hu.petabyte.redflags.engine.model.noticeparts.ContractingAuthority;
import hu.petabyte.redflags.engine.model.noticeparts.LEFTInfo;
import hu.petabyte.redflags.engine.model.noticeparts.Lot;
import hu.petabyte.redflags.engine.model.noticeparts.ObjOfTheContract;
import hu.petabyte.redflags.engine.model.noticeparts.Procedure;

/**
 * @author Zsolt Jurányi
 */
public class Notice {

	// meta
	private final NoticeID id;
	private NoticeID documentFamilyId;
	private boolean cancelled;
	private final Data data = new Data();
	private final List<Notice> familyMembers = new ArrayList<Notice>();
	// document
	private ContractingAuthority contr;
	private final List<ObjOfTheContract> objs = new ArrayList<ObjOfTheContract>();
	private final List<Lot> lots = new ArrayList<Lot>();
	private LEFTInfo left;
	private Procedure proc;
	private final List<Award> awards = new ArrayList<Award>();
	private ComplementaryInfo compl;
	// flags
	private final Map<String, IndicatorResult> indicatorResults = new HashMap<String, IndicatorResult>();

	public Notice(NoticeID id) {
		this.id = id;
	}


	public List<Award> getAwards() {
		return awards;
	}

	public ComplementaryInfo getCompl() {
		return compl;
	}

	public ContractingAuthority getContr() {
		return contr;
	}

	public Data getData() {
		return data;
	}

	public NoticeID getDocumentFamilyId() {
		return documentFamilyId;
	}

	public List<Notice> getFamilyMembers() {
		return familyMembers;
	}

	public NoticeID getId() {
		return id;
	}

	public Map<String, IndicatorResult> getIndicatorResults() {
		return indicatorResults;
	}

	public LEFTInfo getLeft() {
		return left;
	}

	public List<Lot> getLots() {
		return lots;
	}

	public List<ObjOfTheContract> getObjs() {
		return objs;
	}

	public Procedure getProc() {
		return proc;
	}

	public String getUrl() {
		return this.id.calculateURL();
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public void setCompl(ComplementaryInfo compl) {
		this.compl = compl;
	}

	public void setContr(ContractingAuthority contr) {
		this.contr = contr;
	}

	public void setDocumentFamilyId(NoticeID documentFamilyId) {
		this.documentFamilyId = documentFamilyId;
	}

	public void setLeft(LEFTInfo left) {
		this.left = left;
	}

	public void setProc(Procedure proc) {
		this.proc = proc;
	}

	@Override
	public String toString() {
		return "Notice{" +
				"id=" + id +
				", url='" + this.id.calculateURL() + '\'' +
				", documentFamilyId=" + documentFamilyId +
				", cancelled=" + cancelled +
				", data=" + data +
				", familyMembers=" + familyMembers +
				", contr=" + contr +
				", objs=" + objs +
				", lots=" + lots +
				", left=" + left +
				", proc=" + proc +
				", awards=" + awards +
				", compl=" + compl +
				", indicatorResults=" + indicatorResults +
				'}';
	}
}
