<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Profile Page</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" >
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" ></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>

<style>
#image{ height: 200px; width: 250px; overflow: hidden;}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

</head>
<body>

<nav class="navbar navbar-toggleable-md navbar navbar-dark bg-dark" >
  <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
    
    
  </button>
  <a class="navbar-brand" href="/">LetsTalkPhotos</a>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
      <li class="nav-item active">
        <a class="nav-link" href="/">Home <span class="sr-only">(current)</span></a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="/VerifyProfile">Profile</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="/Friends">Friends</a>
      </li>
    </ul>
    <form class="form-inline my-2 my-lg-0">
      <input class="form-control mr-sm-2" type="text" placeholder="Search">
      <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
    </form>
  </div>
</nav>



  







<p class="font-weight-bold" style="margin-top:5%;font-size:20px;">${user.name}</p>
<div id="image"><img src ="${ImgSrc}" alt="Profile Page Image"/ 
style="margin-top:10%;margin-left:3%"></div>


<div class="bio"style="width: 20%;margin-left:3%;" >

<!-- Get values from user profile table -->



   
<form action="/UpdateProfile" method="post">
<ul class="errorMessages"></ul>
  <label for="bio">About Me:</label>
  </br>
 <textarea name="bio" required><c:out value="${user.biography}"></c:out></textarea>
 <!-- <button type="submit" id="submitbio">Update</button> -->
</form>
</div>





	

</body>
</html>