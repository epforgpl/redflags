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
 
 <#function flagInfo info>
	<#assign first = true />						
	<#list info?split("|") as p>
		<#if first>
			<#assign lab><@label p, p /></#assign>
			<#assign first = false />
		<#else>
			<#assign k = p?split("=")[0] />
			<#if p?split("=")[1]??>
				<#assign v = p?split("=")[1] />
				<#assign lab = lab?replace("{" + k + "}", v) />
			</#if>
		</#if>
	</#list>
	<#return lab />
</#function>

<#function indName id>
	<#assign lab>flag.${id?replace(".*-", "", "r")}.name</#assign>
	<#return lab />
</#function>

<#function indDesc id>
	<#assign lab>flag.${id?replace(".*-", "", "r")}.desc</#assign>
	<#return lab />
</#function>

<#macro smallFlags flags>
	<#list flags as f>
		<div class="flag <#if f.score &lt;= 0.5>pink-flag</#if>" data-toggle="tooltip" data-placement="bottom" title="<@label indName(f.id) />"></div>
	</#list>
</#macro>

<#macro detailedFlags flags>
	<ul class="flaglist">
		<#list flags as f>
			<li class="<#if f.score &lt;= 0.5>pink-flag</#if>">
				<div style="display: table; width: 100%">
					<div style="display: table-cell; vertical-align: middle;">
						${flagInfo(f.information)}
					</div>
					<div class="text-right" style="display: table-cell; vertical-align: middle;">
						<span class="glyphicon glyphicon-info-sign flag-info-icon" tabindex="0" role="button"
						data-toggle="popover" data-trigger="focus" data-placement="left" data-container="body"
						title="<@label indName(f.id) />" data-content="<@label indDesc(f.id) />"
						data-template='<div class="popover" role="tooltip"><div class="arrow"></div><h3 class="popover-title flag-info <#if f.score &lt;= 0.5>pink-flag</#if>"></h3><div class="popover-content"></div></div>'
						></span>
					</div>
				</div>
			</li>
		</#list>
	</ul>
</#macro>