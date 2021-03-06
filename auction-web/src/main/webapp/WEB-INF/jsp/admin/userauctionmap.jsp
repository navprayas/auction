<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="f" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<spring:url value="/admin/initcache" var="admin_initcache_url" />
<spring:url value="admin/closeAuction" var="close_auction_url" />
<spring:url value="/css" var="css_url" />
<spring:url value="/js" var="js_url" />
<script src="${js_url}/jquery.min.js"></script>

<script>
	function selectAllUser() {
		$('.username').each(function(index) {
			var classes = $(this).is(":checked") ? true : false;
			if (!classes) {
				$(this).attr('checked', true);
			} else {
				$(this).attr('checked', false);
			}
		});
	}
</script>
<!-- { middle } -->
<section class="main">
	<div class="container">
		<div class="table-container">
			<div class="top-line">
				<div class="col-xs-12 col-sm-6">
					<ul class="nav nav-tabs" role="tablist">
						<li role="presentation" class="active">Select Auction <select
							name="auction" id="auction">
								<c:forEach var="auction" items="${auctionlist}">
									<option id="${auction.auctionId}" value="${auction.auctionId}">
										<c:out value="${auction.name}" />
									</option>
								</c:forEach>
						</select>



						</li>


						<li><a aria-controls="home" role="tab" data-toggle="tab"
							href="activemarketlist" onclick="openUrl(this.href)"></a></li>
						<li><a aria-controls="home" role="tab" data-toggle="tab"
							href="closedmarketlist" onclick="openUrl(this.href)"></a></li>
						<li><c:if test="${ not empty message }">
						${ message}
						
						</c:if></li>
					</ul>
				</div>
				<div class="col-xs-12 col-sm-6">
					<ul class="list-inline pull-right">
						<!-- <li><a href="#excel"></a></li>
						<li><a href="#pdf"></a></li>
						<li><a href="#print"></a></li> -->
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
								<c:forEach var="category" items="${categorylist}">
									<div class="col-xs-6 col-sm-2">
										<input type="checkbox" id="${category.id}" name="Categories">
										${category.categoryName}</input>
									</div>
								</c:forEach>
							</form>

							<div class="col-xs-12 col-sm-1 col-md-2 col-lg-2">


								<form action="userauctionmap" name="auctionmapform"
									method="post">
									<table>
										<tr>
											<td><input type="hidden" id="selectedAuctionId"
												name="selectedAuctionId" /> <input type="hidden"
												id="selectedCategoryIdList" name="selectedCategoryIdList" />
												<input type="hidden" id="selectedUserIdList"
												name="selectedUserIdList" /></td>
										</tr>
									</table>
									<input type="button" class="btn btn-primary" Value="Go"
										onclick="setValuesAndSubmit();">
								</form>

							</div>
						</div>
					</div>
					<div class="table-responsive user-map">
						<table class="table table-bordered table-striped text-center">
							<tr>




								<th>Sr. No.</th>
								<th><input type="checkbox" name="selectAll"
									onclick="selectAllUser();"> &nbsp;&nbsp;Select All</th>
								<th>User Name</th>
								<th>Email</th>
							</tr>
							<c:forEach var="user" items="${userlist}" varStatus="status">
								<tr>
									<td>${status.index+1}</td>
									<td><input type="checkbox" id="${user.id}" name="users"
										class="username"></td>
									<td>${user.username}</td>
									<td>${user.email}</td>
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
		function setValuesAndSubmit() {
			var auctionId = document.getElementById("auction").value;
			document.getElementById("selectedAuctionId").value = auctionId;

			var catList = getValues("Categories");
			console.log(catList);
			document.getElementById("selectedCategoryIdList").value = catList;

			var userList = getValues("users");
			console.log(userList);
			document.getElementById("selectedUserIdList").value = userList;
			if (!auctionId) {
				alert("auction needs to be Selected");
				return false;
			}
			if (catList.length == 0) {
				alert("categories need to be Selected");
				return false;
			}
			if (userList.length == 0) {
				alert("users need to be Selected");
				return false;
			}

			document.forms["auctionmapform"].submit();
		}

		function getValues(objName) {
			var results = "";
			var arr = new Array();
			arr = document.getElementsByName(objName);

			for (var i = 0; i < arr.length; i++) {
				var obj = document.getElementsByName(objName).item(i);

				if (i > 0 && obj.checked && results.length > 0) {
					results += "$";
				}
				if (obj.checked) {
					results += obj.getAttribute("id");
				}
			}
			return results;
		}
	</script>



</section>

