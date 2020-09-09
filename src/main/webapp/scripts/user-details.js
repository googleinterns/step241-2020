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

function generateUserDetails() {
  const urlParams = new URLSearchParams(window.location.search);
  const email = urlParams.get("email");
  const params = new URLSearchParams();
  params.append("email", email);
  fetch("/user-data?email=" + email).then(result => result.json()).then((user) => {
    document.getElementById("title").innerHTML = user.name;
    const userDetailsElement = document.getElementById("user-details");
    const profilePictureHTML = "<img class=\"profile-pic\" src=\"" + user.profilePictureUrl + "\"></img>";
    const departmentHTML = "<h2>" + user.department + " year "+ user.year + "</h2>";
    const phoneHTML = "<h3>" + user.phone + "</h3>";
    const bioHTML = "<p>" + user.bio + "</p>";
    const emailTo = "<a href='mailto:" + user.email + "?subject=Enquiries for Student Recommendations'>message me</a>";
    const emailButtonHTML = "<div class='button' >" + emailTo + "</div>";
    userDetailsElement.innerHTML = profilePictureHTML + departmentHTML + phoneHTML + bioHTML + emailButtonHTML;
  });
}
