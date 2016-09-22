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

<#assign needDateRangePicker></#assign>
<#include "/includes/header.ftl">
<#include "/includes/flag-info.ftl">
<#include "/includes/filter-readable.ftl">

<#-- ###########################################################################

NEED UPDATE!

Model:

	allCount
		Pass the count of elements in the whole query.

	count
		Pass the count of elements showed on the page, it is needed to generate
		pager URLs.

	counts
		Pass the options for elements per page setting as an array.

	filter
		Optional, the value of 'filter' GET parameter, used to generate pager
		URLs.

	filteredCount
		Pass the count of elements matching the filters in the whole query.

	objs
		Pass your objects as List<Map<String,Object>>. It can be generated by
		JdbcTemplate or using commons-beanutils BeanMap.

	page
		Pass the current page number (starting with 1), it is needed to generate
		pager URLs.

	pages
		Pass the number of pages, it is needed to generate pager URLs.

############################################################################ -->

<#if !allCount?? || !count?? || !counts?? || !filteredCount?? || !objs?? || !page?? || !pages??>
	<p class="text-danger">
		Some of these model attributes are missing: allCount, count, counts, filteredCount, objs, page, pages.
	</p>
<#else>

<#if filter??>
	<#assign removeFilter>/notices/${count}/1</#assign>
<#else>
	<#assign removeFilter = "#" />
</#if>

<#-- NAVBAR ---------------------------------------------------------------- -->

<div class="row">
	<nav class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#details-navbar">
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
			</div>
			<div class="collapse navbar-collapse" id="details-navbar">
				<ul class="nav navbar-nav">
					<li><a href="#">
						<#if filteredCount != allCount>${filteredCount} / </#if>
						${allCount} <@label "notices.count" />
					</a></li>

					<li class="dropdown">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false"><@label "notices.order.menu" /> <span class="caret"></span></a>
						<ul class="dropdown-menu" role="menu">
							<li><a href="/notices/10/1/by-date<#if filter?? && filter?length &gt; 0>?filter=${filter}</#if>">
								<span class="glyphicon glyphicon-<#if order?? && order == "by-date">check<#else>unchecked</#if>"></span>
								&nbsp;
								<@label "notices.order.byDate" />
							</a></li>
							<li><a href="/notices/10/1/by-flags<#if filter?? && filter?length &gt; 0>?filter=${filter}</#if>">
								<span class="glyphicon glyphicon-<#if order?? && order == "by-flags">check<#else>unchecked</#if>"></span>
								&nbsp;
								<@label "notices.order.byFlags" />
							</a></li>
						</ul>
					</li>

				</ul>
				<ul class="nav navbar-nav navbar-right">
					<li>
						<a href="/filters"><@label "filters.list" /></a>
					</li>
					<li>
						<a id="filterLink" data-toggle="collapse" href="#filterPanel"
							aria-expanded="<#if filter??>true<#else>false</#if>" aria-controls="collapseExample">
							<@label "filters" />
						</a>
					</li>
					<#--
					<form class="navbar-form navbar-left" role="search">
						<div class="input-group">
							<span class="input-group-addon" data-toggle="tooltip" data-placement="top" title="<@spring.messageText "notices.filter.hint" "notices.filter.hint" />"><span class="glyphicon glyphicon-filter"></span></span>
							<input id="filter" name="filter" <#if filter?? && filter?length &gt; 0>value="${filter}"</#if> type="text" class="form-control" placeholder="<@spring.messageText 'list.filter', '' />" />
							<span class="input-group-btn">
								<a class="btn btn-default" href="${removeFilter}"><span class="glyphicon glyphicon-remove"></span></a>
							</span>
						</div>
						<button type="submit" style="display:none">Submit</button>
					</form>
					-->
				</ul>
			</div>
		</div>
	</nav>
</div>

