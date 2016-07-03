<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<title>Data Lake Homepage</title>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css" rel="stylesheet">
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	<link href="https://cdnjs.cloudflare.com/ajax/libs/dropzone/4.3.0/min/dropzone.min.css" rel="stylesheet">
	<script src="https://cdnjs.cloudflare.com/ajax/libs/dropzone/4.3.0/min/dropzone.min.js"></script>
	<link href="<c:url value="/resources/css/index.css" />" rel="stylesheet">
	<link href="<c:url value="/resources/css/results.css" />" rel="stylesheet">
	<link href='https://fonts.googleapis.com/css?family=Montserrat:400,700' rel='stylesheet' type='text/css'>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/vis/4.16.1/vis.min.js"></script>
	<link href="https://cdnjs.cloudflare.com/ajax/libs/vis/4.16.1/vis.min.css" rel="stylesheet">
	
</head>
<body>
	
	<div id="top-bar">
		<a href="/">
			<h2 id="header-title">
				550 Data Lake 
			</h2>
		</a>
		<c:choose>
	  		<c:when test="${!isLoggedIn}">
				<div class="bar-button-box">
					<button class="bar-button" data-toggle="modal" data-target="#logInModal">
						Log In
					</button>
				</div>
				<div class="bar-button-box">
					<button class="bar-button" data-toggle="modal" data-target="#signUpModal">
						 Sign Up
					</button>
				</div>
			</c:when>
			<c:otherwise>
			  <div class="bar-button-box">
			  		<form action="/logout" method="post"> 
						<button class="bar-button">
							 Log Out
						</button>
					</form>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	
	<div class="container">
		<div class="row">
			<div class="col-xs-12 text-center">
	
				<!-- Flash Messages -->
				<c:if test="${resultMessage != null}">
					<div class="text-center alert alert-success alert-dismissible custom-alert" role="alert">
					    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
					  	    <span aria-hidden="true">&times;</span>
					    </button>
					    ${resultMessage}
					</div>
				</c:if>
				
				<!-- Search Input Panel -->
				<div class="panel panel-default">
					<div class="panel-heading">
						<h1 class="panel-title">Search the Data Lake</h1>
					</div>
				  	<div class="panel-body">
				    	<div>
							<form action="/search" method="get">
								<input type="text" name="query" placeholder="Search" id="search-entry-box">
								<br>
							  	<button type="submit" class="search-button btn btn-default">Search</button>
							</form>
						</div>
				  	</div>
				</div>
				
				<c:forEach items="${resultsList}" var="result">
						<div class="panel panel-primary">
							<div class="panel-heading">${result.document.getName()}</div>
							<div class="panel-body">
						   		<div class="graph-display" id="${result.document.getId()}"></div>
							</div>
							<div class="panel-footer">
								<div class="btn-group">
									<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
										Download File<span class="caret"></span>
									</button>
									<ul class="dropdown-menu">
										<li>
										<a href="/downloadFile/${result.document.getId()}">
											${result.document.getName()}
										</a>
										</li>
									</ul>
								</div>
							</div>
						</div>
				</c:forEach>
					
			</div>
		</div>
	</div>
	
	<!--Sign Up Modal-->
	<div class="modal" id="signUpModal" tabindex="-1" role="dialog" aria-labelledby="signUpModal">
	  <div class="modal-dialog modal-sm" role="document">
	    <div class="modal-content">
	      <div class="modal-body text-center">
			<form action="/signup" method="post">
				<input type="text" name="username" placeholder="User Name">
				<input type="text" name="first" placeholder="First Name">
				<input type="text" name="last" placeholder="Last Name">
				<input type="password" name="password" placeholder="Password">
				<br>
			  	<input type="submit" value="Sign Up">
			</form>
	      </div>
	  	</div>
	  </div>
	</div>
	
		<!--Login Modal-->
	<div class="modal" id="logInModal" tabindex="-1" role="dialog" aria-labelledby="logInModal">
	  <div class="modal-dialog modal-sm" role="document">
	    <div class="modal-content">
	      <div class="modal-body text-center">
			<form action="/authenticate" method="post">
				<input type="text" name="username" placeholder="User Name">
				<input type="password" name="password" placeholder="Password">
				<br>
			  	<input type="submit" value="Log In">
			</form>
	      </div>
	  	</div>
	  </div>
	</div>
	
	
<c:forEach items="${resultsList}" var="result">
	<script type="text/javascript">
	    // create an array with nodes
	    var nodes = new vis.DataSet(${result.nodesJson});
	
	    // create an array with edges
	    var edges = new vis.DataSet(${result.edgesJson});
	
	    // create a network
	    var container = document.getElementById('${result.document.id}');
	
	    // provide the data in the vis format
	    var data = {
	        nodes: nodes,
	        edges: edges
	    };
	    var options = {};
	
	    // initialize your network!
	    var network = new vis.Network(container, data, options);
	</script>
</c:forEach>
		
	
</body>
</html>