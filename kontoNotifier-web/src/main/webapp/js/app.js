require("https://apis.google.com/js/platform.js");

/* Builds the updated table for the member list */
function buildMemberRows(members) {
    return _.template( $( "#member-tmpl" ).html(), {"members": members});
}




function updateNotifiersTable() {
    // Display the loader widget
    $.mobile.loading("show");

    $.ajax({
        url: "rest/notifier",
        cache: false,
        success: function(data) {
            $( "#notifiers" ).empty().append(buildNotifiersRows(data));
            $( "#notifiers-table" ).table( "refresh" );
        },
        error: function(error) {
            console.log("error updating table -" + error.status);
        },
        complete: function() {
            // Hide the loader widget
            $.mobile.loading("hide");
            
        }
    });
}



function updateKontenTable() {
    // Display the loader widget
    $.mobile.loading("show");

    $.ajax({
        url: "rest/notifier",
        cache: false,
        success: function(data) {
            $( "#kontos" ).empty().append(buildNotifiersRows(data));
            //$( "#kontos-table" ).table( "refresh" ); //TODO dosnt work
        },
        error: function(error) {
            console.log("error updating table -" + error.status);
        },
        complete: function() {
            // Hide the loader widget
            $.mobile.loading("hide");
            
        }
    });
}

/* Builds the updated table for the member list */
function buildNotifiersRows(data) {
    return _.template( $( "#notifiers-tmpl" ).html(), {"data": data});
}



function updatekontosTable() {
    // Display the loader widget
    $.mobile.loading("show");

    $.ajax({
        url: "rest/konto",
        cache: false,
        success: function(data) {
            $( "#kontos" ).empty().append(buildkontosRows(data));
            $( "#kontos-table" ).table( "refresh" );
        },
        error: function(error) {
            console.log("error updating table -" + error.status);
        },
        complete: function() {
            // Hide the loader widget
            $.mobile.loading("hide");
            
        }
    });
}

/* Builds the updated table for the member list */
function buildkontosRows(data) {
    return _.template( $( "#kontos-tmpl" ).html(), {"data": data});
}


function getKontos() {

	$select = $('#kontoSelection');
    $.ajax({
        url: "rest/konto",
        cache: false,
        success: function(data) {
        	console.log(" ------------ -" + data);
        	
            $select.html('');
            
            $.each(data, function(key, val){
            	console.log(" ------------ -" + val.account + "  --------" + val.id);
              $select.append( new Option( val.account, val.id) );
            })
            return data;
        },
        error: function(error) {
            console.log("error updating table -" + error.status);
        },
        complete: function() {
            // Hide the loader widget
//            $.mobile.loading("hide");
        }
    });
}

/* Uses JAX-RS GET to retrieve current member list */
function getNotifiers() {

    $.ajax({
        url: "rest/notifier",
        cache: false,
        success: function(data) {
           
              $( "#nots" ).append(buildNotsRows(data));
              $( "#nots-table" ).table( "refresh" );
            
        },
        error: function(error) {
            //console.log("error updating table -" + error.status);
        },
        complete: function() {
            // Hide the loader widget
//            $.mobile.loading("hide");
        }
    });
}


/* Builds the updated table for the member list */
function buildNotsRows(data) {
	console.log(" ------------ -" + data);
	res = _.template( $( "#nots-tmpl" ).html(), {"data": data});
    return res;
}
 
function saveKonto(data) {
    //clear existing  msgs
    $('span.invalid').remove();
    $('span.success').remove();

    // Display the loader widget
    $.mobile.loading("show");

    $.ajax({
        url: 'rest/konto',
        contentType: "application/json",
        dataType: "json",
        type: "POST",
        data: data,
        success: function(data) {
            //console.log("Member registered");

            //clear input fields
            $('#konto-reg')[0].reset();

            $('#konto-formMsgs').append($('<span class="success"> Kontodaten gespeichert.</span>'));
            
            updateKontenTable();
        },
        error: function(error) {
            if ((error.status == 409) || (error.status == 400)) {
                //console.log("Validation error registering user!");

                var errorMsg = $.parseJSON(error.responseText);

                $.each(errorMsg, function(index, val) {
                    $('<span class="invalid">' + val + '</span>').insertAfter($('#' + index));
                });
            } else {
                //console.log("error - unknown server issue");
                $('#konto-formMsgs').append($('<span class="invalid">Unknown server error</span>'));
            }
        },
        complete: function() {
            // Hide the loader widget
            $.mobile.loading("hide");
        }
    });
}


function saveNotifier(data) {
    //clear existing  msgs
    $('span.invalid').remove();
    $('span.success').remove();

    // Display the loader widget
    $.mobile.loading("show");

    $.ajax({
        url: 'rest/notifier',
        contentType: "application/json",
        dataType: "json",
        type: "POST",
        data: data,
        success: function(data) {
            //console.log("Member registered");

            //clear input fields
            $('#not-reg')[0].reset();

            //mark success on the registration form
            $('#not-formMsgs').append($('<span class="success">Info-Service gespeichert.</span>'));

            updateNotifiersTable();
        },
        error: function(error) {
            if ((error.status == 409) || (error.status == 400)) {
                //console.log("Validation error registering user!");

                var errorMsg = $.parseJSON(error.responseText);

                $.each(errorMsg, function(index, val) {
                    $('<span class="invalid">' + val + '</span>').insertAfter($('#' + index));
                });
            } else {
                //console.log("error - unknown server issue");
                $('#not-formMsgs').append($('<span class="invalid">Unknown server error</span>'));
            }
        },
        complete: function() {
            // Hide the loader widget
            $.mobile.loading("hide");
        }
    });

}



function sendToken(data) {

    $.ajax({
        url: 'rest/setToken',
        contentType: "application/json",
        dataType: "json",
        type: "POST",
        data: JSON.stringify(data),
        success: function(data) {
        	console.log(" id_token sent");
        },
        error: function(error) {
            if ((error.status == 409) || (error.status == 400)) {
                //console.log("Validation error registering user!");

                var errorMsg = $.parseJSON(error.responseText);
                console.log("error - "+errorMsg);
                $.each(errorMsg, function(index, val) {
                    $('<span class="invalid">' + val + '</span>').insertAfter($('#' + index));
                });
            } else {
                console.log("error - unknown server issue" + error);
            }
        },
        complete: function() {
            // Hide the loader widget
        }
    });
}

function signOut() {
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
      console.log('User signed out.');
//      document.getElementById("user").innerHTML = "";
      document.getElementById("footer").hide();
    });
  }

function require(script) {
    $.ajax({
        url: script,
        dataType: "script",
        async: false,           // <-- This is the key
        success: function () {
            // all good...
        },
        error: function () {
            throw new Error("Could not load script " + script);
        }
    });
}

function redirect(path)
{		
//	window.location
	//location.replace(path);   
}

var profile;

function onSignIn(googleUser) {
  // Useful data for your client-side scripts:
  profile = googleUser.getBasicProfile();
  console.log("ID: " + profile.getId()); // Don't send this directly to your server!
  console.log("Name: " + profile.getName());
  console.log("Image URL: " + profile.getImageUrl());
  console.log("Email: " + profile.getEmail());

  // The ID token you need to pass to your backend:
  var id_token = googleUser.getAuthResponse().id_token;
  console.log("ID Token: " + id_token);

//  document.getElementById("user").innerHTML =  profile.getName() ;

  document.getElementById("introtext").innerHTML = " ";
 

  var data = { "id_token":""+id_token+"" };
         
  sendToken( data );
//  document.getElementById("footer").show();

  
};

