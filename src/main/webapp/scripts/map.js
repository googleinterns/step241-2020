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

let recommendationMarkers = [];

/* Fetch all stored recommendation markers from datastore and initialize map. */
function init() {
  fetch('/all-markers').then(response => response.json()).then((markers) => {
    recommendationMarkers = markers;
    initMap();
  });
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

  map.addListener("click", e => {
    placeMarkerAndPanTo(e.latLng, map);
  });

  let markersToPlace = recommendationMarkers;
  /* Filter out the recommendation markers by category if a category parameter is passed */
  /* this is the case when a category button is clicked */
  recommendationMarkers.filter(marker => !category || formatCategory(marker.category) == category)
      .forEach(marker => placeMarker(marker, map));
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
  /* Update the latitude and longitude values in the popup */
  populateLocation(latLng);
  document.getElementById("close-button").onclick = () => closePopup(marker);
  togglePopup();
}

/* Function to place markers from the datastore */
function placeMarker(markerDetails, map) {
  const marker = new google.maps.Marker ({
    position: new google.maps.LatLng(markerDetails.lat, markerDetails.lng),
    map: map,
    icon: getColourMarker(formatCategory(markerDetails.category)),
    id: markerDetails.id
  });

  /* Add Listener for Click on Marker */
   marker.addListener("click", () => {
     fetchRecommendationInfo(marker.id);
   });
}

/* Get recommendation from datastore and fill html */
function fetchRecommendationInfo(id) {
  fetch("/recommendation?id=" + id).then(result => result.json()).then((recommendation) => {
    /* Update the HTML */
    const formattedCategory = formatCategory(recommendation.category);
    document.getElementById("category-header").innerHTML = formattedCategory;
    document.getElementById("category-header").style.backgroundColor = getBackgroundColour(formattedCategory);
    document.getElementById("place-title").innerHTML = recommendation.name;
    document.getElementById("rec-address").innerHTML = recommendation.lat + ", " + recommendation.lng;
    document.getElementById("place-recommendation").innerHTML = recommendation.description;
    document.getElementById("rec-container").style.display = "block";
  });
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

/* Formats the category string */
function formatCategory(category) {
  switch(category) {
    case "restaurants":
      return "Restaurants";
    case "places-to-visit":
      return "Places to Visit";
    case "bars-and-clubs":
      return "Bars and Clubs";
    case "study-places":
      return "Study Places";
    default:
      return "";
  }
}

/* Set the PopUp to Active */
function togglePopup() {
  document.getElementById("popup-add-recs").classList.toggle("active");
}

function closePopup(marker) {
  // delete greyIcon marker when popup box is closed
  marker.setMap(null);
  togglePopup();
}

/* Use the position of marker on map to auto-fill location */
function populateLocation(pos) {
  const location = pos.lat() + ", " + pos.lng();
  document.getElementById("location").value = location;
}
