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
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that stores user recommendation data in datastore */
@WebServlet("/recommendation")
public class RecommendationServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    String name = request.getParameter("place-name");
    String formattedLatLng = request.getParameter("location");

    // Get latitude and longitude from formattedLatLng
    String [] latLng = formattedLatLng.split(", ");
    String lat = latLng[0];
    String lng = latLng[1];
    String category = request.getParameter("category-list");
    String description = request.getParameter("description");
    int costRating = Integer.parseInt(request.getParameter("price"));
    int crowdRating = Integer.parseInt(request.getParameter("crowd"));

    // Create entity on recommendation data
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
