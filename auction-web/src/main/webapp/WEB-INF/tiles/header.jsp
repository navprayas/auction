<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<spring:url value="/" var="baseurl" />
<spring:url value="/css" var="css_url" />
<spring:url value="/js" var="js_url" />
<spring:url value="/images" var="images_url" />
<spring:url value="/documents" var="files_url" />

<link rel="icon" href="${images_url}/favicon.png">

<title>Home</title>

<!-- Bootstrap core CSS -->
<link href="${css_url}/bootstrap.css" rel="stylesheet">
<link href="${css_url}/font-awesome.min.css" rel="stylesheet">

<!-- Custom styles for this template -->
<link href="${css_url}/custom.css" rel="stylesheet">

<header class="header">
	<div class="container">
		<div class="col-xs-12 col-sm-3 hdr-lft">
			<div class="hdr-lft-in">
				<img src="${images_url}/maharashtra-seamless-ltd.png">
			</div>
		</div>
		<div class="col-xs-12 col-sm-6 hdr-mdl">
			<div class="hdr-mdl-in">
				<marquee scrollamount="2" onmouseover="stop();"
					onmouseout="start();"> In case of any technical
					difficulties, please contact support at +912225970344 </marquee>
			</div>
		</div>
		<div class="col-xs-12 col-sm-3 hdr-rgt">
			<div class="hdr-rgt-in text-right">
				<img src="${images_url}/jindal-logo.png">
			</div>
		</div>
	</div>
	<div class="strip-bar">
		<div class="container">
			<nav class="navbar">
				<div class="navbar-header">
					<button aria-controls="navbar" aria-expanded="false"
						data-target="#navbar" data-toggle="collapse"
						class="navbar-toggle collapsed" type="button">
						<span class="sr-only">Toggle navigation</span> <span
							class="icon-bar"></span> <span class="icon-bar"></span> <span
							class="icon-bar"></span>
					</button>
				</div>
				<div class="navbar-collapse collapse" id="navbar">
					<ul class="nav navbar-nav auction-menu">
						<sec:authorize ifAnyGranted="ROLE_ADMIN">
							<li class="active"><a href="auctionmanagement">Auction
									Management</a></li>
							<li><a href="userauctionmap">User Auction Map</a></li>
							<li><a href="registervendor">Create Vendor</a></li>
							<li><a href="registeruser">Create User</a></li>
							<li><a href="changepassword">Change Password</a></li>
							<li><a href="createauction">Create Auction</a></li>
							<li><a href="#">File Upload</a></li>

						</sec:authorize>


						<sec:authorize ifAnyGranted="ROLE_OBSERVER">

							<li class="active"><a href="marketlist">Forward Market</a></li>
							<li><a href="#">Reports</a></li>
							<li><a href="#">Online Users</a></li>
							<li><a href="${files_url}/termandcondition.pdf">General
									Terms &amp; Condition</a></li>

							<li><a href="#">Change Password</a></li>
						</sec:authorize>



						<sec:authorize ifAnyGranted="ROLE_BIDDER">

							<li class="active"><a href="marketlist">Forward Market</a></li>
							<li><a href="#">Reports</a></li>
							<li><a href="${files_url}/termandcondition.pdf"
								target="_blank">General Terms &amp; Condition</a></li>
							<li><a href="changepassword">Change Password</a></li>
						</sec:authorize>



					</ul>
				</div>
				<!--/.nav-collapse -->
				<div class="welcom">
					<ul class="list-inline">
						<sec:authorize access="isAuthenticated()">
							<li>Welcome <sec:authentication
									property="principal.username" /></li>
							<li><a href="${baseurl}logout">Logout</a></li>
						</sec:authorize>
					</ul>

				</div>
			</nav>
		</div>
	</div>
</header>
