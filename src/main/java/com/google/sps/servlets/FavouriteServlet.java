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
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.Recommendation;
import java.io.IOException;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that sends and get user data to and from the datastore */
@WebServlet("/favourite")
public class FavouriteServlet extends HttpServlet {

  private DatastoreService datastore;
  private UserService userService;

  @Override
  public void init(){
    datastore = DatastoreServiceFactory.getDatastoreService();
    userService = UserServiceFactory.getUserService();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get list of user's favourite recommendation from datastore
    String email = userService.getCurrentUser().getEmail();
    Query query = new Query("Favourite").setFilter(new FilterPredicate("email", FilterOperator.EQUAL, email));
    PreparedQuery prepared = datastore.prepare(query);

    // Get recommendations based on recommendation ID as key
    List<Recommendation> favouriteRecommendations = new ArrayList<>();
    for (Entity favourite : prepared.asIterable()) {
      long recommendationId = (long) favourite.getProperty("recommendation-id");
      Key recommendationKey = KeyFactory.createKey("Recommendation", recommendationId);
      try {
        Entity recommendation = datastore.get(recommendationKey);
        favouriteRecommendations.add(convertEntityToRecommendation(recommendation));
      }
      catch (EntityNotFoundException e) {
        e.printStackTrace();
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
    }

    // Return list of favourite recommendations
    String json = new Gson().toJson(favouriteRecommendations);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String email = userService.getCurrentUser().getEmail();
    long recommendationId = Long.parseLong(request.getParameter("id"));

    // Send favourited recommendation ID to datastore, if it has not been stored
    if(!isFavourite(email, recommendationId)) {
      Entity favourite = new Entity("Favourite");
      favourite.setProperty("email", email);
      favourite.setProperty("recommendation-id", recommendationId);
      datastore.put(favourite);
    }
  }

  private boolean isFavourite(String email, long recommendationId) {
    Query query = new Query("Favourite")
      .setFilter(new FilterPredicate("email", FilterOperator.EQUAL, email))
      .setFilter(new FilterPredicate("recommendation-id", FilterOperator.EQUAL, recommendationId));
    List<Entity> favourite = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());
    return favourite != null && !favourite.isEmpty();
  }

  // Convert datastore entity into recommendation object
  private Recommendation convertEntityToRecommendation(Entity recommendation) {
    long id = recommendation.getKey().getId();
    String name = (String) recommendation.getProperty("name");
    double lat = (double) recommendation.getProperty("latitude");
    double lng = (double) recommendation.getProperty("longitude");
    String category = (String) recommendation.getProperty("category");
    String description = (String) recommendation.getProperty("description");
    int costRating = (int) (long) recommendation.getProperty("cost-rating");
    int crowdRating = (int) (long) recommendation.getProperty("crowd-rating");
    return new Recommendation(id, name, category, lat, lng, description, costRating, crowdRating);
  }
}