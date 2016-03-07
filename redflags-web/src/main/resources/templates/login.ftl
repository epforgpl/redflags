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
<#assign mode>${mode!"login"}</#assign>

<#if RequestParameters.error??>
	<#assign messageLabel = "login.error" />
	<#assign messageLevel = -1 />
</#if>
<#assign messageLevel>${messageLevel!0}</#assign>
<#if 0  ==  messageLevel?number><#assign textClass = "primary" /></#if>
<#if 0 &gt; messageLevel?number><#assign textClass = "danger"  /></#if>
<#if 0 &lt; messageLevel?number><#assign textClass = "success" /></#if>

<#assign messageLabel>${messageLabel!"login.info"}</#assign>
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
</#if>

<div class="row">
	<div class="col-md-8 col-md-offset-2">
		<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
			<div class="panel panel-default">
				<div class="panel-heading" role="tab" id="login-panel-heading">
					<h4 class="panel-title">
						<a data-toggle="collapse" data-parent="#accordion" href="#login-panel" aria-expanded="<#if mode == "login">true<#else>false</#if>" aria-controls="login-panel">
						<span class="glyphicon glyphicon-lock"></span>
						&nbsp;&nbsp;
						<@label "login" />
						</a>
					</h4>
				</div>
				<div id="login-panel" class="panel-collapse collapse <#if mode == "login">in</#if>" role="tabpanel" aria-labelledby="login-panel-heading">
					<div class="panel-body">
						<form class="form-horizontal" action="/login" method="post">
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
							<div class="col-md-6 col-md-offset-3">
								<div class="form-group">								
									<input type="email" class="form-control" name="username" placeholder="<@label "login.email" />" value="<#-- teszt@redflags.eu -->" required />
								</div>							
								<div class="form-group">								
									<input type="password" class="form-control" name="password"  placeholder="<@label "login.password" />" value="<#-- teszt1234 -->" required />
								</div>
							</div>
							<div class="col-md-6 col-md-offset-3">
								<div class="form-group">
									<#-- <p class="text-warning"><strong>A Captcha ideiglenesen nincs ellenőrizve, hogy könnyebb legyen a többi funkció tesztelése.</strong><p> -->
									<div class="g-recaptcha" id="loginCaptcha"></div>
								</div>		
							</div>													
							<div class="col-md-4 col-md-offset-4">								
								<div class="form-group">																											
									<div class="checkbox">
										<label>
										<input type="checkbox" name="remember-me" id="remember-me">&nbsp;<@label "login.rememberMe" />
										</label>
									</div>									
								</div>
							</div>
							<div class="col-md-6 col-md-offset-3 text-center">
								<div class="form-group">								
									<button type="submit" class="btn btn-primary">
										<@label "login.submit" />
										&nbsp;
										<span class="glyphicon glyphicon-log-in"></span>						
									</button>
								</div>
							</div>							
						</form>
					</div>
				</div>
			</div>
			
			<div class="panel panel-default">
				<div class="panel-heading" role="tab" id="forgot-panel-heading">
					<h4 class="panel-title">
						<a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#forgot-panel" aria-expanded="<#if mode == "forgot">true<#else>false</#if>" aria-controls="forgot-panel">
						<span class="glyphicon glyphicon-refresh"></span>
						&nbsp;&nbsp;
						<@label "forgot" />
						</a>
					</h4>
				</div>
				<div id="forgot-panel" class="panel-collapse collapse <#if mode == "forgot">in</#if>" role="tabpanel" aria-labelledby="forgot-panel-heading">
					<div class="panel-body">
						<form class="form-horizontal" action="/forgot" method="post">
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>							
							<div class="col-md-6 col-md-offset-3">
								<div class="form-group">
									<input type="email" class="form-control" name="email" placeholder="<@label "login.email" />" required />
								</div>
							</div>	
							<div class="col-md-6 col-md-offset-3">
								<div class="form-group">
									<div class="g-recaptcha" id="forgotCaptcha"></div>
								</div>		
							</div>						
							<div class="col-md-6 col-md-offset-3 text-center">
								<div class="form-group">		
									<button type="submit" class="btn btn-primary">
										<@label "forgot.submit" />
										&nbsp;
										<span class="glyphicon glyphicon-send"></span>						
									</button>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
			
			<div class="panel panel-default">
				<div class="panel-heading" role="tab" id="register-panel-heading">
					<h4 class="panel-title">
						<a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#register-panel" aria-expanded="<#if mode == "register">true<#else>false</#if>" aria-controls="register-panel">
						<span class="glyphicon glyphicon-user"></span>
						&nbsp;&nbsp;
						<@label "register" />
						</a>
					</h4>
				</div>
				<div id="register-panel" class="panel-collapse collapse <#if mode == "register">in</#if>" role="tabpanel" aria-labelledby="register-panel-heading">
					<div class="panel-body">
						<form class="form-horizontal" action="/register" method="post">
							<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>	
							<div class="col-md-6 col-md-offset-3">
								<div class="form-group">
									<input type="email" class="form-control" name="email" placeholder="<@label "login.email" />" required />
								</div>															
								<div class="form-group">
									<input type="password" class="form-control" name="password"  placeholder="<@label "login.password" />" required />
								</div>
								<div class="form-group">
									<input type="text" class="form-control" name="name" placeholder="<@label "register.name" />" required />
								</div>
							</div>
							<div class="col-md-6 col-md-offset-3">
								<div class="form-group">
									<div class="g-recaptcha" id="registerCaptcha"></div>
								</div>
							</div>
							<div class="col-md-6 col-md-offset-3 text-center">
								<div class="form-group">	
									<button type="submit" class="btn btn-primary">
										<@label "register.submit" />
										&nbsp;
										<span class="glyphicon glyphicon-plus"></span>						
									</button>
								</div>
							</div>							
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="row">
	<div class="col-md-6 col-md-offset-3">
	</div>
</div>
<script type="text/javascript">
    var CaptchaCallback = function(){
    	var sk = '6LdI6wYTAAAAAOX4tE-4x2R50o0DTpaQt17JcEcB';
    	grecaptcha.render('loginCaptcha', {'sitekey' : sk});
        grecaptcha.render('forgotCaptcha', {'sitekey' : sk});
        grecaptcha.render('registerCaptcha', {'sitekey' : sk});
    };
</script>
<#include "/includes/footer.ftl">