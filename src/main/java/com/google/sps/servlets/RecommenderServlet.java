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
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.gson.Gson;
import com.google.sps.data.DistancePair;
import com.google.sps.data.Recommendation;
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

  private final String[] FACTORS = {"cost-rating", "crowd-rating"};
  private DatastoreService datastore;

  @Override
  public void init(){
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    List<Recommendation> recommendations = new ArrayList<>();
    
    // Get all pairs of recommendation IDs and corresponding distances from user's preference
    List<DistancePair> distances = getRecommendationsDistances(request);
    // Sort by distance
    Collections.sort(distances);

    for (int i = 0; i < 5; i++) {
      // Get recommendation ID
      DistancePair recommendationDistance = distances.get(i);
      long id = recommendationDistance.getId();
      try {
        Key key = KeyFactory.createKey("Recommendation", id);
        Entity recommendation = datastore.get(key);
        // Get details of recommendation
        String name = (String) recommendation.getProperty("place-name");
        String category = (String) recommendation.getProperty("category");
        double lat = (double) recommendation.getProperty("latitude");
        double lng = (double) recommendation.getProperty("longitude");
        String description = (String) recommendation.getProperty("description");
        // Double casting is needed here because the integer is stored as a long integer in datastore.
        // for reference: https://cloud.google.com/appengine/docs/standard/java/datastore/entities#Properties_and_value_types
        int costRating = (int) (long) recommendation.getProperty("cost-rating");
        int crowdRating = (int) (long) recommendation.getProperty("crowd-rating");
        Recommendation featuredRecommendation = new Recommendation(name, category, lat, lng, description, costRating, crowdRating);
        // Add the recommendation to recommendations list to be returned
        recommendations.add(featuredRecommendation);
      }
      catch (EntityNotFoundException e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
    }
    response.setContentType("application/json");
    String json = new Gson().toJson(recommendations);
    response.getWriter().println(json);
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

  private List<Entity> getRecommendationsByCategory(String category) {
    Query query = new Query("Recommendation").setFilter(new FilterPredicate("category", FilterOperator.EQUAL, category));
    // Get the rating for each factor
    for (String factor : FACTORS) {
      query.addProjection(new PropertyProjection(factor, Integer.class));
    }
    return datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
  }

  private int calculateDistance(HttpServletRequest request, Entity recommendation) {
    int distance = 0;
    // Get the rating of the recommendation and the rating from the user preference for each factor
    for (String factor : FACTORS) {
      // Double casting is needed here because in datastore, the int is stored as a long.
      // for reference: https://cloud.google.com/appengine/docs/standard/java/datastore/entities#Properties_and_value_types
      int recommendationRating = (int) (long) recommendation.getProperty(factor);
      int preferenceRating = Integer.parseInt(request.getParameter(factor));
      // Square the absolute difference to calculate the distance
      distance += Math.pow((recommendationRating - preferenceRating), 2);
    }
    return distance;
  }
}
