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
    latitude: 51.5252574,
    longitude: -0.1282885,
    description: "Counter-serve chain for ready-made sandwiches plus breakfast, coffee, soups & salads.",
    crowd: 3,
    price: 1
  },
  {
    name: "Wasabi",
    description: "Light-filled, modern chain branch serving up bento boxes, sushi and hot Japanese dishes.",
    latitude: 51.5172219,
    longitude: -0.1334257,
    crowd: 3,
    price: 2
  },
  {
    name: "Shake Shack",
    description: "Branch of an American chain known for its gourmet burgers & frozen custard shakes. Dog-friendly.",
    latitude: 51.5172383,
    longitude: -0.1399918,
    crowd: 2,
    price: 2
  },
  {
    name: "Taco Bell",
    latitude: 51.5203205,
    longitude: -0.125156,
    description: "Fast-food chain serving Mexican-inspired fare such as tacos, quesadillas & nachos.",
    crowd: 4,
    price: 1
  },
  {
    name: "KFC",
    latitude: 51.5204878,
    longitude: -0.1431986,
    description: "Fast-food chain known for its buckets of fried chicken, plus wings & sides.",
    crowd: 4,
    price: 2
  }
];

/* Load top 5 recommendations */
function loadTopRecommendations() {
  const topRecommendationsList = document.getElementById("top-recommendations-list");
  topRecommendationsList.innerHTML = "";

  const recommendationBox = document.createElement("div");
  recommendationBox.className = "recommendation-box";
  for (var i = 1; i <= topRecommendations.length; i++) {
    const topRecommendation = topRecommendations[i];
    const recommendationBox = document.createElement("div");
    recommendationBox.className = "recommendation-box";
    if(i == 1) {
      recommendationBox.innerHTML = "<p class=\"top-recommendation\">Most Recommended</p>";
    }
    const nameHTML = "<h3><b>#" + i + " " + topRecommendation.name + "</b></h3>";
    const locationHTML = "<p>latitiude: " + topRecommendation.latitude + ", longitude: " + topRecommendation.longitude + "</p>";
    const ratingHTML = "<p>crowd: " + topRecommendation.crowd + "/5, price: " + topRecommendation.price + "/5</p>";
    const descriptionHTML = "<p>" + topRecommendation.description + "</p>";
    recommendationBox.innerHTML += nameHTML + locationHTML + ratingHTML + descriptionHTML;
    topRecommendationsList.append(recommendationBox);
  }
}
