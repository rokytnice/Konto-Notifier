 

/* Builds the updated table for the member list */
function buildMemberRows(members) {
    return _.template( $( "#member-tmpl" ).html(), {"members": members});
}

/* Uses JAX-RS GET to retrieve current member list */
function updateMemberTable() {
    // Display the loader widget
    $.mobile.loading("show");

    $.ajax({
        url: "rest/konto",
        cache: false,
        success: function(data) {
            $( "#members" ).empty().append(buildMemberRows(data));
            $( "#member-table" ).table( "refresh" );
        },
        error: function(error) {
            //console.log("error updating table -" + error.status);
        },
        complete: function() {
            // Hide the loader widget
            $.mobile.loading("hide");
        }
    });
}

/* Uses JAX-RS GET to retrieve current member list */
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
        data: JSON.stringify(data),
        success: function(data) {
            //console.log("Member registered");

            //clear input fields
            $('#reg')[0].reset();

            $('#formMsgs').append($('<span class="success"> Kontodaten gespeichert.</span>'));

            updateMemberTable();
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
                $('#formMsgs').append($('<span class="invalid">Unknown server error</span>'));
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
        data: JSON.stringify(data),
        success: function(data) {
            //console.log("Member registered");

            //clear input fields
            $('#reg')[0].reset();

            //mark success on the registration form
            $('#formMsgs').append($('<span class="success">Info-Service gespeichert.</span>'));

            updateMemberTable();
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
                $('#formMsgs').append($('<span class="invalid">Unknown server error</span>'));
            }
        },
        complete: function() {
            // Hide the loader widget
            $.mobile.loading("hide");
        }
    });

}
