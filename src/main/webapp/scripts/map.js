// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/* Global Icon Variables */
var yellowIcon;
var blueIcon;
var purpleIcon;
var redIcon;
var greyIcon;

/* Hard-coded Recommendations */
const recommendations = [
  ['Places to Visit', 'Sky Garden', '51.510881, -0.083751', 'Sky Garden is a great place to visit, with really good views of London. \
  You can enjoy the 360 degree view of the city - for free! They are public gardens and definitely worth a visit \
  when you have the time. Remember to keep socially distanced!'],
  ['Restaurants', 'Mildreds Kings Cross', '51.531299, -0.117168', 'A lovely little place to grab something to eat! There are a lot of \
  different options and a great atmosphere inside Mildred\'s!'],
  ['Bars and Clubs', 'Fitz\'s Bar', '51.517735, -0.097112', 'This is a recommendation for Fitz\'s Bar in London.'],
  ['Study Places', 'Bloomsbury Coffee House', '51.525212, -0.126469', 'A really great place to study!']
]

window.onload = function() {
  /* Custom Markers */
  yellowIcon = {
    url: "/images/yellow-marker.png",
    scaledSize: new google.maps.Size(30, 40)
  };
  blueIcon = {
    url: "/images/blue-marker.png",
    scaledSize: new google.maps.Size(30, 40)
  };
  purpleIcon = {
    url: "/images/purple-marker.png", 
    scaledSize: new google.maps.Size(30, 40)
  };
  redIcon = {
    url: "/images/red-marker.png", 
    scaledSize: new google.maps.Size(30, 40)
  };
  greyIcon = {
    url: "/images/grey-marker.png",
    scaledSize: new google.maps.Size(30, 40)
  }
  initMap();
}

/* Set Up Map */
function initMap() { 
  /* 'Hard Coded' origin, Google UK Pancras Square */
  const origin = {
    lat: 51.533364, 
    lng: -0.125777
  }
  const map = new google.maps.Map(document.getElementById("map"), {
    center: origin,
    zoom: 15
  });
  map.addListener("click", e => {
    placeMarkerAndPanTo(e.latLng, map);
  });
  
  /* Add Hard-Coded Markers */
  addMarker(map);
}

function addMarker(map) {
  /* Hard-Coded Marker Data */
  const mildredsKingsCross = new google.maps.Marker({
    position: {lat: 51.531299, lng: -0.117168},
    map: map,
    title: 'Mildreds Kings Cross',
    icon: yellowIcon
  });
  const skyGarden = new google.maps.Marker({
    position: {lat: 51.510881, lng: -0.083751},
    map: map,
    title: 'Sky Garden',
    icon: blueIcon
  })
  const fitzBar = new google.maps.Marker({
    position: {lat: 51.517735, lng: -0.097112},
    map: map,
    title: 'Fitz\'s Bar',
    icon: purpleIcon
  });
  const bloomsburyCoffeeHouse = new google.maps.Marker({
    position: {lat: 51.525212, lng: -0.126469},
    map: map,
    title: 'Bloomsbury Coffee House',
    icon: redIcon
  });
  
  waitForClick(map, mildredsKingsCross);
  waitForClick(map, skyGarden);
  waitForClick(map, fitzBar);
  waitForClick(map, bloomsburyCoffeeHouse);
}

function waitForClick(map, marker) {
  var placeName = marker.getTitle();
  var placeLongLat;
  var placeCategory;
  var placeRecommendation;

  /* Iterate through stored recommendations to get details */
  for (i = 0; i < recommendations.length; i++) {
    var rec = recommendations[i]
    if (rec[1] == placeName){
      placeCategory = rec[0];
      placeLongLat = rec[2];
      placeRecommendation = rec[3];
      break;
    }
  }

  marker.addListener("click", () => {
    /* Update the HTML */
    document.getElementById("category-header").innerHTML = placeCategory;
    document.getElementById("place-title").innerHTML = placeName;
    document.getElementById("rec-address").innerHTML = placeLongLat;
    document.getElementById("place-recommendation").innerHTML = placeRecommendation;
    document.getElementById("rec-container").style.display = "block";
    /* Adjust the map settings */
    map.setZoom(16);
    map.setCenter(marker.getPosition());
  });
}

/* Place Marker Where Map is Clicked On & Show Popup*/
function placeMarkerAndPanTo(latLng, map) {
  var marker = new google.maps.Marker({
    position: latLng,
    map: map,
    icon: greyIcon
  });

  map.panTo(latLng);
  togglePopup();
  var markerPosition = marker.getPosition();
  populateLocation(markerPosition);

  /* If marker is right-clicked, delete */
  google.maps.event.addListener(marker, 'rightclick', function(event) {
      marker.setMap(null);
  });
}

/* Set the PopUp to Active */
function togglePopup() {
  document.getElementById("popup-add-recs").classList.toggle("active");
}

/* Use the position of marker on map to auto-fill location */
function populateLocation(pos) {
  var location = pos.lat() + ", " + pos.lng();
  document.getElementById("location").value = location;
}
