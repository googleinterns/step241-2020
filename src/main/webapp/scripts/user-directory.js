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

function generateUserCards() {
  fetch("/user-directory").then(result => result.json()).then((users) => {
    const userDirectoryElement = document.getElementById("user-directory");
    users.forEach((user) => {
      const userCardElement = document.createElement("div");
      userCardElement.className = "user-card";
      if ( !user.hasOwnProperty("profilePictureUrl") ) {
        user.profilePictureUrl = "/images/avatar.jpg";
      }
      const profilePictureHTML = "<img class=\"profile-picture\" src=\"" + user.profilePictureUrl + "\"></img>";
      const nameHTML = "<h3>" + user.name + "</h3>";
      const departmentHTML = "<h4>" + user.department + " Year "+ user.year + "</h4>";
      const userPageLink = "<a href='/user-details.html?email=" + user.email + "'>more info</a>";
      const userPageHTML = "<div class='button'>" + userPageLink + "</div>";
      userCardElement.innerHTML = profilePictureHTML + nameHTML + departmentHTML + userPageHTML;
      userDirectoryElement.appendChild(userCardElement);
    });
  });
}
