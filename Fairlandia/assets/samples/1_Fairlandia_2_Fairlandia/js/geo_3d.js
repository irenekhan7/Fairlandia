var World = {

	init: function initFn() {
		this.createPois();
	},

	createPois: function createPoisFn() {

		// Create 3D model drawable
		var earth = new AR.Model("assets/travellers.wt3", {
			scale: {
   			 x: 0.3, 
   			 y: 0.3,
  			 z: 0.3
  			}
		});

		//Initial example : 250,7° from current location of the user (about West-South-West)  -7 northing and -20 easting
		//Current example : 
		var geoLoc = new AR.GeoLocation(40.8848, 73.8597, 320);
		var loc = new AR.RelativeLocation(geoLoc, -2000, 0, -400);
        var obj = new AR.GeoObject(loc, {
            drawables: {
				cam: [earth]
			}
		});
	}
};

World.init();