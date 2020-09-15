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

const topRecommendations = [
  {
    name: "Pret A Manger", 
    crowd: 3,
    price: 1
  },
  {
    name: "Wasabi", 
    crowd: 3,
    price: 2
  },
  {
    name: "Franco Manca", 
    crowd: 2,
    price: 2
  },
  {
    name: "Taco Bell", 
    crowd: 4,
    price: 1
  },
  {
    name: "KFC", 
    crowd: 4,
    price: 2
  }
];

/* Load top 5 recommendations */
function loadTopRecommendations() {
  var i;
  for (i = 0; i < topRecommendations.length; i++) {
    const topRecommendation = topRecommendations[i];
    const recommendationBox = document.createElement("div");
    recommendationBox.className = "recommendation-box";
    const rankHTML = "<h4>Rank " + i + "</h4>";
    const nameHTML = "<h3><b>" + topRecommendation.name + "</b></h3>";
    const ratingHTML = "<p>crowd: " + topRecommendation.crowd + "   price:" + topRecommendation.price + "</p>";
    recommendationBox.innerHTML = rankHTML + nameHTML + ratingHTML;
    document.getElementById("top-recommendations-list").append(recommendationBox);
  }
}
