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
<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/bootswatch/3.3.5/lumen/bootstrap.min.css" />
	<link rel="stylesheet" type="text/css" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,400italic,300,700&subset=latin,latin-ext" />
	<link rel="stylesheet" type="text/css" href="/css/app.css" />
	<link rel="shortcut icon" href="/img/favicon.ico" />
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
	<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.5/js/bootstrap.min.js"></script>
	<title><#if title??><@spring.messageText title "" /> - </#if>Red Flags API</title>
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
				<a href="/" id="logo"></a>
				
				<div id="header-nav" class="animated fadeInDown">
					<div class="row">
						<ul id="nav" class="menu">
							<li <#if springMacroRequestContext.requestUri == "/">class="current"</#if>><a href="/">About the project</a></li>
							<li <#if springMacroRequestContext.requestUri?contains("/registration")>class="current"</#if>><a href="/registration">
								<span>
									Registration
								</span>
							</a></li>
							<li><a href="http://api-docs.redflags.eu/">
								<span>
									Documentation
								</span>
							</a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div id="content">
		<div class="container">