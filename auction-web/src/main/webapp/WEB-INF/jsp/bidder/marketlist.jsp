
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<spring:url value="/css" var="css_url" />
<spring:url value="/js" var="js_url" />




<!-- { middle } -->
<section class="main">
	<div class="container">
		<div class="table-container">
			<div class="top-line">
				<div class="col-xs-12 col-sm-6">
					<ul class="nav nav-tabs" role="tablist">
						<li role="presentation" class="active"><a
							aria-controls="home" role="tab" data-toggle="tab"
							href="marketlist" onclick="openUrl(this.href)">Market List</a></li>
						<li><a aria-controls="home" role="tab" data-toggle="tab"
							href="activemarketlist" onclick="openUrl(this.href)">Active
								Market</a></li>
						<li><a aria-controls="home" role="tab" data-toggle="tab"
							href="closedmarketlist" onclick="openUrl(this.href)">Closed
								Market</a></li>
					</ul>
				</div>
				<div class="col-xs-12 col-sm-6">
					<ul class="list-inline pull-right">
						<li><a href="#excel">Export to Excel</a></li>
						<li><a href="#pdf">Download PDF</a></li>
						<li><a href="#print">Print</a></li>
					</ul>
				</div>
				<div class="clearfix"></div>
			</div>
			<!-- { tab-Effects } -->
			<div class="tab-content">
				<div role="tabpanel" class="tab-pane active" id="summary">
					<div class="mark-check1">
						<div class="col-sx-12 col-sm-12 select-date">
							<form role="form">
								<div class="col-xs-12 col-sm-4 col-md-3 col-lg-2 tetrt">
									Select Date:&nbsp; <span>Form</span>
								</div>
								<div class="col-xs-12 col-sm-3 col-md-3 col-lg-2">
									<input type="date" placeholder="Date" id="inputdate31"
										class="form-control">
								</div>
								<div class="col-xs-12 col-sm-1 tetrt">To</div>
								<div class="col-xs-12 col-sm-3 col-md-3 col-lg-2">
									<input type="date" placeholder="Date" id="inputdate41"
										class="form-control">
								</div>
								<div class="col-xs-12 col-sm-1 col-md-2 col-lg-2">
									<input type="button" class="btn btn-primary" Value="Go">
								</div>
							</form>
						</div>
					</div>
					<div class="table-responsive user-map">
						<table id="marketlist-pagination"
							class="table table-bordered table-striped text-center">
							<tr>
								<th>Sr. No.</th>
								<th>Group Id</th>
								<th>Name</th>
								<th>Location</th>
								<th>City</th>
								<th>Zone</th>
								<th>MinBidPrice</th>
								<th>MinBidIncrement</th>
								<th>Time Left</th>
								<th>Created Time</th>
							</tr>

							<c:forEach var="marketlist" items="${bidItems}"
								varStatus="status">
								<tr>
									<td>${status.index+1}</td>
									<td>${marketlist.bidItemGroupId}</td>
									<td>${marketlist.name}</td>
									<td>${marketlist.location}</td>
									<td>${marketlist.city}</td>
									<td>${marketlist.zone}</td>
									<td>${marketlist.minBidPrice}</td>
									<td>${marketlist.minBidIncrement}</td>
									<td>${marketlist.timeLeft}</td>
									<td>${marketlist.createdTime}</td>

								</tr>
							</c:forEach>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- /container -->



	<script type="text/javascript">
		function openUrl(url) {
			window.location.href = url;
		}
		//pagination function started here
		/* $(document).ready(function() {
			$('#marketlist-pagination').dataTable({
				"aaSorting" : [ [ 0, "asc" ] ]
			});
		}); */
		//pagination function ended here
		var marketList = function() {
			$
					.ajax({
						url : 'marketlistajaxcall',
						method : 'get',
						success : function(data) {
							alert("data updating");
							$('#marketlist-pagination').empty();
							var marketlist = jQuery.parseJSON(data);
							var l = marketlist.length;
							var tableHeader = "<tr><th>Sr. No.</th><th>Group Id</th><th>Name</th><th>Location</th><th>City</th><th>Zone</th><th>MinBidPrice</th><th>MinBidIncrement</th><th>Time Left</th><th>Created Time</th></tr>";
							$('#marketlist-pagination').append(tableHeader);
							var tableData = "";

							for (var i = 0; i < l; i++) {
								tableData += "<tr><td>" + i + "</td><td>"
										+ marketlist[i].bidItemGroupId
										+ "</td><td>" + marketlist[i].name
										+ "</td><td>" + marketlist[i].location
										+ "</td><td>" + marketlist[i].city
										+ "</td><td>" + marketlist[i].zone
										+ "</td><td>"
										+ marketlist[i].minBidPrice
										+ "</td><td>"
										+ marketlist[i].minBidIncrement
										+ "</td><td>" + marketlist[i].timeleft
										+ "</td><td>"
										+ marketlist[i].createdTime + "</td>";
							}
							$('#marketlist-pagination').append(tableData);
						},
						error : function(err) {
							alert(err);
						}
					});
		};
		setInterval(marketList, 5000); // you could choose not to continue on failure...
	</script>
</section>

