//Virginia Reel song plays for 20 seconds
var hasDialogue = '';
function playVirginiaReel(hasDialogue, played) {
if(played === false)
{
	var sound = new AR.Sound("assets/virginia_reel_20secs.mp3", {
	  onLoaded : function(){sound.play();},
	  onError : function(){
	    // alert the user that the sound file could not be loaded - or not because they wouldnt know
	    },
	});
	sound.load();
	sound.play();
	sound.onFinishedPlaying = function() {
    	if (hasDialogue) {
    		$("#virginiareel").dialog("open");
    	}
	};
}
}

// =====================================================================
// enable to tap outside the modal dialogue to close it
$(document.body).on("click", ".ui-widget-overlay", function() {
    $.each($(".ui-dialog"), function() {
        var $dialog;
        $dialog = $(this).children(".ui-dialog-content");
        if($dialog.dialog("option", "modal")) {
            $dialog.dialog("close");
        }
    });
});

// Dialogue window buttons appended below the intro text
// ======================================================================
// App intro questions/answers
 $("#appintromain").dialog({
    autoOpen: false,
	modal: true,
	resizable: true,
	buttons: {
		"Tell Me More...": function() {
		    $(this).dialog("close");
			$("#appintro1").dialog("open");
		},
		"Is there a band playing?": function() {
			$(this).dialog("close");
			$("#bandinfo").dialog("open");
		},
		"What is this application?": function() {
			$(this).dialog("close");
			$("#appintro2").dialog("open");
		},
		"Start the Tour...": function() {
			$(this).dialog("close");
		}
	}
});

$("#appintro1").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return to Invitation": function() {
			$(this).dialog("close");
			$("#appintromain").dialog("open");
		}
	}
});

$("#bandinfo").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return to Invitation": function() {
			$(this).dialog("close");
			$("#appintromain").dialog("open");
		}
	}
});

$("#appintro2").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return to Invitation": function() {
			$(this).dialog("close");
			$("#appintromain").dialog("open");
		}
	}
});

// ======================================================================
//Bridget Larkins, hallway maid questions/answers
 $("#bridgetmain").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Tell me, how late will you have guests?": function() {
		    $(this).dialog("close");
			$("#bridget1").dialog("open");
		},
		"I hear an accent. Where are you from originally?": function() {
			$(this).dialog("close");
			$("#bridget2").dialog("open");
		},
		"How long have you been employed by the Harts?": function() {
			$(this).dialog("close");
			$("#bridget3").dialog("open");
		},
		"Did you work all day to prepare for this party?": function() {
			$(this).dialog("close");
			$("#bridget4").dialog("open");
		}
	}
});
	
$("#bridget1").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#bridgetmain").dialog("open");
		}
	}
});
	
$("#bridget2").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#bridgetmain").dialog("open");
		}
	}
});

$("#bridget3").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#bridgetmain").dialog("open");
		}
	}
});

$("#bridget4").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#bridgetmain").dialog("open");
		}
	}
});

// ======================================================================
//Sitting room desserts and champagne questions/answers
$("#sittingroommain").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"What desserts are popular right now?": function() {
		    $(this).dialog("close");
			$("#dessert1").dialog("open");
		},
		"What happened to Sitting Rooms?": function() {
			$(this).dialog("close");
			$("#architecture").dialog("open");
		},
		"How is champagne made these days?": function() {
		    $(this).dialog("close");
		    $("#champagne1").dialog("open");
		},
		"Why is champagne so popular?": function() {
		    $(this).dialog("close");
		    $("#champagne2").dialog("open");
		}
	}
});

$("#dessert1").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#sittingroommain").dialog("open");
		}
	}
});
$("#architecture").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#sittingroommain").dialog("open");
		}
	}
});
$("#champagne1").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#sittingroommain").dialog("open");
		}
	}
});
$("#champagne2").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#sittingroommain").dialog("open");
		}
	}
});

// ======================================================================
// Parlor - Maria Tillman questions/answers

$("#maria").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Can't you ask Mr. Hart for a dance yourself?": function() {
		    $(this).dialog("close");
		    $("#maria1").dialog("open");
		},
		"What's so special about a couple's dance?": function() {
		    $(this).dialog("close");
		    $("#maria2").dialog("open");
		}
	}
});

$("#maria1").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#maria").dialog("open");
		}
	}
});
	
$("#maria2").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#maria").dialog("open");
		}
	}
});

// ======================================================================
// Parlor - Billy Burden questions/answers

$("#billy").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"What is the Virginia Reel?": function() {
		    $(this).dialog("close");
		    $("#billy1").dialog("open");
		},
		"How late will we dance tonight?": function() {
		    $(this).dialog("close");
		    $("#billy2").dialog("open");
		}
	}
});

$("#billy1").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#billy").dialog("open");
		}
	}
});
	
$("#billy2").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#billy").dialog("open");
		}
	}
});


// ======================================================================
//Richard Hart Jr. in the dining room
$("#richardhartmain").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"What is your occupation?": function() {
			$(this).dialog("close");
			$("#richardhart1").dialog("open");
		},
		"Tell me about your parents": function() {
			$(this).dialog("close");
			$("#richardhart2").dialog("open");
		},
		"Mrs. Hart has been supportive of education, especially for women?": function() {
			$(this).dialog("close");
			$("#richardhart3").dialog("open");
		}
	}
});

$("#richardhart1").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#richardhartmain").dialog("open");
		}
	}
});

$("#richardhart2").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#richardhartmain").dialog("open");
		}
	}
});

$("#richardhart3").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#richardhartmain").dialog("open");
		}
	}
});

// ======================================================================
//Food items and Receipt, dining room
$("#diningfoodmain").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Tell me more about the food and drinks": function() {
		    $(this).dialog("close");
			$("#diningfood1").dialog("open");
		},
		"How much did a Ball like this cost in 1850?": function() {
			$(this).dialog("close");
			$("#ballcost").dialog("open");
		},
		"View the actual receipt from the caterer": function() {
			$("#partyreceipt").dialog("open");
		}
	}
});
		
$("#diningfood1").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#diningfoodmain").dialog("open");
		}
	}
});


$("#ballcost").dialog({
    autoOpen: false,
	modal: true,
	resizable: false,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#diningfoodmain").dialog("open");
		}
	}
});	

//Actual receipt from caterer, provided by RCHS
$("#partyreceipt").dialog({
    autoOpen: false,
	modal: true,
	resizable: true,
	buttons: {
		"Return": function() {
			$(this).dialog("close");
			$("#diningfoodmain").dialog("open");
		}
	}
});
