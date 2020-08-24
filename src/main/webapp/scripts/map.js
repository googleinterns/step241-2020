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

/* Custom Markers */
const yellowIcon = {
  url: "/images/yellow-marker.png", // url
  scaledSize: new google.maps.Size(30, 40), // scaled size
  origin: new google.maps.Point(0, 0), // origin
  anchor: new google.maps.Point(0, 0) // anchor
};

const blueIcon = {
  url: "/images/blue-marker.png", // url
  scaledSize: new google.maps.Size(30, 40), // scaled size
  origin: new google.maps.Point(0, 0), // origin
  anchor: new google.maps.Point(0, 0) // anchor
};

const purpleIcon = {
  url: "/images/purple-marker.png", // url
  scaledSize: new google.maps.Size(30, 40), // scaled size
  origin: new google.maps.Point(0, 0), // origin
  anchor: new google.maps.Point(0, 0) // anchor
};

const redIcon = {
  url: "/images/red-marker.png", // url
  scaledSize: new google.maps.Size(30, 40), // scaled size
  origin: new google.maps.Point(0, 0), // origin
  anchor: new google.maps.Point(0, 0) // anchor
};

/* Set Up Map */
function initMap() {
  /* 'Hard Coded' origin, Google UK Pancras Square*/
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
  /*Add Hard-Coded Markers */
  addMarker(map);
}

/* Temporary Function to Hold Marker for Sky Garden */
function addMarker(map) {
  const skyGarden = {lat: 51.510881, lng: -0.083751}
  const marker = new google.maps.Marker({
    position: skyGarden,
    map: map,
    title: "Sky Garden"
  })
  marker.addListener("click", () => {
    document.getElementById("rec-container").style.display = "block";
    map.setZoom(16);
    map.setCenter(marker.getPosition());
  });
}

/* Place Marker Where Map is Clicked On & Show Popup*/
function placeMarkerAndPanTo(latLng, map) {
  const greyIcon = {
    url: "/images/grey-marker.png", // url
    scaledSize: new google.maps.Size(30, 40), // scaled size
    origin: new google.maps.Point(0, 0), // origin
    anchor: new google.maps.Point(0, 0) // anchor 
  }
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
