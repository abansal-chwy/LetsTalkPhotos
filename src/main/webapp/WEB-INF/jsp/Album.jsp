<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
 <link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
  
<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
	<link href="https://vjs.zencdn.net/6.6.3/video-js.css" rel="stylesheet">
<link href="https://cdnjs.cloudflare.com/ajax/libs/videojs-record/2.1.0/css/videojs.record.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.3.1.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/video.js/6.7.2/video.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/RecordRTC/5.4.6/RecordRTC.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/adapterjs/0.15.0/adapter.min.js"></script>
<script src="https://collab-project.github.io/videojs-record/dist/wavesurfer.min.js"></script>
<script src="https://collab-project.github.io/videojs-record/dist/wavesurfer.microphone.min.js"></script>
<script src="https://collab-project.github.io/videojs-record/dist/videojs.wavesurfer.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/videojs-record/2.1.2/videojs.record.min.js"></script>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
  

  <style>
  /* change player background color */
  #myVideo {
      background-color: #9ab87a;
  }
  </style>
</head>

<body style="background-color: #e9ebee;">




<script>
  // This is called with the results from from FB.getLoginStatus().
  function statusChangeCallback(response) {
    console.log('statusChangeCallback');
    console.log(response);
    // The response object is returned with a status field that lets the
    // app know the current login status of the person.
    // Full docs on the response object can be found in the documentation
    // for FB.getLoginStatus().
    if (response.status === 'connected') {
      // Logged into your app and Facebook.
      testAPI();
    } else {
    	window.location='/';
    	
    	
      // The person is not logged into your app or we are unable to tell.
      
    }
  }
  function checkLoginState() {
	    FB.getLoginStatus(function(response) {
	      statusChangeCallback(response);
	    });
	  }
</script>


	<div id="fb-root"></div>
	<script>
		(function(d, s, id) {
			var js, fjs = d.getElementsByTagName(s)[0];
			if (d.getElementById(id))
				return;
			js = d.createElement(s);
			js.id = id;
			js.src = 'https://connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.12&appId=166947310676837&autoLogAppEvents=1';
			fjs.parentNode.insertBefore(js, fjs);
		}(document, 'script', 'facebook-jssdk'));
	</script>
	<nav class="navbar navbar-toggleable-md navbar navbar-dark bg-dark">
	<button class="navbar-toggler navbar-toggler-right" type="button"
		data-toggle="collapse" data-target="#navbarSupportedContent"
		aria-controls="navbarSupportedContent" aria-expanded="false"
		aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>


	</button>
	<a class="navbar-brand" href="/">LetsTalkPhotos</a>

	<div class="collapse navbar-collapse" id="navbarSupportedContent">
		<ul class="navbar-nav mr-auto">
			<li class="nav-item active"><a class="nav-link" href="/">Home
					<span class="sr-only">(current)</span>
			</a></li>
			<li class="nav-item"><a class="nav-link" href="/VerifyProfile">Profile</a>
			</li>
			<li class="nav-item"><a class="nav-link" href="/Friends">Friends</a>
			</li>
			
			<li class="nav-item dropdown"><a
				class="nav-link dropdown-toggle" href="#" id="navbarDropdown"
				role="button" data-toggle="dropdown" aria-haspopup="true"
				aria-expanded="false"> Notifications </a>
				<div class="dropdown-menu" aria-labelledby="navbarDropdown">
					<c:forEach items="${notificationPost}" var="notificationPost">

						<li><a href="viewSelectedPostfriend?postid=${notificationPost.key}">${notificationPost.value}
							has created a new post</a></li>
						


					</c:forEach>
					<c:forEach items="${postNotificationsComments}"
						var="postNotificationsComments">

						<a
							href="viewSelectedPostfriend?postid=${postNotificationsComments.key}">
							${postNotificationsComments.value}
							has commented on a post</a>
				

					</c:forEach>

					

				</div></li>

		</ul>
		<!-- <form class="form-inline my-2 my-lg-0">
			<input class="form-control mr-sm-2" type="text" placeholder="Search">
			<button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
		</form> -->
	</div>
	 </nav>
	 
	<div class="fb-login-button" data-max-rows="1" data-size="Medium" data-button-type="continue_with" data-show-faces="false" 
	data-auto-logout-link="true" 
              data-use-continue-as="false" onlogin="checkLoginState();" style="float:right;margin-right:2%;"></div>
              <div id="status"></div>
              
              
	<div style="margin-top:3%">
	<form action="/veiwAlbumCreated" method="Post">
	<button class="btn btn-primary" id ="saveButton">View Album</button>
