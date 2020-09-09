// Copyright 2020 Google LLC
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

const skyGarden = {
  name: "Sky Garden", 
  category: "Places to Visit", 
  lat: 51.510881, 
  lng: -0.083751,
  description: "Sky Garden is a great place to visit, with really good views of London. \
      You can enjoy the 360 degree view of the city - for free! They are public gardens and \
      definitely worth a visit when you have the time. Remember to keep socially distanced!", 
  costRating: 1, 
  crowdRating: 4
};

const mildredsKingsCross = {
  name: "Mildreds Kings Cross", 
  category: "Restaurants", 
  lat: 51.531299, 
  lng:-0.11716,
  description: "A lovely little place to grab something to eat! There are a lot of \
      different options and a great atmosphere inside Mildred\'s!", 
  costRating: 1, 
  crowdRating: 1
};

const fitzBar = {
  name: "Fitz\'s Bar", 
  category: "Bars and Clubs", 
  lat: 51.517735, 
  lng: -0.097112, 
  description: "This is a recommendation for Fitz\'s Bar in London.",
  costRating: 5, 
  crowdRating: 3
};

const bloomsburyCoffeeHouse = {
  name: "Bloomsbury Coffee House", 
  category: "Study Places", 
  lat: 51.525212, 
  lng: -0.126469, 
  description: "A really great place to study!", 
  costRating: 2, 
  crowdRating: 3
};

/* Hard-coded recommendations */
const recommendations = [
  skyGarden, mildredsKingsCross, fitzBar, bloomsburyCoffeeHouse
];

/* Custom Markers */
const yellowIcon = {
  url: "/images/yellow-marker.png",
  scaledSize: new Image(30, 40)
};
const blueIcon = {
  url: "/images/blue-marker.png",
  scaledSize: new Image(30, 40)
};
const purpleIcon = {
  url: "/images/purple-marker.png", 
  scaledSize: new Image(30, 40)
};
const redIcon = {
  url: "/images/red-marker.png", 
  scaledSize: new Image(30, 40)
};
const greyIcon = {
  url: "/images/grey-marker.png",
  scaledSize: new Image(30, 40)
}

/* Set Up Map */
function initMap() { 
  /* 'Hard Coded' origin, Google UK Pancras Square */
  const origin = {
    lat: 51.533364, 
    lng: -0.125777
  };
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
  /* Iterate through stored recommendations to get details */
  for (i = 0; i < recommendations.length; i++) {
    const recommendation = recommendations[i];
    const placeCategory = recommendation.category;
    const placeName = recommendation.name;
    const placeLatLng = new google.maps.LatLng(recommendation.lat, recommendation.lng);
    const placeRecommendation = recommendation.description;
    /* TODO link the marker to the placeCost, placeCrowd, placeRecommendation (to store recommendation) */
    const marker = new google.maps.Marker({
      position: placeLatLng,
      map: map,
      title: placeName,
      icon: getColourMarker(placeCategory)
    });
    /* Add Listener for Click on Marker */
    google.maps.event.addListener(marker, "click", () => {
      /* Update the HTML */
      document.getElementById("category-header").innerHTML = placeCategory;
      document.getElementById("category-header").style.backgroundColor = getBackgroundColour(placeCategory);
      document.getElementById("place-title").innerHTML = placeName;
      document.getElementById("rec-address").innerHTML = placeLatLng;
      document.getElementById("place-recommendation").innerHTML = placeRecommendation;
      document.getElementById("rec-container").style.display = "block";
      /* Adjust the map settings */
      map.setZoom(16);
      map.setCenter(marker.getPosition());
    });
  }
}

function getBackgroundColour(category) {
  switch(category) {
    case 'Restaurants':
      return "#ffba04"; //yellow
    case 'Places to Visit':
      return "#21b5b5"; //blue
    case 'Bars and Clubs':
      return "#a73f9b"; //purple
    case 'Study Places':
      return "#ff5b5b"; //red
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
function placeMarkerAndPanTo(latLng, map) {
  const marker = new google.maps.Marker({
    position: latLng,
    map: map,
    icon: greyIcon
  });

  map.panTo(latLng);
  togglePopup();
  const markerPosition = marker.getPosition();
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
  const location = pos.lat() + ", " + pos.lng();
  document.getElementById("location").value = location;
}
