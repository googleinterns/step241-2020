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

function loadTopRecommendations() {
  // Get category, costRating and crowdRating from user input radio buttons
  var chosenCategory = document.querySelector('input[name="recommendation-category"]:checked').value;
  var preferredCost = document.querySelector('input[name="price"]:checked').value;
  var preferredCrowd = document.querySelector('input[name="crowd"]:checked').value;

  fetch('/recommender?category=' + chosenCategory +"&cost-rating=" 
      + preferredCost +"&crowd-rating=" + preferredCrowd)
  .then(response => response.json())
  .then((recommendations) => {
    for (var j = 0; j < 5; j++) {
      displayRecommendation(recommendations[j], j+1);
    }
  });
}

/* Update HTML to display recommendation */
function displayRecommendation(recommendation, j) {
  const topRecommendationsList = document.getElementById("top-recommendations-list");
  const recommendationBox = document.createElement("div");
  recommendationBox.className = "recommendation-box";
  // if highest recommendation, label with 'Most Recommended' in the HTML
  if (j == 1) {
    recommendationBox.innerHTML = "<p class=\"top-recommendation\">Most Recommended</p>";
  }
  const nameHTML = "<h3><b>#" + j + " " + recommendation.name + "</b></h3>";
  const locationHTML = "<p>latitiude: " + recommendation.lat + ", longitude: " + recommendation.lng + "</p>";
  const ratingHTML = "<p>crowd: " + recommendation.crowdRating + "/5, price: " + recommendation.costRating + "/5</p>";
  const descriptionHTML = "<p>" + recommendation.description + "</p>";
  recommendationBox.innerHTML += nameHTML + locationHTML + ratingHTML + descriptionHTML;
  topRecommendationsList.append(recommendationBox);
}
