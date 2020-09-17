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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.PropertyProjection;
import com.google.appengine.api.datastore.Query;
import com.google.gson.Gson;
import com.google.sps.data.Marker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* Servlet that fetches all Markers from datastore */
@WebServlet("/all-markers")
public class AllMarkersServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Collection<Marker> allMarkers = getMarkers();
    Gson gson = new Gson();
    String json = gson.toJson(allMarkers);
    response.getWriter().println(json);
  }
  
  private Collection<Marker> getMarkers() {
    /* Collection to hold all markers from datastore */
    Collection<Marker> allMarkers = new ArrayList<>();
    Query query = new Query("Recommendation");
    query.addProjection(new PropertyProjection("latitude", Double.class));
    query.addProjection(new PropertyProjection("longitude", Double.class));
    query.addProjection(new PropertyProjection("category", String.class));
    PreparedQuery results = DatastoreServiceFactory.getDatastoreService().prepare(query);
    for (Entity entity : results.asIterable()) {
      double lat = (double) entity.getProperty("latitude");
      double lng = (double) entity.getProperty("longitude");
      String category = (String) entity.getProperty("category");
      long id = entity.getKey().getId();
      Marker marker = new Marker(lat, lng, id, category);
      allMarkers.add(marker);
    }
    return allMarkers;
  }
} 
