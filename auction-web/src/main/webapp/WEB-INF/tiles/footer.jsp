<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<spring:url value="/js" var="js_url" />
<spring:url value="/css" var="css_url" />
<link href="${css_url}/jquery.dataTables.css" rel="stylesheet">
<script src="${js_url}/jquery.min.js"></script>
<script src="${js_url}/bootstrap.min.js"></script>
<script src="${js_url}/ie10-viewport-bug-workaround.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
<script type="text/javascript" src="${js_url}/jquery.dataTables.js"></script>


<footer class="footer">
	<div class="container">
		<div class="col-xs-12 col-sm-6 copyrgt">
			<p>Copyright &copy; 2015 Navprayas</p>
		</div>
		<div class="col-xs-12 col-sm-6 poweredby text-right">
			<p>
				Developed By: <a href="http://cfeindia.com/">CFE India</a>
			</p>
		</div>
	</div>
</footer>