<div class="collapse <#if filter?? && filter?length &gt; 0>in</#if>" id="filterPanel"><div class="well">
	<form class="form-horizontal" method="GET">
		<#-- <input type="hidden" name="filter" value="${filter!""}" /> -->
		<input type="hidden" name="order" value="${order!""}" />
    	<fieldset>

	    	<div class="form-group col-sm-6 col-lg-4 <#if filters["doc"]?? && filters["doc"]?length &gt; 0>has-success</#if>"><div class="col-md-11 input-group">
	    		<span class="input-group-addon" data-toggle="tooltip" data-placement="top" title="<@label "notices.filter.doc.hint" "" />"><span class="glyphicon glyphicon-filter"></span></span>
	    		<select class="form-control" name="doc">
					<option value=""><@label "notices.filter.doc.all" /></option>
					<#if docTypes??>
						<#list docTypes as dt>
						    <#-- <option value="${dt.id}" <#if filters["doc"]?? && dt.id == filters["doc"]>selected</#if>><@label "notice.data.documentType.${dt.id}" dt.name /></option> -->
						</#list>
					</#if>
				</select>
			</div></div>

    		<#assign f = [ "contr", "winner", "cpv", "date", "value", "text", "flags" ] />
    		<#list 0..f?size-1 as i>
    			<#assign n = f[i] />
    			<div class="form-group col-sm-6 col-lg-4 <#if filters[n]?? && filters[n]?length &gt; 0>has-success</#if>"><div class="col-md-11 input-group">
					<span class="input-group-addon" data-toggle="tooltip" data-placement="top" title="<@label "notices.filter.${n}.hint" "" />"><span class="glyphicon glyphicon-filter"></span></span>
					<input class="form-control" type="text" name="${n}" placeholder="<@label "notices.filter.${n}.placeholder" />" <#if filters[n]??>value="${filters[n]}"</#if> />
	    		</div></div>
    		</#list>

			<div class="form-group col-sm-6 col-lg-4 <#if filters["indicators"]?? && filters["indicators"]?length &gt; 0>has-success</#if>"><div class="col-md-11 input-group">
				<span class="input-group-addon" data-toggle="tooltip" data-placement="top" title="<@label "notices.filter.indicators.hint" "" />"><span class="glyphicon glyphicon-filter"></span></span>
				<select class="form-control" size="1" name="indicators" id="indicator-filter" multiple title='<@label "notices.filter.indicators.placeholder" />' data-selected-text-format="count>0" data-live-search="true" data-width="100%">
					<#list indicators as i>
						<#assign sel = false />
						<#if filters["indicators"]??>
							<#list filters["indicators"]?split(",") as fi>
								<#if i == fi>
									<#assign sel = true />
									<#break>
								</#if>
							</#list>
						</#if>
						<option value="${i}" <#if sel == true>selected</#if>><@label indName(i) /></option>
					</#list>
				</select>
			</div></div>

    		<div class="form-group col-xs-12 text-center">
    			<input class="btn btn-primary" type="submit" name="doFilter" value="<@label "filter.submit" />" />
    			&nbsp;&nbsp;
    			<input class="btn btn-success" type="submit" name="saveFilter" value="<@label "notices.filter.save" />" />
    			&nbsp;&nbsp;
    			<a class="btn btn-default" href="${removeFilter}"><@label "filter.clear" /></a>
    		</div>
    	</fieldset>
    </form>
</div></div>

<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.7.5/css/bootstrap-select.min.css" />
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.7.5/js/bootstrap-select.min.js"></script>

