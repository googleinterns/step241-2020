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

window.onscroll = function() {
  const scrollTopButton = document.getElementById("to-top-button");
};

window.onload = function() {
  fetch("/login").then(result => result.json()).then((details) => {
    if (!details.isUserLoggedIn) {
        
      // If user is not logged in then redirect to the login page, if not already there
      const loginPageURL = window.location.protocol + "//" + window.location.host;
      if(window.location.href !== loginPageURL) {
        window.location.href = loginPageURL;
      }

      // On the login page put the link to login with email on the login button 
      document.getElementById("login-link").href = details.loginURL;
    }
  });
}

// When the user clicks on the button, scroll to the top of the document
function toTopFunction() {
  document.body.scrollTop = 0;
  document.documentElement.scrollTop = 0;
}
