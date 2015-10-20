require("https://apis.google.com/js/platform.js");
require("js/libs/sha512.js");
// require("js/fusioncharts.js");
// require("js/themes/fusioncharts.theme.fint.js");

var serviceRootUrl = "https://kontoagent.ddns.net/notifier/";

/* Builds the updated table for the member list */
function buildMemberRows(members) {
	return _.template($("#member-tmpl").html(), {
		"members" : members
	});
}

function updateNotifiersTable() {
	// Display the loader widget
	$.mobile.loading("show");

	$.ajax({
		url : serviceRootUrl + "rest/notifier",
		cache : false,
		success : function(data) {
			$("#notifiers").empty().append(buildNotifiersRows(data));
			$("#notifiers-table").table("refresh");
		},
		error : function(error) {
			reLoginOnTimedOutSession(error);
			console.log("error updating table -" + error.status);
		},
		complete : function() {
			// Hide the loader widget
			$.mobile.loading("hide");

		}
	});
}

/* Builds the updated table for the member list */
function buildNotifiersRows(data) {
	return _.template($("#notifiers-tmpl").html(), {
		"data" : data
	});
}

function updatekontosTable() {
	// Display the loader widget
	$.mobile.loading("show");

	$.ajax({
		url : serviceRootUrl + "rest/konto",
		cache : false,
		success : function(data) {
			$("#kontos").empty().append(buildkontosRows(data));
			$("#kontos-table").table("refresh");
		},
		error : function(error) {
			reLoginOnTimedOutSession(error);
			console.log("error updating table -" + error.status);
		},
		complete : function() {
			// Hide the loader widget
			$.mobile.loading("hide");

		}
	});
}

/* Builds the updated table for the member list */
function buildkontosRows(data) {
	return _.template($("#kontos-tmpl").html(), {
		"data" : data
	});
}

function getKontos() {

	$select = $('#kontoSelection');
	$selectAomat = $('#aomat-kontoSelection');
	$.ajax({
		url : serviceRootUrl + "rest/konto",
		cache : false,
		success : function(data) {
			console.log(" ------------ -" + data);

			$select.html('');

			$.each(data, function(key, val) {
				console.log(" ------------ -" + val.account + "  --------"
						+ val.id);
				$select.append(new Option(val.account, val.id));
				$selectAomat.append(new Option(val.account, val.id));
			})
			return data;
		},
		error : function(error) {
			reLoginOnTimedOutSession(error);
			console.log("error updating table -" + error.status);
		},
		complete : function() {
			// Hide the loader widget
			// $.mobile.loading("hide");
		}
	});
}

/* Uses JAX-RS GET to retrieve current member list */
function getNotifiers() {

	$.ajax({
		url : serviceRootUrl + "rest/notifier",
		cache : false,
		success : function(data) {
			console.log(" ------------ -" + data);
			$("#nots").append(buildNotsRows(data));
			$("#nots-table").table("refresh");
			return data;
		},
		error : function(error) {
			reLoginOnTimedOutSession(error);
			// console.log("error updating table -" + error.status);
		},
		complete : function() {
			// Hide the loader widget
			// $.mobile.loading("hide");
		}
	});
}

/* Builds the updated table for the member list */
function buildNotsRows(data) {
	console.log(" ------------ -" + data);
	res = _.template($("#nots-tmpl").html(), {
		"data" : data
	});
	return res;
}

/* for google oauth */
function reLoginOnTimedOutSession(error) {

	if (gUser != null) {
		if (error.status == 401) {
			onSignIn(gUser);
		}
	}
}

function saveKonto(data) {
	// clear existing msgs
	$('span.invalid').remove();
	$('span.success').remove();

	// Display the loader widget
	$.mobile.loading("show");

	$
			.ajax({
				url : serviceRootUrl + "rest/konto",
				contentType : "application/json",
				dataType : "json",
				type : "POST",
				data : data,
				success : function(data) {
					// console.log("Member registered");

					// clear input fields
					$('#konto-reg')[0].reset();

					$('#konto-formMsgs')
							.append(
									$('<span class="success"> Kontodaten gespeichert.</span>'));

					updatekontosTable();
				},
				error : function(error) {
					reLoginOnTimedOutSession(error);
					if ((error.status == 409) || (error.status == 400)) {
						// console.log("Validation error registering user!");

						var errorMsg = $.parseJSON(error.responseText);

						$.each(errorMsg, function(index, val) {
							$('<span class="invalid">' + val + '</span>')
									.insertAfter($('#' + index));
						});
					} else {
						// console.log("error - unknown server issue");
						$('#konto-formMsgs')
								.append(
										$('<span class="invalid">Unknown server error</span>'));
					}
				},
				complete : function() {
					// Hide the loader widget
					$.mobile.loading("hide");
				}
			});
}

