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
 
<#function readableFilter filters>
	<#if !filters?? || 0 == filters?size><#return ""></#if>
	
	<#assign r><@label "filter.readable.prefix" "" /></#assign>
	
	<#list filters as f>
		<#assign r>${r} <@label "filter.readable.${f.type}.prefix" "" /></#assign>
		<#if f.type == "contr" || f.type == "winner">
			<#if f.info??>
				<#assign r>${r} <strong><a href="/organization/${f.parameter}">${f.info}</a></strong>
					<@label "filter.readable.${f.type}.byId" /></#assign>
			<#else>
				<#assign r>${r} <strong>'${f.parameter}'</strong>
					<@label "filter.readable.${f.type}.byWords" /></#assign>
			</#if>
		</#if>
		<#if f.type == "cpv">
			<#assign and><@label "filter.readable.cpv.and" "" /></#assign>
			<#assign r>${r} <strong>${f.parameter?replace(',', '</strong> ${and} <strong>', 'r')}</strong></#assign>
		</#if>
		<#if f.type =="date">
			<#assign r>${r} <strong>${f.parameter?split(":")[0]}</strong></#assign>
			<#assign r>${r} <@label "filter.readable.date.infix" "" /></#assign>
			<#assign r>${r} <strong>${f.parameter?split(":")[1]}</strong></#assign>
		</#if>
		<#if f.type == "doc">
			<#assign r>${r} <strong><@label "notice.data.documentType.${f.parameter}" "" /></strong></#assign>
		</#if>
		<#if f.type =="flags">
			<#assign r>${r} <strong>${f.parameter?split("-")[0]}</strong></#assign>
			<#assign r>${r} <@label "filter.readable.flags.infix" "" /></#assign>
			<#assign r>${r} <strong>${f.parameter?split("-")[1]}</strong></#assign>
		</#if>
		<#if f.type =="indicators">
			<#list f.parameter?split(",") as i>
				<#assign r>${r} <strong><@label "flag.${i}.name" /></strong>, </#assign>
			</#list>
			<#assign r = r?replace(", $", "", "r") />
		</#if>
		<#if f.type =="text">
			<#assign r>${r} <strong>'${f.parameter}'</strong></#assign>
		</#if>
		<#if f.type =="value">
			<#assign r>${r} <strong>${f.parameter?split("-")[0]} M</strong></#assign>
			<#assign r>${r} <@label "filter.readable.value.infix" "" /></#assign>
			<#assign r>${r} <strong>${f.parameter?split("-")[1]} M</strong></#assign>
		</#if>
		<#assign r>${r} <@label "filter.readable.${f.type}.suffix" "" /></#assign>
	</#list>
	<#return r?replace(',$', '', 'r')+'.'>
</#function>