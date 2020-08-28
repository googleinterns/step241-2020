// Copyright 2019 Google LLC
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

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.User;
import java.io.IOException;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that sends and get user data to and from the datastore */
@WebServlet("/user-data")
public class UserDataServlet extends HttpServlet {
  
  private DatastoreService datastore;
  private BlobstoreService blobstoreService;

  public void init(){
    datastore = DatastoreServiceFactory.getDatastoreService();
    blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get list of entities of user details from data store
    Query query = new Query("User").addSort("name", SortDirection.ASCENDING);
    PreparedQuery prepared = datastore.prepare(query);

    // Convert entities to java class user entities
    List<User> users = new ArrayList<>();
    for (Entity entity : prepared.asIterable()) {
      String userEmail = (String) entity.getProperty("userEmail");
      String name = (String) entity.getProperty("name");
      String department = (String) entity.getProperty("department");
      String bio = (String) entity.getProperty("bio");
      String profilePictureUrl = (String) entity.getProperty("profilePictureUrl");
      long timestamp = (long) entity.getProperty("timestamp");
      users.add(new User(userEmail, name, department, bio, profilePictureUrl, timestamp));
    }

    // Return list of user details
    String json = new Gson().toJson(users);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    String userEmail = userService.getCurrentUser().getEmail();
    long timestamp = System.currentTimeMillis();

    // Fetch user data from filled form on the client side
    String name = request.getParameter("name");
    String department = request.getParameter("department");
    String bio = request.getParameter("bio");
    String profilePictureUrl = getUploadedFileUrl(request, "user-img");

    // Build user entity and upsert to datastore
    Entity user = new Entity("User", userEmail);
    user.setProperty("userEmail", userEmail);
    user.setProperty("name", name);
    user.setProperty("department", department);
    user.setProperty("bio", bio);
    user.setProperty("timestamp", timestamp);
    if(profilePictureUrl != null){
      user.setProperty("profilePictureUrl", profilePictureUrl);
    }
    datastore.put(user);
    
    response.sendRedirect(request.getParameter("redirectUrl"));
  }

  // Get URL to the uploaded file
  private String getUploadedFileUrl(HttpServletRequest request, String inputElementName) {
    // User does not submit any file
    try {
      if(request.getPart(inputElementName).getSize() == 0) {
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    List<BlobKey> blobKeys = blobstoreService.getUploads(request).get(inputElementName);

    // User submitted form without selecting a file, so we can't get a URL. (devserver)
    if(blobKeys == null || blobKeys.isEmpty()) {
      return null;
    }

    // User submitted form without selecting a file, so we can't get a URL. (live server)
    BlobKey blobKey = blobKeys.get(0);
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
    if (blobInfo.getSize() == 0) {
      blobstoreService.delete(blobKey);
      return null;
    }

    // Use ImagesService to get a URL that points to the uploaded file.
    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
    return ImagesServiceFactory.getImagesService().getServingUrl(options);
  }
}
