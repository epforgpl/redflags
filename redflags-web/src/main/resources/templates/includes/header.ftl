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

<#-- ###########################################################################

Model:

	appTitle
		Title of the application.

	pageTitle
		You can pass here the title of the page. It will be shown in the window
		header as "pageTitle - appTitle".

	pageTitleLabel
		You can pass the label name to be used as page title. This attribute
		sets and overrides the value of 'pageTitle'.

	prevPageTitleLabel
		You can pass the label name to be used as the title of the previous
		page.

	prevPageUrl
		You can pass the url of the previous page.

############################################################################ -->

<#if pageTitleLabel??>
	<#assign pageTitle><@label pageTitleLabel "" /></#assign>
</#if>
<#if prevPageUrl?? && prevPageTitleLabel??>
	<#assign pageTitle>
		<a href="${prevPageUrl}"><@label prevPageTitleLabel "" /></a>
		/
		${pageTitle}
	</#assign>
</#if>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="keywords" content="<@label "meta.keywords" />" />
	<meta property="og:description" name="description" content="<@label "meta.description" />" />
	<meta property="og:site_name" content="Red Flags"/>
	<meta property="og:image" content="http://www.redflags.eu/img/redflags-logo-official.png"/>
	<title><#if pageTitle?? && "" != pageTitle>${pageTitle?replace("<[^>]+>", "", "r")} - </#if>${appTitle}</title>
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/bootswatch/3.3.5/lumen/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css" />
	<link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,400italic,300,700&subset=latin,latin-ext" />
	<#if needDateRangePicker??>
		<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-daterangepicker/1.3.23/daterangepicker-bs3.min.css" />
	</#if>
	<link rel="stylesheet" type="text/css" href="/css/app.css" />
	<link rel="shortcut icon" href="/img/favicon.ico" />
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
	<script type="text/javascript">
		(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	 	(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
		m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
		})(window,document,'script','//www.google-analytics.com/analytics.js','ga');
		ga('create', 'UA-40698821-2', 'auto');
		ga('create', 'UA-70253429-1', 'auto', 'km');
		ga('send', 'pageview');
		ga('km.send', 'pageview');
	</script>
</head>
<body>
	<#if springMacroRequestContext.requestUri == "/">
	<div id="fb-root"></div>
	<script type="text/javascript">
		(function(d, s, id) {var js, fjs = d.getElementsByTagName(s)[0];
		if (d.getElementById(id)) return;js = d.createElement(s); js.id = id;
		js.src = "//connect.facebook.net/hu_HU/sdk.js#xfbml=1&version=v2.5";
		fjs.parentNode.insertBefore(js, fjs);}(document, 'script', 'facebook-jssdk'));
	</script>
	</#if>

	<div id="header-outer-left"></div>
	<div id="header-outer-right"></div>
	<div id="header">
		<div class="container">
			<div id="header-table">
				<div id="logo"><a href="/"></a></div>

				<div id="header-nav" class="animated fadeInDown">
					<div class="row">
						<ul class="menu hidden-xs hidden-sm visible-md visible-lg">
							<li <#if springMacroRequestContext.requestUri == "/">class="current"</#if>><a href="/"><@label "menu.root" /></a></li>
							<li <#if springMacroRequestContext.requestUri?contains("/notice")>class="current"</#if>><a href="/notices"><@label "notices.title" /></a></li>
							<li <#if springMacroRequestContext.requestUri?contains("/organization")>class="current"</#if>><a href="/organizations"><@label "organizations.title" /></a></li>
						</ul>
					</div>
					<#if springMacroRequestContext.requestUri == "/">
						<div class="row">
							<ul id="nav" class="menu submenu hidden-xs hidden-sm visible-md visible-lg">
								<li <#if springMacroRequestContext.requestUri == "/">class="current"</#if>><a href="/#about"><@label "about.heading" /></a></li>
								<li><a href="/#stats"><@label "charts.heading" /></a></li>
								<li><a href="/#features"><@label "features.heading" /></a></li>
								<li><a href="/#contact"><@label "contact.heading" /></a></li>
							</ul>
						</div>
					</#if>
				</div>

				<div id="header-right" class="animated fadeInDown">
					<#--
					<span class="badge opacity"><span class="glyphicon glyphicon-tag"></span>&nbsp;&nbsp;v${version}</span>
					<span class="badge opacity badge-debug"><span class="glyphicon glyphicon-cog"></span>&nbsp;&nbsp;build: ${built}</span>
					<#if queryTime??>
						<span class="badge opacity badge-debug"><span class="glyphicon glyphicon-time"></span>&nbsp;&nbsp;${queryTime}ms</span>
					</#if>
					-->
					
					<#list languages as e>
						<#if lang?? && lang != e>
							<a href="?lang=${e}" data-toggle="tooltip" data-placement="bottom" title="<@label "lang.${e}" />"
							><span class="badge opacity"><i class="fa fa-language"></i> ${e?upper_case}</span></a>
						</#if>
					</#list>
					
					<#if Session["SPRING_SECURITY_CONTEXT"]?exists>
						<#if Session["SPRING_SECURITY_CONTEXT"].authentication.name??>
							<a href="/logout"><span class="badge opacity"><@label "logout" /></span></a>
							<strong>
								${Session["SPRING_SECURITY_CONTEXT"].authentication.principal.account.name}&nbsp;&nbsp;<span class="glyphicon glyphicon-user"></span>
							</strong>
						</#if>
					<#else>
						<a href="/login"><span class="badge opacity"><@label "login" /></span></a>
					</#if>
				</div>
			</div>
		</div>
	</div> <!-- header -->

	<div id="content">
		<div class="container">
			<noscript>
				<div class="alert alert-danger" id="noscript-alert">
				  <@label "noscript" />
				</div>
			</noscript>
			
			<#if siteMessage?? && siteMessage?length &gt; 0 >
				<div class="col-md-10 col-md-offset-1 well well-sm text-danger text-center">
					<strong><@label siteMessage /></strong>
				</div>
			</#if>

			<#if pageTitle??>
				<div class="row">
					<h1>${pageTitle}</h1>
				</div>
			</#if>