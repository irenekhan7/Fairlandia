//RCHS 1850 Ball
$(document).ready(function() {
	var World = {
		loaded: false,
	
		init: function initFn() {
			/* Disable all sensors in "IR-only" Worlds to save performance. If the property is set to true, any geo-related components (such as GeoObjects and ActionRanges) are active. If the property is set to false, any geo-related components will not be visible on the screen, and triggers will not fire.*/
			AR.context.services.sensors = false;
			this.createOverlays();
		},
	
		createOverlays: function createOverlaysFn() {
			// Initialize Tracker images for four rooms
			// Important: If you replace the tracker file with your own, make sure to change the target name accordingly.
			// e.g. replace "pageOne" used for creating the AR.Trackeable2DOBject below, with the name of one of your new target images.
			this.tracker = new AR.Tracker("assets/four_rooms.wtc", {
				onLoaded: this.worldLoaded
			});

			// App Intro Button image - might not work here
/*			this.imgButton = new AR.ImageResource("assets/btnViewInvitation.jpg");	
			var btnOpenIntro = this.createWwwButton(function() {$("#appintromain").dialog("open");};, 0.1, {
				offsetX: -0.25,
				offsetY: -0.25,
				zOrder: 1
			});
*/	
			// Create a overlay maid for hallway
			var imgHallwayMaid = new AR.ImageResource("assets/maid_hallway.png");
			var overlayHallwayMaid = new AR.ImageDrawable(imgHallwayMaid, 1, {
				offsetX: 0.05,
				offsetY: 0.05,
				scale: 0.7,
				opacity: 0.6
			});
			//Bridget Larkins hallway maid dialog window
			overlayHallwayMaid.onClick = function() {
	        	$("#bridgetmain").dialog("open");	
			};
			//render the hallway with the maid overlay
			var pageHallway = new AR.Trackable2DObject(this.tracker, "1_hallway", {
				drawables: {
					cam: overlayHallwayMaid
				}
			});
	
			// Sitting room - dessert and champagne item
			var imgSittingDessert = new AR.ImageResource("assets/fooddrink/champagne_dessert.png");
			var overlaySittingDessert = new AR.ImageDrawable(imgSittingDessert, 1, {
				offsetX: 0.07,
				offsetY: -0.01,
				scale: 0.4
			});
			//dessert dialog window
			overlaySittingDessert.onClick = function() {
	        	$("#sittingroommain").dialog("open");	
			};
			//render the sitting room with dessert overlay
			var pageSitting = new AR.Trackable2DObject(this.tracker, "2_drawingroom", {
				drawables: {
					cam: overlaySittingDessert
				}
			});
				
			// Front parlor - Create overlay Maria Tillman
			var imgParlorLady = new AR.ImageResource("assets/parlor_mariatillman.png");
			var overlayParlorLady = new AR.ImageDrawable(imgParlorLady, 1, {
				offsetX: 0.2,
				offsetY: 0.01,
				opacity: 0.7,
				scale: 0.7,
				zOrder: 1
			});
			// Front parlor - Create overlay drunk Billy Burden
			var imgParlorBilly = new AR.ImageResource("assets/parlor_billyburden.png");
			var overlayParlorBilly = new AR.ImageDrawable(imgParlorBilly, 1, {
				offsetX: 0.05,
				offsetY: -0.1,
				opacity: 0.6,
				scale: 0.4,
				zOrder: -1
			});
			//Maria Tillman dialog window
			overlayParlorLady.onClick = function() {
	        	$("#maria").dialog("open");	
			};
			//Billy dialog window
			overlayParlorBilly.onClick = function() {
	        	$("#billy").dialog("open");	
			};
			//render the front parlor with overlays
			//Virginia Reel song plays for 20 seconds when user finds marker image
			var played = false;
			var pageParlorDance = new AR.Trackable2DObject(this.tracker, "3_parlor_front", {
				drawables: {
					cam: [overlayParlorBilly, overlayParlorLady]
				},
				onEnterFieldOfVision: function() {
				playVirginiaReel('', played);
				played = true; 
				}
			});
			
			// Dining room - Create overlay Richard Hart Jr. and food items
			var imgRichardHart = new AR.ImageResource("assets/richardhart.png");
			var overlayRichardHart = new AR.ImageDrawable(imgRichardHart, 1, {
				offsetX: 0.17,
				offsetY: -0.1,
				scale: 0.9,
				opacity: 0.7
			});
			var imgDiningFood = new AR.ImageResource("assets/fooddrink/oysters.png");
			var overlayDiningFood = new AR.ImageDrawable(imgDiningFood, 1, {
				offsetX: -0.17,
				offsetY: -0.1,
				scale: 0.3
			});

			//render the dining room with overlays
			var pageDiningRoom = new AR.Trackable2DObject(this.tracker, "4_diningroom", {
				drawables: {
					cam: [overlayDiningFood, overlayRichardHart]
				}
			});
			//Richard Hart dialog window
			overlayRichardHart.onClick = function() {
	        	$("#richardhartmain").dialog("open");	
			};
			//Dining Room food dialog window
			overlayDiningFood.onClick = function() {
	        	$("#diningfoodmain").dialog("open");	
			};
		},
		//thumbnail of the target image
		worldLoaded: function worldLoadedFn() {
			var cssDivLeft = " style='display: table-cell;vertical-align: middle; text-align: right; width: 50%; padding-right: 15px;'";
			var cssDivRight1 = " style='display: table-cell;vertical-align: middle; text-align: left; padding-right: 15px; width: 38px'";
			var cssDivRight2 = " style='display: table-cell;vertical-align: middle; text-align: left; padding-right: 15px;'";
			document.getElementById('loadingMessage').innerHTML =
			"<div" + cssDivLeft + ">Scan a room to begin</div>" +
				"<div" + cssDivRight1 + ">(view images)</div>";
		}
	};
	
	World.init();
});
