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
 
		<#if springMacroRequestContext.requestUri == "/"><#-- fb script in header! -->
		<div class="row fblike-row">
			<div class="col-xs-12 text-center">
				<div class="fb-like" data-href="http://www.redflags.eu/" data-layout="standard" data-action="like" data-show-faces="false" data-share="true"></div>
			</div>
		</div>
		</#if>
		
	</div><#-- /container -->
	
</div><#-- content -->

<p class="text-center license">
	<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0;margin-bottom:10px" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png"></a>
	<br>
	This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><b>Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</b></a>.
</p>

<div class="footer">
	<div class="container text-center logos">	
		<a href="http://ec.europa.eu/dgs/home-affairs/" target="_blank"><img src="/img/eu-logo.png" alt="European Commission" width="53" height="35" /></a>		
		<a href="http://k-monitor.hu/" target="_blank"><img src="/img/kmonitor-logo.png" alt="K-Monitor" width="111" height="35" /></a>
		<a href="http://petabyte-research.org/" target="_blank"><img src="/img/petabyte-logo.png" alt="Petabyte" width="88" height="35" /></a>			
		<a href="http://transparency.hu/" target="_blank"><img src="/img/transparency-international-logo.png" alt="Transparency International" width="120" height="35" /></a>
		<a href="http://epf.org.pl/en/" target="_blank"><img src="/img/epanstwo.png" alt="ePanstwo Foundation" height="35" /></a> 
		<a href="http://transparencee.org/" target="_blank"><img src="/img/transparencee.png" alt="TransparenCEE network" height="35" /></a> 	
	</div>
</div>

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>

<#if springMacroRequestContext.requestUri == "/">
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery-one-page-nav/3.0.0/jquery.nav.min.js"></script>
</#if>

<#if needDateRangePicker??>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/1.3.23/moment.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/1.3.23/daterangepicker.min.js"></script>
</#if>

<#if needReCaptcha??>
	<script type="text/javascript" src="//www.google.com/recaptcha/api.js?onload=CaptchaCallback&render=explicit" async defer></script>
</#if>

<#if needHiCharts??>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/highcharts/4.1.9/highcharts.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/highcharts/4.1.9/modules/data.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/highcharts/4.1.9/modules/exporting.js"></script>
	<script type="text/javascript" src="/js/rainbowvis.min.js"></script>
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/owl-carousel/1.3.3/owl.carousel.min.css" />
	<script src="https://cdnjs.cloudflare.com/ajax/libs/owl-carousel/1.3.3/owl.carousel.min.js"></script>
	<script type="text/javascript">
		Highcharts.setOptions({		
			lang: {
				printChart: '<@label "highcharts.printChart" />',
				downloadJPEG: '<@label "highcharts.downloadJPEG" />',
				downloadPDF: '<@label "highcharts.downloadPDF" />',
				downloadPNG: '<@label "highcharts.downloadPNG" />',
				downloadSVG: '<@label "highcharts.downloadSVG" />'
			}
		});
	</script>
</#if>
<script type="text/javascript">

	// cookie consent
	window.cookieconsent_options = {
		theme: "dark-floating-tada",
    	message: "<@label 'cookie.consent.message' />",
    	dismiss: "<@label 'cookie.consent.dismiss' />",
		learnMore: null,
		link: null
	};
	
	// rf init
	$(function () {	
		$('[data-toggle="tooltip"]').tooltip();
		$('.flag-info-icon').popover();  
		$('#filter').focus();
		if (location.href.indexOf("#") != -1 && location.href.indexOf("filter=") == -1) { $('#filterLink').click(); }
		
		<#if springMacroRequestContext.requestUri == "/">
			$("#nav").onePageNav({ changeHash: true });
			$("#filter-feature-box").click(function(){location.href="/notices#"});
			$("#subscribe-feature-box").click(function(){location.href="/filters"});
			$("#update-feature-box").click(function(){location.href="/notices"});
			$("#api-feature-box").click(function(){location.href="http://api.redflags.eu/"});
		</#if>
	});
</script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/cookieconsent2/1.0.9/cookieconsent.min.js"></script>

</body>
</html>
