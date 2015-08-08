
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<spring:url value="/css" var="css_url" />
<spring:url value="/js" var="js_url" />
<spring:url value="/css" var="css_url" />
<spring:url value="/images" var="images_url" />
<link rel="icon" href="${images_url}/favicon.png">
	function validatePassword() {
		var newPass = $('#newPassword').val();
		var cnfPass = $('#cnfrmPassword').val();
		if (newPass != cnfPass) {
			alert("Please Enter New Password and confirm password same.");
			return false;
		}
		return true;
	}
</script>


<!-- Bootstrap core CSS -->
<link href="${css_url}/bootstrap.css" rel="stylesheet">
<link href="${css_url}/font-awesome.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="${css_url}/custom.css" rel="stylesheet">


<!-- { middle } -->
<section class="main">
	<div class="container">
		<div class="form-container">

			<c:if test="${not empty message}">
				<h4 align="center" style="color: red;">${message}</h4>

			</c:if>


			<h1>
				<i class="fa fa-sign-in"></i> Change Password
			</h1>
			<form role="form" action="changepassword" method="post"
				onsubmit="return validatePassword();">
				<div class="form-group">
					<label for="exampleInputPassword1">Current Password</label> <input
						type="password" class="form-control" name="oldPassword"
						id="oldPassword" placeholder="Enter Your Current Password">
				</div>
				<div class="form-group">
					<label for="exampleInputPassword1"> New Password</label> <input
						type="password" class="form-control" name="newPassword"
						id="newPassword" placeholder="Enter New Password">
				</div>

				<div class="form-group">
					<label for="exampleInputPassword1"> Confirm Password</label> <input
						type="password" class="form-control" name="cnfrmPassword"
						id="cnfrmPassword" placeholder=" Confirm Password">
				</div>

				<div class="form-group form-submit">
					<a href="#"></a>
					<button type="submit" class="btn btn-primary pull-right">Change
						Password</button>
					<div class="clearfix"></div>
				</div>
			</form>
		</div>
	</div>
	<!-- /container -->
</section>