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
}

/* Toggle the display of the popup box to change profile */
function togglePopup() {
  document.getElementById("popup-profile").classList.toggle("active");
  //pre-fill text area by the old user details
  document.getElementById("name").innerHTML = "old name";
  document.getElementById("department").innerHTML ="old department";
  document.getElementById("bio").innerHTML ="old bio";
}
