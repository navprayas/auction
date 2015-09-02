<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<meta http-equiv="refresh" content="6000">
<script>
	function openUrl(url) {
		window.location.href = url;
	}

	var marketList = function() {
		$
				.ajax({
					url : 'closedmarketlistajaxcall',
					method : 'get',
					success : function(data) {
						$('#marketlist-pagination').empty();
						var marketlist = jQuery.parseJSON(data);
						var l = marketlist.length;
						var tableHeader = "<tr><th>Sr. No.</th><th>Name</th><th>Location</th><th>City</th><th>Zone</th><th>Minimum Bid Price</th><th>Minimum Bid Increment</th><th>Created Time</th></tr>";
						$('#marketlist-pagination').append(tableHeader);
						var tableData = "";
						if (l > 0) {
							for (var i = 0; i < l; i++) {
								tableData += "<tr><td>" + marketlist[i].seqId + "</td><td>"
										+ marketlist[i].name + "</td><td>"
										+ marketlist[i].location + "</td><td>"
										+ marketlist[i].city + "</td><td>"
										+ marketlist[i].zone + "</td><td>"
										+ marketlist[i].minBidPrice
										+ "</td><td>"
										+ marketlist[i].minBidIncrement
										+ "</td><td>"
										+ getConvertedDate(marketlist[i].createdTime)
										+ "</td></tr>";
							}
						} else {
							tableData += "<tr><td colspan=''>No Data Found</td></tr>";
						}
						$('#marketlist-pagination').append(tableData);
					},
					error : function(err) {
						alert(err);
					}
				});
	};
	setInterval(marketList, 1000 * 60 * 2); // you could choose not to continue on failure...
</script>
<!-- { middle } -->
<section class="main">
	<div class="container">
		<div class="table-container">
			<div class="top-line">
				<div class="col-xs-12 col-sm-6">
					<ul class="nav nav-tabs" role="tablist">
						<li><a aria-controls="home" role="tab"
							data-toggle="tab" href="marketlist" onclick="openUrl(this.href)">Market
								List</a></li>
						<li><a aria-controls="home" role="tab" data-toggle="tab"
							href="activemarketlist" onclick="openUrl(this.href)">Active
								Market</a></li>
						<li role="presentation"  class="active"><a aria-controls="home" role="tab" data-toggle="tab"
							href="closedmarketlist"
							onclick="openUrl(this.href)">Closed Market</a></li>
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
						<table class="table table-bordered table-striped text-center"
							id="marketlist-pagination">
							<tr>
								<th>Sr. No.</th>
								<th>Name</th>
								<th>Location</th>
								<th>City</th>
								<th>Zone</th>
								<th>Min Bid Price</th>
								<th>Min Bid Increment</th>
								<th>Created Time</th>
							</tr>

							<c:forEach var="marketlist" items="${bidItems}"
								varStatus="status">
								<tr>
									<td>${marketlist.seqId}</td>
									<td>${marketlist.name}</td>
									<td>${marketlist.location}</td>
									<td>${marketlist.city}</td>
									<td>${marketlist.zone}</td>
									<td>${marketlist.minBidPrice}</td>
									<td>${marketlist.minBidIncrement}</td>
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
	<script>
	function getConvertedDate(time) {
		var date = new Date(time);
		var dd = date.getDate();
		if (dd < 10)
			dd = '0' + dd;

		var mm = date.getMonth() + 1;

		if (mm < 10)
			mm = '0' + mm;

		var yy = date.getFullYear() % 100

		if (yy < 10)
			yy = '0' + yy;

		var hh = date.getHours();
		if (hh < 10)
			hh = '0' + hh;

		var min = date.getMinutes();
		if (min < 10)
			min = '0' + min;

		var sec = date.getSeconds();
		if (sec < 10)
			sec = '0' + sec;
		return dd + '-' + mm + '-' + yy + ' ' + hh + ':' + mm + ':' + sec;

	}
	
	
	</script>
</section>

