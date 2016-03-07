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
<#assign pageTitle><a href="/filters"><@label "filters.list" /></a> / <@label "filter.edit" /></#assign>
<#include "/includes/header.ftl">
<#include "/includes/filter-readable.ftl">

<#if !filter??>
	<p class="text-center text-primary notfound"><strong><@label 'filter.notfound', '' /></strong></p>
<#else>
	<div class="row">
		<div class="col-md-10 col-md-offset-1">
			<div class="panel panel-success">
				<div class="panel-heading">
					<h3 class="panel-title"><@label "filter.details" /></h3>
				</div>
				<div class="panel-body">
					<form class="form-horizontal" method="POST">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						<div class="row details-row form-group">				
							<div class="col-md-3">
								<label class="control-label" for="name">
									<strong><@label "filter.name" /></strong>
								</label>
							</div>
							<div class="col-md-9">
								<input class="form-control" type="text" name="name" value="${filter.name!""}" />			
							</div>
						</div>
						<div class="row details-row form-group">
							<div class="col-md-3">
								<label class="control-label">
									<strong><@label "filter.readable" /></strong>
								</label>
							</div>
							<div class="col-md-9">
								<p class="form-control-static">
									${readableFilter(filter.filters)?replace(', ', ',<br/>')}			
								</p>
							</div>
						</div>
						<div class="row details-row form-group">
							<div class="col-md-3">
								<label class="control-label" for="subscribe">
									<strong><@label "filter.subscribe" /></strong>
								</label>
							</div>
							<div class="col-md-9">
								<div class="checkbox">
									<label>
										<input type="checkbox" name="subscribe" <#if filter.subscribe>checked</#if> />
										<@label "filter.subscribe.description" />
									</label>
								</div>
							</div>
						</div>
						<div class="row details-row form-group">
							<div class="col-md-9 col-md-offset-3">
								<input type="submit" class="btn btn-success" value="<@label "filter.save" />" />
								&nbsp;&nbsp;
								<a class="btn btn-primary" href="/notices/10/1/by-date?filter=${filter.filter}">
									<@label "filter.submit" />
								</a>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</#if>

<#include "/includes/footer.ftl">