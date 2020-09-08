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

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/recommender")
public class RecommenderServlet extends HttpServlet {

  private final int RECOMMENDATIONS = 3;
  private final int FACTORS_N = 3;
  private final String[] factors = {"price", "crowd", "distance"};

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<Pair<Integer, Integer>> distances = getRecommendationsDistances(request);
    distances.sort(Comparator.<Pair<Integer, Integer>>comparingInt(Pair::getKey).thenComparingInt(Pair::getValue));
    // TODO: retrieve and return top 5 recommendations based on the value
  }

  private List<Pair<Integer, Integer>> getRecommendationsDistances(HttpServletRequest request){
    String category = request.getParameter("category");
    List<Entity> recommendations = getRecommendationsByCategory(category);

    List<Pair<Integer, Integer>> distances = new ArrayList<>();
    for(Entity recommendation : recommendations) {
      int id= entity.getKey().getId();
      int distance = calculateDistance(request, entity),
      distances.add(new Pair<>(distance, id));
    }
    return distances;
  }

  private List<Entity> getRecommendationsByCategory(String category){
    Query query = new Query("recommendation").setFilter(new FilterPredicate("category", FilterOperator.EQUAL, category));
    for(String factor : factors) {
      query.addProjection(new PropertyProjection(factor, Integer.class));
    }
    return datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
  }

  private Integer calculateDistance(HttpServletRequest request, Entity recommendation) {
    int distance = 0;
    for(String factor : factors) {
      int recommendationValue = (int) recommendation.getProperty(factor);
      int preferenceValue = Integer.parseInt(request.getParameter(factor));
      distance += Math.pow((recommendationValue - preferenceValue), 2);
    }
    return distance;
  }

  private List<Entity> getRecommendationsByCategory(String category){
    Query query = new Query("recommendation").setFilter(new FilterPredicate("category", FilterOperator.EQUAL, category));
    for(String factor : factors) {
      query.addProjection(new PropertyProjection(factor, Integer.class));
    }
    return datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
  }
}