</form>
	</div>
	
	
	<div class="container">
	
              </div>
              
  <audio id="myAudio" class="video-js vjs-default-skin"></audio>            
	
<script>
var player = videojs("myAudio", {
    controls: true,
    width: 600,
    height: 300,
    fluid: false,
    plugins: {
        wavesurfer: {
            src: "live",
            waveColor: "#36393b",
            progressColor: "#black",
            debug: true,
            cursorWidth: 1,
            msDisplayMax: 20,
            hideScrollbar: true
        },
        record: {
            audio: true,
            video: false,
            maxLength: 20,
            debug: true
        }
    }
}, function() {
    // print version information at startup
    videojs.log('Using video.js', videojs.VERSION,
        'with videojs-record', videojs.getPluginVersion('record'),
        '+ videojs-wavesurfer', videojs.getPluginVersion('wavesurfer'),
        'and recordrtc', RecordRTC.version);
});
// error handling
player.on('deviceError', function() {
    console.log('device error:', player.deviceErrorCode);
});
player.on('error', function(error) {
    console.log('error:', error);
});
// user clicked the record button and started recording
player.on('startRecord', function() {
    console.log('started recording!');
});
// user completed recording and stream is available
player.on('finishRecord', function() {
    // the blob object contains the recorded data that
    // can be downloaded by the user, stored on server etc.
    console.log('finished recording: ', player.recordedData);
    
     var reader = new FileReader();
    var base64data;
    reader.readAsDataURL(player.recordedData);
    reader.onloadend=function(){
    	base64data=reader.result;
    	console.log(base64data);
    	$("#record").val(base64data); 
    }
});

$(document).ready(function(){
	$("#saveButton").on("click",function(){
		$("#audioForm").submit();
	});
});
</script>


<script>

jQuery(function ($) {
    var $inputs = $('input[name=recording],input[name=textannotation]');
    $inputs.on('input', function () {
        // Set the required property of the other input to false if this input is not empty.
        $inputs.not(this).prop('required', !$(this).val().length);
    });
});
</script>
<div class ="upload_image">


<br>



</div>	
<form id="audioForm" action="/uploadToAlbum" method="post" enctype="multipart/form-data">
<input type="hidden" id="record" name="recording"  required>
<br>
<div class="input-group mb-3">
  <div class="custom-file">
  <label for="file">Upload your Picture.</label>
    <input type="file" name ="file" class="custom-file-input" id="inputGroupFile02" required>
    <label class="custom-file-label" for="inputGroupFile02">Choose Picture</label>
  </div>
  
</div>
<input type ="text" name="textannotation" required>
<br>
<button class="btn btn-primary" id ="saveButton">Save</button>
</form>
	
	<script>
    var createAllErrors = function() {
        var form = $( this ),
            errorList = $( "ul.errorMessages", form );

        var showAllErrorMessages = function() {
            errorList.empty();

            // Find all invalid fields within the form.
            var invalidFields = form.find( ":invalid" ).each( function( index, node ) {

                // Find the field's corresponding label
                var label = $( "label[for=" + node.id + "] "),
                    // Opera incorrectly does not fill the validationMessage property.
                    message = node.validationMessage || 'Invalid value.';

                errorList
                    .show()
                    .append( "<li><span>" + label.html() + "</span> " + message + "</li>" );
            });
        };

        // Support Safari
        form.on( "submit", function( event ) {
            if ( this.checkValidity && !this.checkValidity() ) {
                $( this ).find( ":invalid" ).first().focus();
                event.preventDefault();
            }
        });

        $( "input[type=submit], button:not([type=button])", form )
            .on( "click", showAllErrorMessages);

        $( "input", form ).on( "keypress", function( event ) {
            var type = $( this ).attr( "type" );
            if ( /date|email|month|number|search|tel|text|time|url|week/.test ( type )
              && event.keyCode == 13 ) {
                showAllErrorMessages();
            }
        });
    };
    
    $( "form" ).each( createAllErrors );
</script>
	
</body>
</html>