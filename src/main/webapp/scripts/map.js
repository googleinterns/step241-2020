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
  ['Places to Visit', 'Sky Garden', 51.510881, -0.083751, 'Sky Garden is a great place to visit, with really good views of London. \
  You can enjoy the 360 degree view of the city - for free! They are public gardens and definitely worth a visit \
  when you have the time. Remember to keep socially distanced!'],
  ['Restaurants', 'Mildreds Kings Cross', 51.531299, -0.11716, 'A lovely little place to grab something to eat! There are a lot of \
  different options and a great atmosphere inside Mildred\'s!'],
  ['Bars and Clubs', 'Fitz\'s Bar', 51.517735, -0.097112, 'This is a recommendation for Fitz\'s Bar in London.'],
  ['Study Places', 'Bloomsbury Coffee House', 51.525212, -0.126469, 'A really great place to study!']
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

let map;

/* Set Up Map */
function initMap() { 
  /* 'Hard Coded' origin, Google UK Pancras Square */
  const origin = {
    lat: 51.533364, 
    lng: -0.125777
  }
  
  map = new google.maps.Map(document.getElementById("map"), {
    center: origin,
    zoom: 15
  });
  
  /* Get all stored markers */
  fetchMarkers();

  map.addListener("click", e => {
    placeMarkerAndPanTo(e.latLng);
  });
  
  /* Add Hard-Coded Markers */
  addHardCodedMarkers();
}

function addHardCodedMarkers() {
  /* Hard-Coded Marker Data */
  var placeName;
  var placeLatLng;
  var placeCategory;
  var placeRecommendation;

  /* Iterate through stored recommendations to get details */
  for (i = 0; i < recommendations.length; i++) {
    var rec = recommendations[i]
    placeCategory = rec[0];
    placeName = rec[1];
    placeLatLng = new google.maps.LatLng(rec[2], rec[3]);
    placeRecommendation = rec[4];
    const marker = new google.maps.Marker({
      position: placeLatLng,
      map: map,
      title: placeName,
      icon: getColourMarker(placeCategory)
    })
    /* Add Listener for Click on Marker */
    google.maps.event.addListener(marker, "click", () => {
    /* Update the HTML */
    var markerRec = getRecommendationDetails(marker.title);
    document.getElementById("category-header").innerHTML = markerRec[0];
    document.getElementById("category-header").style.backgroundColor = getBackgroundColour(markerRec[0]);
    document.getElementById("place-title").innerHTML = markerRec[1];
    document.getElementById("rec-address").innerHTML = new google.maps.LatLng(markerRec[2], markerRec[3]);
    document.getElementById("place-recommendation").innerHTML = markerRec[4];
    document.getElementById("rec-container").style.display = "block";
    /* Adjust the map settings */
    map.setZoom(16);
    map.setCenter(marker.getPosition());
   });
  }
}

function getRecommendationDetails(name) {
  /* Iterate through stored recommendations to get details */
  for(i = 0; i<recommendations.length; i++) {
    var rec = recommendations[i];
    if (rec[1] == name) {
      return rec;
    }
  }
  return null;
}

function getBackgroundColour(category) {
  switch(category) {
    case 'Restaurants':
      return "#ffba04";
    case 'Places to Visit':
      return "#21b5b5";
    case 'Bars and Clubs':
      return "#a73f9b";
    case 'Study Places':
      return "#ff5b5b";
    default:
      return "";
  } 
}

/* Returns the Coloured Marker for Category */
function getColourMarker(category) {
  switch(category) {
    case 'Restaurants':
      return yellowIcon;
    case 'Places to Visit':
      return blueIcon;
    case 'Bars and Clubs':
      return purpleIcon;
    case 'Study Places':
      return redIcon;
    default:
      return "";
  }
}

/* Place Marker Where Map is Clicked On & Show Popup*/
function placeMarkerAndPanTo(latLng) {
  /* Make marker for the clicked location */
  const marker = new google.maps.Marker({
    position: latLng,
    map: map,
    icon: greyIcon
  });
  /* Recenter the Map */
  map.panTo(latLng);
  /* Show the popup box */
  togglePopup(latLng);
  /* Update the latitude and longitude values in the popup */
  populateLocation(marker.getPosition());

  /* If marker is right-clicked, delete */
  google.maps.event.addListener(marker, "rightclick", function(event) {
    marker.setMap(null);
  });
}

/* Set the PopUp to Active */
function togglePopup(latLng) {
  document.getElementById("popup-add-recs").classList.toggle("active");
  document.getElementById("submit-recommendation").addEventListener("click", () =>
    postMarker(latLng), false);
}

/* Use the position of marker on map to auto-fill location */
function populateLocation(pos) {
  var location = pos.lat() + ", " + pos.lng();
  document.getElementById("location").value = location;
}

/* POST marker */
function postMarker(latLng) {
  const params = new URLSearchParams();
  params.append('lat', latLng.lat());
  params.append('lng', latLng.lng());
  fetch('/add-marker', {
    method: 'POST', 
    body: params
  });
}

/* Fetch all markers from datastore and add to map*/
function fetchMarkers() {
  fetch('/all-markers')
  .then(response => response.json())
  .then((markers) => {
    markers.forEach((marker) => {
      placeMarkerAndPanTo(new google.maps.LatLng(marker.lat, marker.lng))
    });
  });
}
