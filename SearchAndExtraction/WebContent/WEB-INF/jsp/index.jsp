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
	<link href='https://fonts.googleapis.com/css?family=Montserrat:400,700' rel='stylesheet' type='text/css'>
	
</head>
<body>
	
	<div id="top-bar">
		<h2 id="header-title">
			550 Data Lake 
		</h2>
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
				
				<!-- User Account Display -->
				<c:if test="${isLoggedIn}">
				
					<!-- File Upload Section-->
					<div class="panel panel-default">
						<div class="panel-heading">
							<h1 class="panel-title">Upload Files</h1>
						</div>
					  	<div class="panel-body">
					    	<form action="/uploadFile" class="dropzone"></form>
					  	</div>
					</div>
					
					<!-- User Files Display -->
					<div class="panel panel-default">
						<div class="panel-heading">
							<h1 class="panel-title">Your Current Files</h1>
						</div>
					  	<div class="panel-body">
					  		You can set access permissions here.
					  	</div>
						  	<ul class="list-group">
							  	<c:forEach items="${documents}" var="doc">
								    <li class="list-group-item text-left" style="position:relative">
								        <div class="doc-name">${doc.name}</div>
								        <div class="permissions-buttons">
									        <div class="btn-group" role="group" aria-label="...">
									        	<button id="${doc.id}P" type="button" style="outline: none;" class="<c:if test="${doc.permission eq 'P'.charAt(0)}">selected </c:if>perm-button btn btn-default" onclick="setPermission(${doc.id}, 'P')">Private</button>
											  	<button id="${doc.id}E" type="button" style="outline: none;" class="<c:if test="${doc.permission eq 'E'.charAt(0)}">selected </c:if>perm-button btn btn-default" onclick="setPermission(${doc.id}, 'E')">Elevated</button>
											  	<button id="${doc.id}A" type="button" style="outline: none;" class="<c:if test="${doc.permission eq 'A'.charAt(0)}">selected </c:if>perm-button btn btn-default" onclick="setPermission(${doc.id}, 'A')">All</button>
											</div>
										</div>
								        
								    </li>
								</c:forEach>
						    	
						  	</ul>
					  	
					</div>
				</c:if>
				
					
			</div>
		</div>
	</div>
	
	
		<!--SignUp Modal-->
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
	
	<script type="text/javascript">
		function setPermission(id, level) {
			setSelected(id, level);
			$.ajax({
		        url : "setDocPermission/",
		        type : "POST", 
		        data : {'docID': id, 'permLevel' : level},
		        success : function() {
		            
		        },
		        error : function(xhr,errmsg,err) {
		            console.log(errmsg);
		            console.log(xhr.status + ": " + xhr.responseText); 
		        }
		    });	
		}
		
		function setSelected(id, level) {
			var typeP = "#" + id + "P";
			var typeE = "#" + id + "E";
			var typeA = "#" + id + "A";
			var selectedType = "#" + id + level;
			$(typeP).removeClass("selected");
			$(typeE).removeClass("selected");
			$(typeA).removeClass("selected");
			$(selectedType).addClass("selected");
		}
	</script>
	
</body>
</html>