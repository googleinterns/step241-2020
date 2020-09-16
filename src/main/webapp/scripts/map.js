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
function initMap(category) { 
  /* 'Hard Coded' origin, Google UK Pancras Square */
  const origin = {
    lat: 51.533364, 
    lng: -0.125777
  };
  const map = new google.maps.Map(document.getElementById("map"), {
    center: origin,
    zoom: 15
  });
  
  /* Get all stored recommendation lat, lng and id */
  fetchAndPlaceMarkers(map);

  map.addListener("click", e => {
    placeMarkerAndPanTo(e.latLng, map);
  });

  /* Filter out the recommendations by category if a category parameter is passed */
  /* this is the case when a category button is clicked */
  if (category) {
    addMarker(map, recommendations.filter(recommendation => recommendation.category === category));
  }
  /* Add all recommendations if there is no parameter given (when loading the page) */
  else {
    addMarker(map, recommendations);
  }
}

/* Add Hard-Coded Markers to map */
function addMarker(map, recommendationsToAdd) {
  /* Iterate through stored recommendationsToAdd to get details */
  for (i = 0; i < recommendationsToAdd.length; i++) {
    const recommendation = recommendationsToAdd[i];
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
      return "#ffba04"; // yellow
    case 'Places to Visit':
      return "#21b5b5"; // blue
    case 'Bars and Clubs':
      return "#a73f9b"; // purple
    case 'Study Places':
      return "#ff5b5b"; // red
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
  /* Recenter the Map */
  map.panTo(latLng);
  togglePopup();
  /* Update the latitude and longitude values in the popup */
  populateLocation(latLng);

  /* If marker is right-clicked, delete */
  google.maps.event.addListener(marker, "rightclick", function(event) {
    marker.setMap(null);
  });
}

/* Function to place markers from the datastore */
function placeMarker(markerDetails, map) {
  marker = new google.maps.Marker ({
    position: new google.maps.LatLng(markerDetails.lat, markerDetails.lng),
    map: map,
    icon: getColourMarker(formatCategory(markerDetails.category)),
    id: markerDetails.id
  });
  //marker.set("id", markerDetails.id);

  // TODO fix issue with listeners
  /* Add Listener for Click on Marker */
   google.maps.event.addListener(marker, "click", () => {
     fetchRecommendationInfo(marker.id);
   });
}

/* Get recommendation from datastore and fill html */
function fetchRecommendationInfo(id) {
  const params = new URLSearchParams();
  params.append('id', id);
  fetch("/recommendation?id=" + id).then(result => result.json()).then((recommendation) => {
    /* Update the HTML */
      document.getElementById("category-header").innerHTML = formatCategory(recommendation.category);
      document.getElementById("category-header").style.backgroundColor = getBackgroundColour(recommendation.category);
      document.getElementById("place-title").innerHTML = recommendation.name;
      document.getElementById("rec-address").innerHTML = recommendation.lat+", "+recommendation.lng;
      document.getElementById("place-recommendation").innerHTML = recommendation.description;
      document.getElementById("rec-container").style.display = "block";
  });
}

/* Formats the category string */
function formatCategory(category) {
  switch(category) {
    case 'restaurants':
      return "Restaurants";
    case 'places-to-visit':
      return "Places to Visit";
    case 'bars-and-clubs':
      return "Bars and Clubs";
    case 'study-places':
      return "Study Places";
    default:
      return "";
  }
}

/* Set the PopUp to Active */
function togglePopup() {
  document.getElementById("popup-add-recs").classList.toggle("active");
  // TODO clear list of events / previously added event listeners
}

/* Use the position of marker on map to auto-fill location */
function populateLocation(pos) {
  const location = pos.lat() + ", " + pos.lng();
  document.getElementById("location").value = location;
}

/* Fetch all markers from datastore and add to map*/
function fetchAndPlaceMarkers(map) {
  fetch('/all-markers')
  .then(response => response.json())
  .then((markers) => {
    markers.forEach((marker) => {
      placeMarker(marker, map);
    });
  });
}
