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

<#assign needHiCharts></#assign>
<#include "/includes/header.ftl">

<div class="row section-wrapper" id="about">
	<div class="col-md-6 col-md-push-6">
		<div class="jumbotron">
			<h1><@label "browse.heading" /></h1>
			<p class="text-justify"><@label "browse.description" /></p>
			<p>
			<a class="btn btn-primary btn-lg" href="/notices"><span class="glyphicon glyphicon-folder-open"></span>&nbsp;&nbsp;&nbsp;<@label "notices.title" /> »</a>
			<a class="btn btn-primary btn-lg" href="/organizations"><span class="glyphicon glyphicon-folder-open"></span>&nbsp;&nbsp;&nbsp;<@label "organizations.title" /> »</a>
			</p>
		</div>
	</div>

	<div class="col-md-6 col-md-pull-6">
		<h1><@label "about.heading" /></h1>
		<p class="text-justify">
			<#include "/about_${lang}.html">
		</p>
	</div>
</div>

<div class="row section-wrapper" id="stats">
	<div class="col-md-12">
		<h1><@label "charts.heading" /></h1>
	</div>
</div>

<div class="row">
	<div class="col-md-8 col-md-offset-2"><div class="well">
		<div id="charts-carousel" class="owl-carousel">
			<div class="item"><div class="chart" id="flagCountDonut"></div></div>
			<div class="item"><div class="chart" id="flaggedCountArea"></div></div>
			<div class="item"><div class="chart" id="sumValueBars"></div></div>
		</div>
		<div id="chart-buttons">
			<div class="chart-button opacity" id="chart-1-button"></div>
			<div class="chart-button opacity" id="chart-2-button"></div>
			<div class="chart-button opacity" id="chart-3-button"></div>
		</div>
	</div></div>
</div>

<div class="row section-wrapper" id="features">
	<div class="col-md-12">
		<h1><@label "features.heading" /></h1>
	</div>
</div>

<div class="row">
	<div class="col-xs-12 col-md-10 col-md-offset-1">
		<div class="col-xs-10 col-xs-offset-1 col-md-4 col-md-offset-0">
			<div class="well well-lg feature-box" id="filter-feature-box">
				<h1 class="text-center"><span class="glyphicon glyphicon-search"></span></h1>
				<p class="text-center"><@label "features.filter" /></p>
			</div>
		</div>
		<div class="col-xs-10 col-xs-offset-1 col-md-4 col-md-offset-0">
			<div class="well well-lg feature-box" id="subscribe-feature-box">
				<h1 class="text-center"><span class="glyphicon glyphicon-envelope"></span></h1>
				<p class="text-center"><@label "features.subscribe" /></p>
			</div>
		</div>
		<div class="col-xs-10 col-xs-offset-1 col-md-4 col-md-offset-0">
			<div class="well well-lg feature-box" id="update-feature-box">
				<h1 class="text-center"><span class="glyphicon glyphicon-refresh"></span></h1>
				<p class="text-center"><@label "features.update" /></p>
			</div>
		</div>
	</div>
</div>

<div class="row section-wrapper" id="contact">
	<div class="col-md-12">
		<h1><@label "contact.heading" /></h1>
	</div>
</div>

<div class="row">
	<div class="col-xs-12">
		<p class="lead"><@label "contact.text" /></p>
		<p class="lead text-center"><br/><strong>info @ redflags . eu</strong></p>
	</div>
</div>

<script>
var green = '#95D8D1';
var red = '#EA465F';

function flaggedNoticeChart(data) {
	$('#flaggedCountArea').highcharts({
		chart: { backgroundColor: null, type: 'areaspline' },
		credits: { enabled: false },
		exporting: {
			chartOptions: {
				plotOptions: {
					series: {
						dataLabels: { enabled: true }
					}
				}
			}
		},
		series: [{
			color: green,
			name: '<@label "charts.notices" />',
			data: data.noticeCounts
		}, {
			color: {
				linearGradient: { x1: 0, x2: 0, y1: 0, y2: 1 },
				stops: [ [0, green], [1, red] ]
			},
			name: '<@label "charts.flaggedNotices" />',
			data: data.flaggedNoticeCounts
		}],
		title: { text: '<@label "charts.rateOfFlaggedNotices" />' },
		tooltip: { shared: true },
		xAxis: { categories: data.categories },
		yAxis: {
			title: { text: '<@label "charts.noticeCount" />' },
			labels: {
				formatter: function() {
					return this.value;
				}
			}
		}
	});
}

function flagCountDonut(data) {
	var rainbow = new Rainbow();
	rainbow.setNumberRange(0, Object.keys(data.flagCountFrequency).length - 1);
	rainbow.setSpectrum(green, red);

	var outer = [];
	var i = 0;
	for(var fc in data.flagCountFrequency) {
		var fq = data.flagCountFrequency[fc];
		var col = rainbow.colourAt(i);
		outer.push({
				name: fc+' <@label "charts.flag" />',
				y: fq,
				color: '#' + col
			});
		i++;
	}

//if (0 == this.point.series.index) {
//	return this.point.y;
//} else if (1 == this.point.series.index) {
//	return 0 == this.point.name[0] ? '<@label "charts.withoutFlag" />' : this.point.name + ' x ' + this.point.y;
//}


	$('#flagCountDonut').highcharts({
		chart: { backgroundColor: null, type: 'pie' },
		credits: { enabled: false },
		exporting: {
			chartOptions: {
				legend: { enabled: true },
				plotOptions: {
					series: {
						dataLabels: { format: "{point.name} × {point.y}" }
					}
				}
			}
		},
		title: { text: '<@label "charts.distOfFlagCounts" />' },
		tooltip: {
			headerFormat: '<b>{point.key}</b><br/>',
			pointFormat: '{point.y} <@label "charts.notice" />'
		},
		series: [{
			size: '45%',
			data: [{
				name: '<@label "charts.unflaggedNotices" />',
				y: data.notFlaggedCount,
				color: green
			}, {
				name: '<@label "charts.flaggedNotices" />',
				y: data.flaggedCount,
				color: {
					linearGradient: { x1: 0, x2: 0, y1: 0, y2: 1 },
					stops: [ [0, red], [1, green] ]
				}
			}],
			dataLabels: {
				formatter: function() { return ""; },
				distance: -30
			}
		}, {
			size: '80%',
			innerSize: '60%',
			data: outer,
			dataLabels: {
				formatter: function() {
					return 0 == this.point.name[0] ? '<@label "charts.withoutFlag" />' : this.point.name;
				}
			}
		}]
	});
}

