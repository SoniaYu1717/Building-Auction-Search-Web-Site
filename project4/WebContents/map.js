function isEmpty(str){
    return (!str || str.length === 0);
}

var latlng;
var address;
var myOptions;
var map;

function initialize(my_place, my_latitude, my_longitude){

    latlng = null;
    myOptions = {
        zoom: 1,
        center: new google.maps.LatLng(0, 0),
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    address = my_place;
    if(!isEmpty(my_latitude) && !isEmpty(my_longitude)){
        latlng = new google.maps.LatLng(my_latitude, my_longitude);
    }

    var geocoder = new google.maps.Geocoder();
    geocoder.geocode({'location': latlng}, function(results, status){
        if(status == google.maps.GeocoderStatus.OK){
            console.log("successful!");
              myOptions = {
                zoom: 8,
                center: results[0].geometry.location,
                mapTypeId: google.maps.MapTypeId.ROADMAP
              };
            map = new google.maps.Map(document.getElementById("map"), myOptions);

            map.setCenter(results[0].geometry.location);
            var marker = new google.maps.Marker({
                map: map,
                position: results[0].geometry.location
            });
        }else{
            var geocoder_new = new google.maps.Geocoder();
            geocoder_new.geocode({'address': address}, function(results_new, status_new){
                if(status_new == google.maps.GeocoderStatus.OK){
                  console.log("successful!");
                  myOptions = {
                    zoom: 8,
                    center: results_new[0].geometry.location,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                  };
                  map = new google.maps.Map(document.getElementById("map"), myOptions);
                    map.setCenter(results_new[0].geometry.location);
                    var marker = new google.maps.Marker({
                        map: map,
                        position: results_new[0].geometry.location
                    });
                }
                else{
                    console.log("Geocode was not successful for the following reason: " + status_new);
                    map = new google.maps.Map(document.getElementById("map"), myOptions);
                }
            });
        }

    });

}