<script type="text/javascript">
$(function() {
	$("#indicator-filter").html($("#indicator-filter option").sort(function (a, b) {
		return a.text == b.text ? 0 : a.text < b.text ? -1 : 1
	}));

	$('#indicator-filter').selectpicker({
		countSelectedText: '<@label "notices.filter.indicators.selected" />',
		style: ''
	});


	$('input[name="date"]').daterangepicker({
		format: 'YYYY-MM-DD',
		startDate: moment().subtract(29, 'days'),
		endDate: moment(),
		minDate: '2012-06-01',
		maxDate: '2015-12-31',
		showDropdowns: true,
		ranges: {
			'<@label "filter.date.prev7days" />': [moment().subtract(6, 'days'), moment()],
			'<@label "filter.date.prev30days" />': [moment().subtract(29, 'days'), moment()],
			'<@label "filter.date.thisMonth" />': [moment().startOf('month'), moment().endOf('month')],
			'<@label "filter.date.lastMonth" />': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
			'<@label "filter.date.thisYear" />': [moment().startOf('year'), moment().endOf('year')],
			'<@label "filter.date.lastMonth" />': [moment().subtract(1, 'year').startOf('year'), moment().subtract(1, 'year').endOf('year')]
		},
		opens: 'left',
		drops: 'down',
		buttonClasses: ['btn', 'btn-sm'],
		applyClass: 'btn-primary',
		cancelClass: 'btn-default',
		separator: ':',
		locale: {
			applyLabel: '<@label "filter.date.apply" />',
			cancelLabel: '<@label "filter.date.cancel" />',
				fromLabel: '<@label "filter.date.from" />',
				toLabel: '<@label "filter.date.to" />',
				customRangeLabel: '<@label "filter.date.custom" />',
				daysOfWeek: [<@label "filter.date.days" />],
				monthNames: [<@label "filter.date.months" />],
				firstDay: 1
			}
	});
});
</script>

<#if filtersList?? && filtersList?size &gt; 0><div class="row"><p class="col-md-10 col-md-offset-1 readable">${readableFilter(filtersList)}</p></div></#if>
<!-- ${filter} -->

<#-- TABLE ----------------------------------------------------------------- -->

<#if 0 == objs?size>
	<p class="text-center text-primary notfound"><strong><@label 'notices.empty', '' /></strong></p>
</#if>

<div class="row"><table class="table table-striped col-md-12 list-table">
	<#list objs as obj>
		<tr>
			<td class="col-md-1 text-right">
				<strong><a href="/notice/${obj.id}">${obj.id}</a></strong>

				<br/>

				${obj.date?date}
			</td>
			<td class="col-md-8 hidden-sm hidden-xs" data-toggle="tooltip" data-placement="bottom" data-container="body" <#if obj.title??>title="${obj.title}"</#if>>
				<#-- <#if obj.type??><strong>${obj.type}&nbsp;</strong></#if> -->
				<#if obj.typeId??><strong><@label "notice.data.documentType.${obj.typeId?split('-')[1]}" />&nbsp;</strong></#if>
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

				<br/>

				<#if obj.contractingOrgName?? && obj.contractingOrgId??>
					<@label "notices.contractingOrg" />
					<a href="/organization/${obj.contractingOrgId}">${obj.contractingOrgName}</a>
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
				<span class="visible-sm visible-xs text-left"><#if obj.typeId??><strong><@label "notice.data.documentType.${obj.typeId?split('-')[1]}" />&nbsp;</strong></#if></span>

				<#if obj.estimated?? && obj.estimatedCurr??>
					<span data-toggle="tooltip" data-placement="bottom" data-container="body" title="<@label "notice.obj.estimatedValue" />">
					${(obj.estimated/1000000)?string["0.#"]}&nbsp;M&nbsp;${obj.estimatedCurr}
					</span>
				</#if>

				<#if obj.total?? && obj.totalCurr??>
					<span data-toggle="tooltip" data-placement="bottom" data-container="body" title="<@label "notice.obj.totalFinalValue" />">
					${(obj.total/1000000)?string["0.#"]}&nbsp;M&nbsp;${obj.totalCurr}
					</span>
				</#if>
			</td>
			<td class="col-md-2 text-center">
				<@smallFlags obj.flags />
			</td>
		</tr>
	</#list>
</table></div>

<#-- PAGER ----------------------------------------------------------------- -->

<#assign urlFormat>/notices/$C/$P/${order}<#if filter?? && filter?length &gt; 0>?filter=${filter}</#if></#assign>
<@pager pages page counts count urlFormat "$P" "$C" />

<!-- Query time: ${queryTime} ms -->

</#if>

<#include "/includes/footer.ftl">