function sumValueBars(data) {
	data = data.replace('NC-1', '<@label "charts.NC-1" />');
	data = data.replace('NC-2', '<@label "charts.NC-2" />');
	data = data.replace('NC-4', '<@label "charts.NC-4" />');
	$('#sumValueBars').highcharts({
		chart: { backgroundColor: null, type: 'column' },
		colors: [ '#95D8D1', '#85C0BB', '#76A8A6' ],
		credits: { enabled: false },
		data: { csv: data },
		exporting: {
			chartOptions: {
				plotOptions: {
					legend: {
						enabled: true
					},
					series: {
						dataLabels: {
							enabled: true,
							format: "{y:.2f}"
						}
					}
				}
			}
		},
		title: { text: '<@label "charts.sumValues" />' },
		tooltip: {
			shared: true,
			valueDecimals: 2,
			valueSuffix: ' <@label "charts.billionHUF" />'
		},
		yAxis: { title: { text: '<@label "charts.sumValueAxis" />' } }
	});
}

$(function() {
	$("#charts-carousel").owlCarousel({
		navigation: false,
		pagination: false,
		slideSpeed: 300,
		singleItem: true
	});
	var owl = $("#charts-carousel").data('owlCarousel');

	function refreshChartButtons(a) {
		for(i = 1; i <= 3; i++) {
			$("#chart-" + i + "-button").toggleClass("active", a == i);
		}
	}
	refreshChartButtons(1);

	for(i = 1; i <= 3; i++) {
		$("#chart-" + i + "-button").click(function() {
			s = this.id.replace(/\D+/g,"");
			owl.goTo(s - 1);
			refreshChartButtons(s);
		});
	}

	$.ajax({
		url: '/chart/flagCounts', type: 'GET', async: true,	dataType: "json",
		success: function(data) { flagCountDonut(data); }
	});
	$.ajax({
		url: '/chart/flaggedNotices', type: 'GET', async: true, dataType: "json",
		success: function(data) { flaggedNoticeChart(data); }
	});
	$.ajax({
		url: '/chart/sumValues', type: 'GET', async: true,
		success: function(data) { sumValueBars(data); }
	});
});
</script>

<!-- jatek -->

<#if "hu" == lang>
<div class="modal fade" id="jatek" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				<h4 class="modal-title" id="myModalLabel">Redflags.hu - Merülj el benne és <strong>nyerj!</strong></h4>
			</div>
			<div class="modal-body">
				<p class="text-justify"><em>
					Február 1-jén elindult a <strong><a href="https://www.facebook.com/TransparencyInternationalMagyarorszag/" target="_blank">
					Transparency International Magyarország</a></strong> és a
					<strong><a href="https://www.facebook.com/Kmonitor/" target="_blank">
					K-Monitor</a></strong> közös játéka.
				</em></p>
				<p class="text-justify">
					Minden héten 3 kérdést fogtok találni felváltva a TI és a
					K-Monitor Facebook oldalán.
				</p>
				<p class="text-justify">
					<strong>15. találós kérdésünk a következő:</strong>
				</p>
				<p class="text-justify">
					Tudod milyen az, amikor nagyon sürgősen fel kell újítani egy
					kastélyt? A <strong><a href="http://444.hu/2014/10/17/hogy-nez-ki-egy-igazan-gyanus-kozbeszerzes-pontosan-igy/" target="_blank">444.hu cikkéből
					</a></strong> kiderül, hogyan is nézett ki a
					keszthelyi Festetics-kastély felújítási pályázata.
				</p>
				<p class="text-justify">
					Keresd meg a cikkben szereplő közbeszerzési eljárást a
					Redflags adatbázisában, és a link megküldésével együtt írd
					meg nekünk, hogy miért olyan gyanús ez a közbeszerzési kiírás!
				</p>
				<p class="text-justify">
					A megfejtéseket <strong>március 6-án éjfélig</strong> várjuk
					a <strong><a href="mailto:jatek@redflags.eu">jatek@redflags.eu</a></strong>
					email címre.
					A helyes megfejtők között egy <strong><a href="http://www.symatoys.com/product/show/1918.html" target="_blank">
					mini drónt</a></strong> sorsolunk ki, az első 5 leggyorsabb
					megfejtő pedig pólót kap ajándékba. 
					Egy kis segítség az oldal használatához:
				</p>
				<div class="embed-responsive embed-responsive-16by9">
					<iframe src="https://www.youtube.com/embed/v3Xco8832RY" frameborder="0" allowfullscreen></iframe>
				</div>
			</div>
			<div class="modal-footer">
				<a href="/notices" class="btn btn-primary">Hirdetménykereső</a>
			</div>
		</div>
	</div>
</div>
<script>$(function(){ $('#jatek').modal('show'); });</script>
</#if>


<#include "/includes/footer.ftl">