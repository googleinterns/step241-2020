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
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.gson.Gson;
import com.google.sps.data.Recommendation;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that stores user recommendation data in datastore */
@WebServlet("/recommendation")
public class RecommendationServlet extends HttpServlet {
  
  private DatastoreService datastore;

  public void init() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Long id = Long.parseLong(request.getParameter("id"));
    
    try {
      Key key = KeyFactory.createKey("Recommendation", id);
      Entity recommendation = datastore.get(key);
      // Get details of recommendation
      String name = (String) recommendation.getProperty("place-name");
      String category = (String) recommendation.getProperty("category");
      double lat = (double) recommendation.getProperty("latitude");
      double lng = (double) recommendation.getProperty("longitude");
      String description = (String) recommendation.getProperty("description");
      int costRating = (int) (long) recommendation.getProperty("cost-rating");
      int crowdRating = (int) (long) recommendation.getProperty("crowd-rating");

      response.setContentType("application/json");
      String json = new Gson().toJson(new Recommendation(name, category, lat, lng, description, costRating, crowdRating));
      response.getWriter().println(json);
    }
    catch (EntityNotFoundException e) {
      e.printStackTrace();
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    String name = request.getParameter("place-name");
    String formattedLatLng = request.getParameter("location");
    // Get latitude and longitude from formattedLatLng

    /* Expected format for formattedLatLng:
            lat, lng
       e.g. 51.346833241, -0.0234157345
    */
    String [] latLng = formattedLatLng.split(", ");
    double lat = Double.parseDouble(latLng[0]);
    double lng = Double.parseDouble(latLng[1]);
    String category = request.getParameter("category-list");
    String description = request.getParameter("description");
    int costRating = Integer.parseInt(request.getParameter("price"));
    int crowdRating = Integer.parseInt(request.getParameter("crowd"));

    // Create entity from recommendation data
    Entity recommendationEntity = new Entity("Recommendation");
    recommendationEntity.setProperty("place-name", name);
    recommendationEntity.setProperty("latitude", lat);
    recommendationEntity.setProperty("longitude", lng); 
    recommendationEntity.setProperty("category", category);
    recommendationEntity.setProperty("description", description);
    recommendationEntity.setProperty("cost-rating", costRating);
    recommendationEntity.setProperty("crowd-rating", crowdRating);

    // Send the recommendation to the datastore
    DatastoreServiceFactory.getDatastoreService().put(recommendationEntity);

    response.sendRedirect("/recommendation-map.html");
  }
}
