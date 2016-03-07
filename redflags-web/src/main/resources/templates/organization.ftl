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



############################################################################ -->

<#if !basic??>
	<p class="text-center text-danger notfound"><strong>
		<@label "organization.notfound" />
	</strong></p>
	<div class="text-center">
		<a class="btn btn-default" href="/organization"><@label "organization.back" /></a>
	</div>
<#else>

<@accordion>

	<@accordion_panel id="basic" headingLabel="organization.title" open=true>
		<@details map=basic fields=[
			'name',
			'code',
			'_type',
			'rawMainActivities'
			] labelPrefix="organization" />		
	</@accordion_panel>	

	<#if calls?? && calls?size &gt; 0>
		<@accordion_panel id="calls" headingLabel="organization.calls" headingSuffix=" (${callCount})" open=true>
			<div class="row"><div class="col-md-12">
				<table class="table table-striped table-hover list-table">
					<#list calls as obj>
						<tr>
							<td class="col-md-2 text-right">
								<strong><a href="/notice/${obj.id}">${obj.id}</a></strong>
								
								<br/>
								
								${obj.date?date}
							</td>
							<td class="col-md-7 hidden-sm hidden-xs" data-toggle="tooltip" data-placement="bottom" data-container="body" <#if obj.title??>title="${obj.title}"</#if>>
								<#if obj.type??><strong>${obj.type}&nbsp;</strong></#if>
								<#if obj.title??>
								<em>
								<#assign s = obj.title />
								<#if s?length  &gt; 75>
									<#assign s = s[0..75] + "..." />
								</#if>
								<#-- ${s?replace("\r?\n", "<br/>", "r")} -->
								${s}
								</em>
								</#if>
							</td>
							<td class="col-md-1 text-right">
								<span class="visible-sm visible-xs text-left"><#if obj.type??><strong>${obj.type}&nbsp;</strong></#if></span>
								
								<#if obj.estimated?? && obj.estimatedCurr??> 
									<span data-toggle="tooltip" data-placement="bottom" data-container="body" title="<@label "notice.obj.estimatedValue" />">
									${(obj.estimated/1000000)?string["0.#"]}&nbsp;M&nbsp;${obj.estimatedCurr}
									</span>
								</#if>
							</td>
							<td class="col-md-1 text-center">
								<@smallFlags obj.flags />
							</td>
						</tr>
					</#list>
				</table>
				<p><strong><a href="/notices/10/1/by-date?filter=contr:${basic["id"]}"><@label "organization.allCalls" /> »</a></strong></p>
			</div></div>
		</@accordion_panel>
	</#if>
	
	<#if wins?? && wins?size &gt; 0>
		<@accordion_panel id="wins" headingLabel="organization.wins" open=true headingSuffix="(${winCount})">
			<div class="row"><div class="col-md-12">
				<table class="table table-striped table-hover list-table">
					<#list wins as obj>
						<tr>
							<td class="col-md-2 text-right">
								<strong><a href="/notice/${obj.id}">${obj.id}</a></strong>
								
								<br/>
								
								${obj.date?date}
							</td>
							<td class="col-md-7 hidden-sm hidden-xs" data-toggle="tooltip" data-placement="bottom" data-container="body" <#if obj.title??>title="${obj.title}"</#if>>
								<#if obj.type??><strong>${obj.type}&nbsp;</strong></#if>
								<#if obj.title??>
								<em><a href="/notice/${obj.id}">
								<#assign s = obj.title />
								<#if s?length  &gt; 75>
									<#assign s = s[0..75] + "..." />
								</#if>
								<#-- ${s?replace("\r?\n", "<br/>", "r")} -->
								${s}
								</a></em>
								</#if>
								
								<#if obj.winners?? && obj.winners?size &gt; 0 >
									<br />
									<@label "notices.winners" />
									<#assign first = true>
									<#list obj.winners as w>
										<#if !first>,</#if>
										<a href="/organization/${w.id}">${w.name}</a>
										<#assign first=false />
									</#list>
								</#if>			
							</td>
							<td class="col-md-1 text-right">
								<span class="visible-sm visible-xs text-left"><#if obj.type??><strong>${obj.type}&nbsp;</strong></#if></span>
								
								<#if obj.estimated?? && obj.estimatedCurr??> 
									<span data-toggle="tooltip" data-placement="bottom" data-container="body" title="<@label "notice.obj.estimatedValue" />">
									${(obj.estimated/1000000)?string["0.#"]}&nbsp;M&nbsp;${obj.estimatedCurr}
									</span>
								</#if>
								
								<#if obj.total?? && obj.totalCurr??> 
									<span data-toggle="tooltip" data-placement="bottom" data-container="body" title="<@label "notice.obj.totalFinalValue" />">				
									${(obj.total/1000000)?string["0.#"]}&nbsp;M ${obj.totalCurr}
									</span>
								</#if>
							</td>
							<td class="col-md-1 text-center">
								<@smallFlags obj.flags />
							</td>
						</tr>
					</#list>
				</table>
				<p><strong><a href="/notices/10/1/by-date?filter=winner:${basic["id"]}"><@label "organization.allWins" /> »</a></strong></p>
			</div></div>
		</@accordion_panel>
	</#if>	

</@accordion>

<!-- Query time: ${queryTime} ms -->

</#if>

<#include "/includes/footer.ftl">