function saveFilter(data) {
	// clear existing msgs
	$('span.invalid').remove();
	$('span.success').remove();

	// Display the loader widget
	$.mobile.loading("show");

	$
			.ajax({
				url : serviceRootUrl + "rest/notifier",
				contentType : "application/json",
				dataType : "json",
				type : "POST",
				data : data,
				success : function(data) {
					// console.log("Member registered");

					// clear input fields
					$('#not-reg')[0].reset();

					// mark success on the registration form
					$('#not-formMsgs')
							.append(
									$('<span class="success">Info-Service gespeichert.</span>'));

					updateNotifiersTable();
				},
				error : function(error) {
					reLoginOnTimedOutSession(error);
					if ((error.status == 409) || (error.status == 400)) {
						// console.log("Validation error registering user!");

						var errorMsg = $.parseJSON(error.responseText);

						$.each(errorMsg, function(index, val) {
							$('<span class="invalid">' + val + '</span>')
									.insertAfter($('#' + index));
						});
					} else {
						// console.log("error - unknown server issue");
						$('#not-formMsgs')
								.append(
										$('<span class="invalid">Unknown server error</span>'));
					}
				},
				complete : function() {
					// Hide the loader widget
					$.mobile.loading("hide");
				}
			});

}

// google oauth token
function sendToken(data) {

	$.ajax({
		url : serviceRootUrl + "rest/setToken",
		contentType : "application/json",
		dataType : "json",
		type : "POST",
		data : JSON.stringify(data),
		success : function(data) {
			console.log(" id_token sent");
			console.log("user name " + name);
			displayEmail(data);
		},
		error : function(error) {
			reLoginOnTimedOutSession(error);
			if ((error.status == 409) || (error.status == 400)) {
				// console.log("Validation error registering user!");

				var errorMsg = $.parseJSON(error.responseText);
				console.log("error - " + errorMsg);
				$.each(errorMsg, function(index, val) {
					$('<span class="invalid">' + val + '</span>').insertAfter(
							$('#' + index));
				});
			} else {
				console.log("error - unknown server issue" + error);
			}
		},
		complete : function() {
			// Hide the loader widget
		}
	});
}

function signOut() {
	var auth2 = gapi.auth2.getAuthInstance();
	auth2.signOut().then(function() {
		console.log('User signed out.');
		// document.getElementById("user").innerHTML = "";
		document.getElementById("footer").hide();
	});
}

function require(script) {
	$.ajax({
		url : script,
		dataType : "script",
		async : false, // <-- This is the key
		success : function() {
			// all good...
		},
		error : function() {
			throw new Error("Could not load script " + script);
		}
	});
}

function redirect(path) {
	// window.location
	// location.replace(path);
}

var profile;
var gUser;// google user

function onSignIn(googleUser) {

	gUser = googleUser;
	// Useful data for your client-side scripts:
	profile = googleUser.getBasicProfile();
	console.log("ID: " + profile.getId()); // Don't send this directly to your
	// server!
	console.log("Name: " + profile.getName());
	console.log("Image URL: " + profile.getImageUrl());
	console.log("Email: " + profile.getEmail());

	// The ID token you need to pass to your backend:
	var id_token = googleUser.getAuthResponse().id_token;
	console.log("ID Token: " + id_token);

	// document.getElementById("user").innerHTML = profile.getName() ;

	document.getElementById("introtext").innerHTML = " ";

	var data = {
		"id_token" : "" + id_token + ""
	};

	sendToken(data);
	// document.getElementById("footer").show();

};

function deleteKonto(id) {
	console.log("del konto id: " + id);
	$
			.ajax({
				url : serviceRootUrl + "rest/konto/deletekonto/" + id,
				contentType : "application/json",
				dataType : "json",
				type : "GET",
				data : id,
				success : function() {

				},
				error : function(error) {
					reLoginOnTimedOutSession(error);
					if ((error.status == 409) || (error.status == 400)) {
						// console.log("Validation error registering user!");

						var errorMsg = $.parseJSON(error.responseText);

						$.each(errorMsg, function(index, val) {
							$('<span class="invalid">' + val + '</span>')
									.insertAfter($('#' + index));
						});
					} else {
						// console.log("error - unknown server issue");
						$('#kontos-formMsgs')
								.append(
										$('<span class="invalid">Unknown server error</span>'));
					}
				},
				complete : function() {
					// Hide the loader widget
					$.mobile.loading("hide");

				}
			});
	updateNotifiersTable();
}

function deleteFilter(id) {
	console.log("del filter id: " + id);
	$
			.ajax({
				url : serviceRootUrl + "rest/notifier/deletefilter/" + id,
				contentType : "application/json",
				dataType : "json",
				type : "GET",
				data : id,
				success : function() {
					updateNotifiersTable();
				},
				error : function(error) {
					reLoginOnTimedOutSession(error);
					if ((error.status == 409) || (error.status == 400)) {
						// console.log("Validation error registering user!");

						var errorMsg = $.parseJSON(error.responseText);

						$.each(errorMsg, function(index, val) {
							$('<span class="invalid">' + val + '</span>')
									.insertAfter($('#' + index));
						});
					} else {
						// console.log("error - unknown server issue");
						$('#kontos-formMsgs')
								.append(
										$('<span class="invalid">Unknown server error</span>'));
					}
				},
				complete : function() {
					// Hide the loader widget
					$.mobile.loading("hide");
				}
			});
}

