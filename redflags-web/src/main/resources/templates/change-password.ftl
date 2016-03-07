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
 
<#assign needReCaptcha></#assign>
<#include "/includes/header.ftl">

<#assign messageLevel>${messageLevel!0}</#assign>
<#if 0  ==  messageLevel?number><#assign textClass = "primary" /></#if>
<#if 0 &gt; messageLevel?number><#assign textClass = "danger"  /></#if>
<#if 0 &lt; messageLevel?number><#assign textClass = "success" /></#if>

<#if messageLabel??>
	<div class="row">
		<div class="col-md-10 col-md-offset-1">
			<div class="well text-${textClass}">			
				<strong>
					<@label messageLabel />
					<#if email??>(${email})</#if>
				</strong>
			</div>
		</div>
	</div>
<#else>	
	<div class="row">
		<div class="col-md-8 col-md-offset-2">		
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4 class="panel-title">						
						<span class="glyphicon glyphicon-pencil"></span>
						&nbsp;&nbsp;
						<@label "change" />
						</a>
					</h4>
				</div>
				<div class="panel-body">
					<form class="form-horizontal" action="/change-password" method="post">
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						<input type="hidden" name="id" value="${id}" />
						<input type="hidden" name="token" value="${token}" />											
						<div class="col-md-6 col-md-offset-3">
							<div class="form-group">
								<input type="email" class="form-control" name="email" value="${email}" disabled />
							</div>
							<div class="form-group">
								<input type="password" class="form-control" name="password"  placeholder="<@label "change.password" />" required />
							</div>
							<div class="form-group">
								<input type="text" class="form-control" name="name" value="${name}" disabled />
							</div>	
						</div>	
						<div class="col-md-6 col-md-offset-3">
							<div class="form-group">
								<div class="g-recaptcha" id="changeCaptcha"></div>
							</div>
						</div>				
						<div class="col-md-6 col-md-offset-3 text-center">
							<div class="form-group">		
								<button type="submit" class="btn btn-primary">
									<@label "change.submit" />
									&nbsp;
									<span class="glyphicon glyphicon-pencil"></span>						
								</button>
							</div>
						</div>				
					</form>					
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	    var CaptchaCallback = function(){
	    	var sk = '6LdI6wYTAAAAAOX4tE-4x2R50o0DTpaQt17JcEcB';
	    	grecaptcha.render('changeCaptcha', {'sitekey' : sk});
	    };
	</script>
</#if>

<#include "/includes/footer.ftl">