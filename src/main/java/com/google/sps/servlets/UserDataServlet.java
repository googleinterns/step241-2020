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
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.User;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that gets a user data with a specific email from the datastore */
@WebServlet("/user-data")
public class UserDataServlet extends HttpServlet {
  
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get current user email to generate entity key
    String email = request.getParameter("email");
    Key userKey = KeyFactory.createKey("User", email);

    try {
      // Get current user details from datastore
      Entity entity = DatastoreServiceFactory.getDatastoreService().get(userKey);
      String name = (String) entity.getProperty("name");
      String department = (String) entity.getProperty("department");   
      int year = (int) (long) entity.getProperty("year");
      long phone = (long) entity.getProperty("phone");
      String bio = (String) entity.getProperty("bio");
      String profilePictureUrl = (String) entity.getProperty("profilePictureUrl");
      long updatedTime = (long) entity.getProperty("updatedTime");

      // Return current user's details
      String json = new Gson().toJson(new User(email, name, department, year, phone, bio, profilePictureUrl, updatedTime));
      response.setContentType("application/json");
      response.getWriter().println(json);
    } catch (EntityNotFoundException e) {
      e.printStackTrace();
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }
}
