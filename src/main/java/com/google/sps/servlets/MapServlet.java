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

@WebServlet("/added-markers")
public class MapServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Collection<Marker> allMarkers = getMarkers();
    Gson gson = new Gson();
    String json = gson.toJson(allMarkers);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    double lat = Double.parseDouble(request.getParameter("lat"));
    double lng = Double.parseDouble(request.getParameter("lng"));
    Marker marker = new Marker(lat, lng);
    storeMarker(marker);
  }

  private Collection<Marker> getMarkers() {
    /* Collection to hold all markers from datastore */
    Collection <Marker> allMarkers = new ArrayList<>();

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    Query query = new Query("Marker");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      double lat = (double) entity.getProperty("lat");
      double lng = (double) entity.getProperty("lng");
      Marker marker = new Marker(lat, lng);
      allMarkers.add(marker);
    }
    return allMarkers;
  }

  private void storeMarker(Marker marker) {
    /* Add the marker to the datastore */
    Entity markerEntity = new Entity("Marker");
    markerEntity.setProperty("lat", marker.getLat());
    markerEntity.setProperty("lng", marker.getLng());
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(markerEntity);
  }
} 
