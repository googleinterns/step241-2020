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

function loadProfile(redirectUrl){
  initMap();
  loadDetails();
  loadUploadUrl();
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
}

/* Load current user's profile details */
function loadDetails(){
  fetch("personal-data").then(result => result.json()).then((user) => {
    // Populate user details section
    const profilePictureHTML = "<img class=\"user-img\" src=\"" + user.profilePictureUrl + "\"></img>";
    const nameHTML = "<h2>" + user.name + "</h2>";
    const departmentHTML = "<h3>" + user.department + "</h3>";
    const bioHTML = "<p>" + user.bio + "</p>";
    document.getElementById("details").innerHTML = profilePictureHTML + nameHTML + departmentHTML + bioHTML;

    // Pre-fill input for editting profile with the current user details
    document.getElementById("name").setAttribute("value", user.name); 
    document.getElementById("department").setAttribute("value", user.department); 
    document.getElementById("bio").innerHTML = user.bio;
  });
}

/* Toggle the display of the popup box to change profile */
function togglePopup() {
  document.getElementById("popup-profile").classList.toggle("active");
}
