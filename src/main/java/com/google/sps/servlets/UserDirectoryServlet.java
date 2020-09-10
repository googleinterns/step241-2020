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
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that gets user directory data (summary) from the datastore */
@WebServlet("/user-directory")
public class UserDirectoryServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get list of entities of user details from data store
    Query query = new Query("User").addSort("name", SortDirection.ASCENDING);
    PreparedQuery prepared = DatastoreServiceFactory.getDatastoreService().prepare(query);

    // Convert entities to java class user entities
    List<User> users = new ArrayList<>();
    for (Entity entity : prepared.asIterable()) {
      String email = (String) entity.getProperty("email");
      String name = (String) entity.getProperty("name");
      String department = (String) entity.getProperty("department");
      int year = (int) (long) entity.getProperty("year");
      String profilePictureUrl = (String) entity.getProperty("profilePictureUrl");
      users.add(new User(email, name, department, year, profilePictureUrl));
    }

    // Return list of user details
    String json = new Gson().toJson(users);
    response.setContentType("application/json");
    response.getWriter().println(json);
  }
}