function registerUser() {

	var pswd = $('#reg-password').val();
	var hash = CryptoJS.SHA512(pswd);
	$('#reg-password').val(hash);
	var data = JSON.stringify($('#registration-form').serialize());

	// clear existing msgs
	$('span.invalid').remove();
	$('span.success').remove();

	// Display the loader widget
	$.mobile.loading("show");

	$.ajax({
		url : serviceRootUrl + "rest/user",
		contentType : "application/json",
		dataType : "json",
		type : "POST",
		data : data,
		success : function(data) {
			// console.log("data");
			$('#registration-form')[0].reset();
			displayEmail(data);
			window.document.location.href = "/notifier/#kontos-art";
		},
		error : function(error) {
			$('#registration-formMsgs').append(
					'<span class="invalid">' + error.responseText + '</span>');
		},
	});

	$.mobile.loading("hide");
}

function displayEmail(data) {
	$("#username1").empty().append(data.email);
	$("#username2").empty().append(data.email);
	$("#username3").empty().append(data.email);
	$("#username4").empty().append(data.email);
	$("#username5").empty().append(data.email);
	$("#username6").empty().append(data.email);
}

function login() {
	var pswd = $('#password').val();
	var hash = CryptoJS.SHA512(pswd);
	$('#password').val(hash);
	var data = JSON.stringify($('#login-form').serialize());

	// clear existing msgs
	$('span.invalid').remove();
	$('span.success').remove();

	// Display the loader widget
	$.mobile.loading("show");

	$.ajax({
		url : serviceRootUrl + "rest/login",
		contentType : "application/json",
		dataType : "json",
		type : "POST",
		data : data,
		success : function(data) {
			// console.log("data");
			$('#login-form')[0].reset();
			// $("/notifier/index.html#login-article").click();
			displayEmail(data);
			window.document.location.href = "/notifier/#kontos-art";
			getKontos();
		},
		error : function(error) {
			$('#login-formMsgs').append(
					'<span class="invalid">' + error.responseText + '</span>');
		},
		complete : function() {
			// Hide the loader widget
		}
	});
	$.mobile.loading("hide");
	
}

function getKontoAusz(id) {
	$
			.ajax({
				url : serviceRootUrl + "rest/kontoauzug", // + id,
				contentType : "application/json",
				dataType : "json",
				type : "GET",
				data : id,
				success : function(data) {
					$("#kontoauszuege").empty().append(
							data.subject + "  <br><br><br>  " + data.message);

				},
				error : function(error) {
					reLoginOnTimedOutSession(error);
					if ((error.status == 409) || (error.status == 400)) {
						// console.log("Validation error registering user!");

						var errorMsg = $.parseJSON(error.responseText);

						$.each(errorMsg, function(index, val) {
							$('<span class="invalid">' + val + '</span>')
									.insertAfter($('#' + index));
						});
					} else {
						// console.log("error - unknown server issue");
						$('#kontoausz-Msgs')
								.append(
										$('<span class="invalid">Unknown server error</span>'));
					}
				},
				complete : function() {
					// Hide the loader widget
					$.mobile.loading("hide");

				}
			});

}

function loadGauge() {

	// Display the loader widget
	$.mobile.loading("show");
	$.ajax({
		url : serviceRootUrl + "rest/overplus",
		contentType : "application/json",
		dataType : "json",
		type : "GET",
		success : function(data) {
			console.log("data");
			drowGouge(data)
		},
		error : function(error) {
			$('#registration-formMsgs').append(
					'<span class="invalid">' + error.responseText + '</span>');
		},
	});

	$.mobile.loading("hide");
}

function drowGouge(overplus) {
	

		var redMin = 0;
		var redMax = overplus.moeglicheAusgabenProTag * 0.3;
		var yellowMin = overplus.moeglicheAusgabenProTag * 0.3;
		var yellowMax = overplus.moeglicheAusgabenProTag * 0.6;
		var greenMin = overplus.moeglicheAusgabenProTag * 0.6;
		var greenMax = overplus.moeglicheAusgabenProTag;

		var csatGauge = new FusionCharts({
			"type" : "angulargauge",
			"renderAt" : "chart-container",
			"width" : "300",
			"height" : "200",
			"dataFormat" : "json",
			"dataSource" : {
				"chart" : {
					"caption" : "Ausgabomat",
					"subcaption" : " ",
					"lowerLimit" : "0",
					"upperLimit" : "overplus.einnahmen",
					"theme" : "fint"
				},
				"colorRange" : {
					"color" : [ {
						"minValue" : redMin,
						"maxValue" : redMax,
						"code" : "#e44a00"
					}, {
						"minValue" : yellowMin,
						"maxValue" : yellowMax,
						"code" : "#f8bd19"
					}, {
						"minValue" : greenMin,
						"maxValue" : greenMax,
						"code" : "#6baa01"
					} ]
				},
				"dials" : {
					"dial" : [ {
						"value" : overplus.moeglicheAusgabenProTag + overplus.bisherigeAusgabenProTag
					} ]
				}
			}
		});

		csatGauge.render();

}
