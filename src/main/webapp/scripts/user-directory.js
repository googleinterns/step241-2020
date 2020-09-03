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
  const users =
  [
    {
      name: "User name 1",
      department: "Department1",
      profilepic: "/images/avatar.jpg",
      email: "useremail1@google.com"
    },
    {
      name: "User name 2",
      department: "Department2",
      profilepic: "/images/avatar.jpg",
      email: "useremail2@google.com"
    },
    {
      name: "User name 3",
      department: "Department3",
      profilepic: "/images/avatar.jpg",
      email: "useremail3@google.com"
    },
    {
      name: "User name 4",
      department: "Department4",
      profilepic: "/images/avatar.jpg",
      email: "useremail4@google.com"
    },
    {
      name: "User name 5",
      department: "Department5",
      profilepic: "/images/avatar.jpg",
      email: "useremail5@google.com"
    },
  ];

  const userDirElement = document.getElementById("user-dir");
  users.forEach((user) => {
    const userCardElement = document.createElement("div");
    userCardElement.className = "user-card";
    const profilepicHTML = "<img class=\"user-img\" src=\"" + user.profilepic + "\"></img>";
    const nameHTML = "<h3>" + user.name + "</h3>";
    const departmentHTML = "<p>" + user.department + "</p>";
    const emailTo = "<a href='mailto:" + user.email + "?subject=Enquiries for Student Recommendations'>message me</a>";
    const button = "<div class='button' >" + emailTo + "</div>";
    userCardElement.innerHTML = profilepicHTML + nameHTML + departmentHTML + button;
    userDirElement.appendChild(userCardElement);
  });
}
