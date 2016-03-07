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
 
<#macro label id notfoundtext=id>
	<@spring.messageText id notfoundtext />
</#macro>

<#macro pager pages page counts count urlFormat pagePlaceholder countPlaceholder>

	<#if pages &gt; 0>
		<div class="text-center">
			
			<ul class="pagination pagination-sm">
				<#assign prev><#if page == 1>#<#else>${urlFormat?replace(pagePlaceholder, '${(page-1)?c}')?replace(countPlaceholder, '${count?c}')}</#if></#assign>
				<#assign next><#if page == pages>#<#else>${urlFormat?replace(pagePlaceholder, '${(page+1)?c}')?replace(countPlaceholder, '${count?c}')}</#if></#assign>
				<li><a href="${prev}">«</a></li>
				<#assign last = 1/>
				<#list 1..pages as p>
					<#if p&lt;4 || (p-page)?abs&lt;3 || p&gt;pages-3>
						<#if p-last&gt;1><li><a href="#">...</a></li></#if>
						<#assign last = p/>
						<#assign curr>${urlFormat?replace(pagePlaceholder, '${p?c}')?replace(countPlaceholder, '${count?c}')}</#assign>
						<li <#if p == page>class="active"</#if>><a href="${curr}">${p}</a></li>
					</#if>
				</#list>
				<li><a href="${next}"">»</a></li>
			</ul>
			
			<#if counts?size &gt; 0>
				&nbsp;&nbsp;&nbsp;
				<ul class="pagination pagination-sm">
					<#list counts as c>
						<li <#if count == c>class="active"</#if>>
							<a href="<#if count == c>#<#else>${urlFormat?replace(pagePlaceholder, '1')?replace(countPlaceholder, '${c?c}')}</#if>">${c}</a>
						</li>
					</#list>
				</ul>				
			</#if>
			
		</div>
	</#if>

</#macro>

<#macro accordion>

	<div class="row">
		<div class="col-md-10 col-md-offset-1">
			<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
				<#nested>
			</div>
		</div>
	</div>

</#macro>

<#macro accordion_panel id headingLabel open headingSuffix="">

	<div class="panel panel-info">
		<div class="panel-heading" role="tab" id="panel-${id}-header">		
			<h3 class="panel-title">
				<a data-toggle="collapse" <#-- data-parent="#accordion" --> href="#panel-${id}-content" aria-expanded="<#if open>true<#else>false</#if>" aria-controls="panel-${id}-content">
					<@label headingLabel />
					${headingSuffix}
				</a>
			</h3>		
		</div>
		<div id="panel-${id}-content" class="panel-collapse collapse <#if open>in</#if>" role="tabpanel" aria-labelledby="panel-${id}-header">
			<div class="panel-body">	
				<#nested>
			</div>
		</div>
	</div>
		
</#macro>

<#macro details map fields labelPrefix>

	<#list fields as f>
		<#assign o = map />
		<#if o[f]??>
			<div class="row details-row">
				<div class="col-md-3">
					<strong><@label "${labelPrefix}.${f}" /></strong>
				</div>
				<div class="col-md-9 text-justify">
					<@print_value v=o[f]/>					
				</div>
			</div>
		</#if>
	</#list>

</#macro>

<#macro print_value v>

	<p>
		<#if v?is_date_like>
			${v?date}
		<#else>
			<#-- ${v?replace("\r?\n", "<br/>", "r")} -->
			${v?replace("\r?\n", "</p><p>", "r")?replace("(^| |>)www\\.", "http://www.", "r")?replace("(^| |>)((http:\\/\\/|www\\.)[^ <]+)", "$1<a href=\"$2\" target=\"_blank\">$2</a>&nbsp;<small class=\"text-muted\"><span class=\"glyphicon glyphicon-new-window\"></span></small>", "r")}
		</#if>	
	</p>
	
</#macro>