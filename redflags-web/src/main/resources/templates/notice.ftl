<#--
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
 -->
 
<#include "/includes/header.ftl">
<#include "/includes/flag-info.ftl">

<#-- ###########################################################################

Model:

	basic
		Basic notice info.
		
	...


############################################################################ -->

<#if !basic??>
	<p class="text-center text-danger notfound"><strong>
		<@label "notice.notfound" />
	</strong></p>
	<div class="text-center">
		<a class="btn btn-default" href="/notices"><@label "notice.back" /></a>
	</div>
<#else>

<#-- TITLE ----------------------------------------------------------------- -->

<div class="row" id="notice-title">
	<div class="col-md-10">
		<#if objs??><#list objs as o>
			<#if o.contractTitle??><h3>${o.contractTitle}</h3><#break></#if>
		</#list></#if>
	</div>
	<div class="col-md-2 text-right">
		<a class="btn btn-xs btn-success" href="${basic.tedUrl}" target="_blank">
			<@label "notice.checkSource" />
			&nbsp;<span class="glyphicon glyphicon-new-window"></span>
		</a>
	</div>
</div>

<#-- RELATED --------------------------------------------------------------- -->

<#if rels?? && rels?size &gt; 0>

	<#assign addInfoWarn = false />
	<#list rels as r>
		<#if r.typeId == "TD-2">
			<#assign addInfoWarn = true />
		</#if>
	</#list>

	<div class="row"><div class="col-md-10 col-md-offset-1">
		<div class="panel panel-success"><div class="panel-heading">
		
				<h3 class="panel-title"><@label "notice.docFamily" /></h3>
				
		</div><div class="panel-body">
					
			<#list rels as r>
				<div class="row <#if basic.id = r.id>related-notice-current</#if>">
					<div class="col-md-3">
						<#if basic.id = r.id>
							${r.id}
						<#else>
							<a href="/notice/${r.id}">${r.id}</a>
						</#if>
					</div>
					<div class="col-md-7">					
						<p><#if r.typeId??><strong><@label "notice.data.documentType.${r.typeId?split('-')[1]}" /></strong></#if></p>
					</div>
					<div class="col-md-2">
						<@smallFlags r.flags />
					</div>
				</div>
			</#list>
			
			<#if addInfoWarn>
				<div class="row">
					<div class="col-xs-12 add-info-warning">
						<div><span class="glyphicon glyphicon-exclamation-sign"></span></div>
						<div><@label "notice.addInfoWarning" /></div>
					</div>
				</div>
			</#if>
			
		</div></div>
	</div></div>
</#if>

<#-- FLAGS ----------------------------------------------------------------- -->

<#if (flags?? && flags?size &gt; 0) || (cnFlags?? && cnFlags?size &gt; 0) >
	<div class="row">
		<div class="col-md-10 col-md-offset-1">
			<div class="panel panel-danger">
				<div class="panel-heading">
					<h3 class="panel-title"><@label "notice.flags" /> (${flags?size})</h3>
				</div>
				<div class="panel-body">
					<#if flags?? && flags?size &gt; 0 >
						<div class="row">
							<div class="col-xs-12">
								<@detailedFlags flags />
							</div>
						</div>
					</#if>
					
					<#if cnFlags?? && cnFlags?size &gt; 0 >
						<div class="row">
							<div class="col-xs-12">
								<p><strong><@label "notice.flagsOfContractNotice" /></strong></p>
							</div>
						</div>				
						<div class="row">
							<div class="col-xs-12">
								<@detailedFlags cnFlags />
							</div>
						</div>
					</#if>
				</div>
			</div>
		</div>
	</div>
</#if>


