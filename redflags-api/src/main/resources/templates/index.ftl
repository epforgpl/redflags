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
<#include "/header.ftl">

<div class="row">
	<div class="col-md-6">
		<h1>
			RedFlags API
		</h1>
		<p class="text-justify">
			The purpose of the <span style="font-weight:bold;color:#BC2A52">Red Flags API</span> is to <strong>make the data collected by the RedFlags project available</strong> in a structured and automatically processable format outside the <a href="http://www.redflags.eu" target="_blank">RedFlags web page</a>. 
		</p><p class="text-justify">
			The API provides information about the <strong>collected notices and organizations in JSON format</strong>. You can access the service through HTTP requests, and the query parameters must be placed in the GET parameters of each request. You can find more information about the specific requests and their usage in the <a href="http://api-docs.redflags.eu/" target="_blank">Red Flags API documentation</a>.
			
		</p><p class="text-justify">
				In order to use <span style="font-weight:bold;color:#BC2A52">Red Flags API</span> <strong>you need to register</strong> by filling the <a href="/registration">registration form</a>. 
				As a result you will receive an e-mail containing your unique API key. 
				You must <strong>place this key into each request as a GET parameter</strong>. The API key provides your personal access to the service, do not share it with others.
		</p>
		<p class="text-center">
			<a class="btn btn-primary" href="/registration">Registration</a>
			<a class="btn btn-primary" href="http://api-docs.redflags.eu/">Documentation</a>
		</p>
	</div>
	<div class="col-md-5 col-md-offset-1">
		<h1>
			About the project
		</h1>
		<p class="text-justify">
		The <span style="font-weight:bold;color:#BC2A52">Red Flags</span> project aims
		to enhance the transparency of public procurements and
		<strong>support the fight against corrupt procurements.</strong> It provides an
		interactive tool that allows the monitoring of procurement processes and their
		implementation by citizens, journalists or even public officials and catch fraud
		risks at different stages of the procurement process.
		The <span style="font-weight:bold;color:#BC2A52">Red Flags</span> tool
		automatically checks procurement documents from the Tenders Electronic Daily
		(TED) and <strong>filters risky procurements</strong> through a special
		algorithm. Although risky does not mean corrupt, flagged procurement documents
		are worth checking.
		</p>
		<p class="text-center"><a class="btn btn-primary" href="http://www.redflags.eu/">Read more</a></p>
	</div>
</div>

<div class="row">
	<div class="col-xs-12">
		<h1>Contact</h1>
		<p class="lead">If you find a mistake or you have any questions, contact us here:</p>
		<p class="lead text-center"><br/><strong>info @ redflags . eu</strong></p>
	</div>
</div>


<#include "/footer.ftl">