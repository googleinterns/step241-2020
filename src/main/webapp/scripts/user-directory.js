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
  fetch("user-data").then(result => result.json()).then((users) => {
    const userDirectoryElement = document.getElementById("user-directory");
    users.forEach((user) => {
      const userCardElement = document.createElement("div");
      userCardElement.className = "user-card";
      const profilePictureHTML = "<img class=\"user-img\" src=\"" + user.profilePictureUrl + "\"></img>";
      const nameHTML = "<h3>" + user.name + "</h3>";
      const departmentHTML = "<h4>" + user.department + "Year" + user.year + "</h4>";
      const phoneHTML = "<h5>" + user.phone + "</h5>";
      const bioHTML = "<p>" + user.bio + "</p>";
      const emailTo = "<a href='mailto:" + user.email + "?subject=Enquiries for Student Recommendations'>message me</a>";
      const emailButtonHTML = "<div class='button'>" + emailTo + "</div>";
      userCardElement.innerHTML = profilePictureHTML + nameHTML + departmentHTML + phoneHTML + emailButtonHTML;
      userDirectoryElement.appendChild(userCardElement);
    });
  });
}
