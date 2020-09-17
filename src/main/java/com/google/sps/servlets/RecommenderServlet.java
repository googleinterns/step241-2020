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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.sps.data.DistancePair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/recommender")
public class RecommenderServlet extends HttpServlet {

  private final int RECOMMENDATIONS_N = 3;
  private final String[] FACTORS = {"price", "crowd", "distance"};

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<DistancePair> distances = getRecommendationsDistances(request);
    Collections.sort(distances);
    // TODO: retrieve and return top 5 recommendations based on the value
  }

  private List<DistancePair> getRecommendationsDistances(HttpServletRequest request) {
    String category = request.getParameter("category");
    List<Entity> recommendations = getRecommendationsByCategory(category);

    List<DistancePair> distances = new ArrayList<>();
    for (Entity recommendation : recommendations) {
      long id = recommendation.getKey().getId();
      int distance = calculateDistance(request, recommendation);
      distances.add(new DistancePair(id, distance));
    }
    return distances;
  }

  private List<Entity> getRecommendationsByCategory(String category){
    Query query = new Query("recommendation").setFilter(new FilterPredicate("category", FilterOperator.EQUAL, category));
    for(String factor : FACTORS) {
      query.addProjection(new PropertyProjection(factor, Integer.class));
    }
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    return datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
  }

  private int calculateDistance(HttpServletRequest request, Entity recommendation) {
    int distance = 0;
    // for every factor considered
    for(String factor : FACTORS) {
      // get the rating of the recommendation and the rating from the user preference
      int recommendationRating = (int) recommendation.getProperty(factor);
      int preferenceRating = Integer.parseInt(request.getParameter(factor));
      // then square the absolute difference to calculate the distance
      distance += Math.pow((recommendationRating - preferenceRating), 2);
    }
    return distance;
  }
}
