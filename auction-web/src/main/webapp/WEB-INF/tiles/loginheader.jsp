<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<spring:url value="/css" var="css_url" />
<spring:url value="/js" var="js_url" />
<spring:url value="/images" var="images_url" />
<link rel="icon" href="${images_url}/favicon.png">
<link href="${css_url}/bootstrap.css" rel="stylesheet">
<link href="${css_url}/font-awesome.min.css" rel="stylesheet">

<!-- { header } -->
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
	<div class="strip-bar"></div>
</header>