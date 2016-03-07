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
 
<#include "/includes/import-spring.ftl">
<#include "/includes/general-macros.ftl">
<#include "/includes/filter-readable.ftl">

<h1>RedFlags</h1>

<@label "filter.email.greeting" /> ${user},


<@label "filter.email.beforeCount" /> <strong>${count} db</strong> <@label "filter.email.afterCount" />

<em>${readableFilter(filters)}</em>

<@label "filter.email.beforeTable" />

<ul><#list notices as n>
	<li><a href="${urlPrefix}/notice/${n.id}"><strong>${n.id}</strong></a> <#if n.typeId??><strong>(<@label "notice.data.documentType.${n.typeId?split('-')[1]}" />)</strong></#if> <em><#if n.title??>${n.title}</#if></em></li>
</#list></ul>

<@label "filter.email.afterTable" /> ${resultsUrl}

<@label "filter.email.close" />,
RedFlags

${date!""}
<@label "filter.email.noreply" />
<@label "filter.email.unsubscribe" /> <a href="${unsubscribeUrl}">${unsubscribeUrl}</a>
<@label "filter.email.contact" /> ${contactAddress}