<@accordion>

	<@accordion_panel id="data" headingLabel="notice.data" open=true>
		<@details map=data fields=['publicationDate'] labelPrefix="notice.data" />
		<#if contr??>
		<div class="row details-row">
			<div class="col-md-3">
				<strong><@label "notice.contr.name" /></strong>
			</div>
			<div class="col-md-9">
				<p><a href="/organization/${contr.id}">${contr.name}</a></p>
			</div>
		</div>
		</#if>
		<#if awards?? && awards?size &gt; 0>
			<div class="row details-row">
				<div class="col-md-3">
					<strong><@label "notice.awards" /></strong>
				</div>
				<div class="col-md-9">
					<#list awards as a>		
						<p><#if a.winner_id??><a href="/organization/${a.winner_id}">${a.winner_name}</a></#if>					
						<#if a.totalFinalValue &gt; 0>(${(a.totalFinalValue/1000000)?string["0.#"]}&nbsp;M&nbsp;${a.totalFinalValueCurr})</#if></p>
					</#list>
				</div>
			</div>
		</#if>
		<@details map=data fields=[
			<#-- 'authorityType', -->
			'contractType',
			'procedureType',
			'awardCriteria',
			'directive',			
			'title',
			'internetAddress'
			] labelPrefix="notice.data" />
		<div class="row details-row">
			<div class="col-md-3">
				<strong><@label "notice.data.cpvCodes" /></strong>
			</div>
			<div class="col-md-9">
				<#list data.cpvCodes as cpv>
					<p><a href="/notices/10/1/by-date/?filter=cpv:${cpv.id}">${cpv.id}</a> (${cpv.name})</p>
				</#list>
			</div>
		</div>		
	</@accordion_panel>	
	
	<#if contr??>
		<@accordion_panel id="contr" headingLabel="notice.contractingAuthority" open=false>
			<div class="row details-row">
				<div class="col-md-3">
					<strong><@label "notice.contr.name" /></strong>
				</div>
				<div class="col-md-9">
					<a href="/organization/${contr.id}">${contr.name}</a>
				</div>
			</div>
			<@details map=contr fields=['_type'] labelPrefix="notice.contr" />
		</@accordion_panel>
	</#if>
	
	<#if objs?? && objs?size &gt; 0>
		<#assign i = 0 />
		<#list objs as o>
			<@accordion_panel id="obj-${o.id}" headingLabel="notice.objOfTheContract" open=false headingSuffix="(${i+1}/${objs?size})">
				<@details map=o fields=['contractTitle', 'shortDescription', 'lotTitle', 'placeOfPerformance', 'totalQuantity', 'frameworkAgreement', 'awardCriteria'] labelPrefix="notice.obj" />
				
				<#if o.estimatedValue?? && o.estimatedValueCurr?? && o.estimatedValue &gt; 0 >
					<div class="row details-row">
						<div class="col-md-3">
							<strong><@label "notice.obj.estimatedValue" /></strong>
						</div>
						<div class="col-md-9">
							${o.estimatedValue} ${o.estimatedValueCurr}				
						</div>
					</div>
				</#if>
				
				<#if o.lotEstimatedValue?? && o.lotEstimatedValueCurr?? && o.lotEstimatedValue &gt; 0 >
					<div class="row details-row">
						<div class="col-md-3">
							<strong><@label "notice.obj.lotEstimatedValue" /></strong>
						</div>
						<div class="col-md-9">
							${o.lotEstimatedValue} ${o.lotEstimatedValueCurr}				
						</div>
					</div>
				</#if>
			
				<#if o.totalFinalValue?? && o.totalFinalValueCurr?? && o.totalFinalValue &gt; 0 >
					<div class="row details-row">
						<div class="col-md-3">
							<strong><@label "notice.obj.totalFinalValue" /></strong>
						</div>
						<div class="col-md-9">
							${o.totalFinalValue} ${o.totalFinalValueCurr}				
						</div>
					</div>
				</#if>
			
				<#if o.duration??>
					<div class="row details-row">
						<div class="col-md-3">
							<strong><@label "notice.obj.duration" /></strong>
						</div>
						<div class="col-md-9">
							<@duration o.duration/>				
						</div>
					</div>
				</#if>
				
				<#if o.renewalCount &gt; 0>
					<div class="row details-row">
						<div class="col-md-3">
							<strong><@label "notice.obj.renewalCount" /></strong>
						</div>
						<div class="col-md-9">
							${o.renewalCount}			
						</div>
					</div>
				</#if>
				
				<#if o.renewalDuration??>
					<div class="row details-row">
						<div class="col-md-3">
							<strong><@label "notice.obj.renewalDuration" /></strong>
						</div>
						<div class="col-md-9">
							<@duration o.renewalDuration />				
						</div>
					</div>
				</#if>
				
			</@accordion_panel>
			<#assign i = i + 1 />
		</#list>
	</#if>
	
	<#if lots?? && lots?size &gt; 0>
		<#assign i = 0 />
		<#list lots as a>
			<@accordion_panel id="lot-${a.id}" headingLabel="notice.lot" open=false headingSuffix="(${i+1}/${lots?size})">
				<@details map=a fields=[ 'numb', 'title', 'shortDescription', 'quantity' ] labelPrefix="notice.lot" />
			
				<#if a.differentDuration??>
					<div class="row details-row">
						<div class="col-md-3">
							<strong><@label "notice.lot.differentDuration" /></strong>
						</div>
						<div class="col-md-9">
							<@duration a.differentDuration/>				
						</div>
					</div>
				</#if>
				
				<#if a.cpvCodes??>
					<div class="row details-row">
						<div class="col-md-3">
							<strong><@label "notice.lot.cpvCodes" /></strong>
						</div>
						<div class="col-md-9">
							<#list a.cpvCodes as cpv>
								<p>${cpv.id} (${cpv.name})</p>
							</#list>
						</div>
					</div>		
				</#if>
				
				<@details map=a fields=[ 'additionalInfo' ] labelPrefix="notice.lot" />
			</@accordion_panel>
			<#assign i = i + 1 />
		</#list>
	</#if>

	<#if left??>
		<@accordion_panel id="left" headingLabel="notice.left" open=false>
			<@details map=left fields=['personalSituation', 'financialAbility', 'technicalCapacity'] labelPrefix="notice.left" />
		</@accordion_panel>
	</#if>
	
	<#if proc??>
		<@accordion_panel id="proc" headingLabel="notice.procedure" open=false>
			<@details map=proc fields=['procedureTypeInfo', 'awardCriteria'] labelPrefix="notice.proc" />
		</@accordion_panel>
	</#if>
	
	<#if awards?? && awards?size &gt; 0>
		<#assign i = 0 />
		<#list awards as a>
			<@accordion_panel id="award-${a.id}" headingLabel="notice.award" open=false headingSuffix="(${i+1}/${awards?size})">
				<@details map=a fields=[ 'number', 'lotNumber', 'lotTitle', 'decisionDate', 'numberOfOffers' ] labelPrefix="notice.award" />
				
				<#if a.winner_id??>				
				<div class="row details-row">
					<div class="col-md-3">
						<strong><@label "notice.award.winner" /></strong>
					</div>
					<div class="col-md-9">
						<a href="/organization/${a.winner_id}">${a.winner_name}</a>
					</div>
				</div>
				</#if>
			
				<#if a.totalEstimatedValue &gt; 0>
				<div class="row details-row">
					<div class="col-md-3">
						<strong><@label "notice.award.totalEstimatedValue" /></strong>
					</div>
					<div class="col-md-9">
						${a.totalEstimatedValue} ${a.totalEstimatedValueCurr}				
					</div>	
				</div>
				</#if>
				<#if a.totalFinalValue &gt; 0>
				<div class="row details-row">
					<div class="col-md-3">
						<strong><@label "notice.award.totalFinalValue" /></strong>
					</div>
					<div class="col-md-9">
						${a.totalFinalValue} ${a.totalFinalValueCurr}				
					</div>
				</div>				
				</#if>
			</@accordion_panel>
			<#assign i = i + 1 />
		</#list>
	</#if>
	
	<#if compl??>
		<@accordion_panel id="compl" headingLabel="notice.complementaryInfo" open=false>
			<@details map=compl fields=['additionalInfo'] labelPrefix="notice.compl" />		
		</@accordion_panel>			
	</#if>

</@accordion>

<!-- Query time: ${queryTime} ms -->

</#if>

<#include "/includes/footer.ftl">



<#macro duration duration>
	<#if duration.beginDate?? || duration.endDate??>
		<#if duration.beginDate??>${duration.beginDate?date}<#else>?</#if>
		-
		<#if duration.endDate??>${duration.endDate?date}<#else>?</#if>,
	</#if>
	${duration.inMonths} <@label "duration.inMonth" /> / ${duration.inDays} <@label "duration.inDays" />
</#macro>
