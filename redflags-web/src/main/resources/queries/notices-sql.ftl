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

<#function like p>
	<#return p?replace('[^A-Za-zÁÉÍÓÖŐÚÜŰáéíóöőúüű0-9]+| +', '%', 'r') />
</#function>

									<#if counting??>
SELECT COUNT(1) FROM (
									</#if>
SELECT
	DISTINCT n.*
FROM
									<#if !counting?? && !orderByFlags??> 
	(select * from rfwl_notices order by date desc, lpad(id, 12, '0') desc) n
									<#else>
	rfwl_notices n
									</#if>
	
									<#if filters??><#list filters as f>
									
										<#-- CPV FILTER -->									
										<#if f.type == "cpv">
	INNER JOIN (
		SELECT * FROM rfwl_cpvs WHERE 1=1
											<#list f.parameter?split(",") as c>
		AND cpvs like "%${c}%"
											</#list>
		) cpvs ON n.id = cpvs.noticeId
										</#if>
										
										<#-- INDICATORS FILTER -->
										<#if f.type == "indicators">
											<#assign it = 0 />
											<#list f.parameter?split(",") as i><#assign it = it+1 />
	INNER JOIN (SELECT noticeId FROM te_flag WHERE id like "%-${i}") it${it} on it${it}.noticeId = n.id
											</#list>
										</#if>
										
										<#-- TEXT FILTER -->
										<#if f.type == "text">
	INNER JOIN (
		SELECT noticeId FROM te_complementaryinfo WHERE additionalInfo LIKE "%${like(f.parameter)}%"
		UNION
		SELECT noticeId FROM te_leftinfo WHERE financialAbility LIKE "%${like(f.parameter)}%"
		UNION
		SELECT noticeId FROM te_leftinfo WHERE personalSituation LIKE "%${like(f.parameter)}%"
		UNION
		SELECT noticeId FROM te_leftinfo WHERE technicalCapacity LIKE "%${like(f.parameter)}%"
		UNION
		SELECT noticeId FROM te_objofthecontract WHERE contractTitle LIKE "%${like(f.parameter)}%"
		UNION
		SELECT noticeId FROM te_procedure WHERE awardCriteria LIKE "%${like(f.parameter)}%"
		UNION
		SELECT noticeId FROM te_procedure WHERE limitOfInvitedOperators LIKE "%${like(f.parameter)}%"
	) t ON n.id = t.noticeId
										</#if>
										
										<#-- WINNER FILTER -->
										<#if f.type == "winner">
	INNER JOIN (
		SELECT * FROM rfwl_winners WHERE
			winnerOrgId = "${f.parameter}" OR winnerOrgName LIKE "%${like(f.parameter)}%"
		) w ON n.id = w.noticeId
										</#if>
									</#list></#if>			
		
WHERE 1=1
								<#if filters??><#list filters as f>
								
									<#-- CONTR FILTER -->
									<#if f.type == "contr">
	AND (contractingOrgId = "${f.parameter}" OR contractingOrgName LIKE "%${like(f.parameter)}%") 
									</#if>
									
									<#-- DATE FILTER -->
									<#if f.type == "date">
	AND n.date BETWEEN "${f.parameter?split(":")[0]}" AND "${f.parameter?split(":")[1]}"
									</#if>
										
									<#-- DOC FILTER -->
									<#if f.type == "doc">
	AND typeId = "TD-${f.parameter}"
									</#if>
									
									<#-- FLAGS FILTER -->
									<#if f.type == "flags">
	AND (flagCount BETWEEN ${f.parameter?split("-")[0]} AND ${f.parameter?split("-")[1]})
									</#if>
									
									<#-- VALUE FILTER -->				
									<#if f.type == "value">
	AND (n.estimated BETWEEN ${f.parameter?split("-")[0]}*1000000 AND ${f.parameter?split("-")[1]}*1000000
		OR n.total BETWEEN ${f.parameter?split("-")[0]}*1000000 AND ${f.parameter?split("-")[1]}*1000000)
									</#if>
									
									<#-- AFTER FILTER -->
									<#if f.type == "after">
	AND id IN (SELECT id FROM te_notice WHERE
		(noticeNumber > ${f.parameter?split("-")[0]} and noticeYear = ${f.parameter?split("-")[1]}) or (noticeYear > ${f.parameter?split("-")[1]}))
									</#if>
									
								</#list></#if>

								<#if !counting??>
							<#if orderByFlags??>
ORDER BY n.flagCount DESC, n.date DESC, lpad(n.id, 12, '0') DESC
							</#if>
LIMIT ? OFFSET ?;
								<#else>
) q;
								</#if>