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
<#include "/includes/filter-readable.ftl">

<#if !filters?? || 0 == filters?size>
	<p class="text-center text-primary notfound"><strong><@label 'filters.empty', '' /></strong></p>
<#else>
	<div class="row">
		<table class="table table-striped table-hover col-md-12 list-table">
			<#list filters as f>
				<tr>
					<td class="col-md-12">
						<div class="col-lg-10">
							<p>						
								<#assign noname><@label "filter.noname" /></#assign>
								<strong>${f.name!"${noname}"}</strong>
								<#if f.subscribe>
									&nbsp;
									<span class="glyphicon glyphicon-send text-primary"
								 	data-toggle="tooltip" data-placement="bottom"
								 	title="<@label "filters.subscribe.tooltip" />"></span>
								</#if>
								<br/>
								${readableFilter(f.filters)}
								<!-- ${f.filter} -->			
							</p>
						</div>				
						<div class="col-lg-2 text-right form-group">						
							<a class="btn btn-primary" href="/notices/10/1/by-date?filter=${f.filter}"
								data-toggle="tooltip" data-placement="bottom"
								title="<@label "filters.doFilter" />">
								<span class="glyphicon glyphicon-filter"></span>
							</a>
							&nbsp;&nbsp;
							<a class="btn btn-success" href="/filter/${f.id}"
								data-toggle="tooltip" data-placement="bottom"
								title="<@label "filters.edit" />">
								<span class="glyphicon glyphicon-pencil"></span>
							</a>
							&nbsp;&nbsp;
							<a class="btn btn-danger delete-link" href="#" data-url="/filter/delete/${f.id}"
								data-toggle="tooltip" data-placement="bottom"
								title="<@label "filters.delete" />">
								<span class="glyphicon glyphicon-remove"></span>
							</a>
						</div>
					</td>
				</tr>
			</#list>
		</table>
	</div>
</#if>

<script type="text/javascript">
$(function() {
	$(".delete-link").click(function() {
		if (confirm("<@label "filters.delete.areyousure" />"))
			window.location.href = $(this).data("url");
	});
});
</script>

<#include "/includes/footer.ftl">