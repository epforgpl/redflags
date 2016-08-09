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
<#assign title = "title.registration" />
<#include "/header.ftl">


<div id="validationErrors">
	<#if errorList??>
		<div class="bs-component">
	    	<#list errorList as error>
		    	<div class="col-md-8 col-md-offset-2 alert alert-dismissible alert-danger">
					${error}
				</div>
	     	</#list>
	  	</div>
	</#if>
</div>

<#if success??>
	<div class="bs-component">
		<div class="col-md-8 col-md-offset-2 alert alert-dismissible alert-success">
			${success}
		</div>
	</div>
</#if>

<#if error??>
	<div class="bs-component">
		<div class="col-md-8 col-md-offset-2 alert alert-dismissible alert-danger">
			${error}
		</div>
	</div>
</#if>

<div class="row">
	<div class="col-md-8 col-md-offset-2">
		<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
			<div class="panel panel-default">
				<div class="panel-heading" role="tab" id="login-panel-heading">
					<h4 class="panel-title">
						<span class="glyphicon glyphicon-lock"></span>
						&nbsp;
						Registration
					</h4>
				</div>
				
					<div class="panel-body">
						<form class="form-horizontal" action="/registration/save" method="post" name="apiUser">
						
							<div class="col-md-6 col-md-offset-3">
								<div class="form-group">								
									<input type="email" class="form-control" name="email" placeholder="E-mail address" <#if apiUser.getEmail()??>value="${apiUser.getEmail()}"</#if> />
								</div>							
								<div class="form-group">								
									<input type="name" class="form-control" name="name" placeholder="Name" <#if apiUser.getName()??>value="${apiUser.getName()}"</#if> />
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
										<span class="glyphicon glyphicon-ok"></span>
										&nbsp;
										Request API access
									</button>
								</div>
							</div>							
						</form>
					</div>
				
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="//www.google.com/recaptcha/api.js?onload=CaptchaCallback&render=explicit" async defer></script>
<script type="text/javascript">
    var CaptchaCallback = function(){
    	var sk = '6LdI6wYTAAAAAOX4tE-4x2R50o0DTpaQt17JcEcB';
        grecaptcha.render('registerCaptcha', {'sitekey' : sk});
    };
</script>

<#include "/footer.ftl">