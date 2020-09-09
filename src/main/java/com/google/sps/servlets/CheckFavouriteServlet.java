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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/check-favourite")
public class CheckFavouriteServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String email = UserServiceFactory.getUserService().getCurrentUser().getEmail();
    long recommendationId = Long.parseLong(request.getParameter("id"));

    Query query = new Query("Favourite")
      .setFilter(new FilterPredicate("email", FilterOperator.EQUAL, email))
      .setFilter(new FilterPredicate("recommendationId", FilterOperator.EQUAL, recommendationId));
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    List<Entity> favourite = datastore.prepare(query).asList(FetchOptions.Builder.withDefaults());

    response.setContentType("text/html");
    if(favourite != null && !favourite.isEmpty()){
      long favouriteId = favourite.get(0).getKey().getId();
      response.getWriter().println(favouriteId);
    }
    else {
      response.getWriter().println("null");
    }
  }
}
