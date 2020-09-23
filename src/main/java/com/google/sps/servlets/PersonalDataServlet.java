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

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
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
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
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

/** Servlet that gets the current user's data */
@WebServlet("/personal-data")
public class PersonalDataServlet extends HttpServlet {
  
  private DatastoreService datastore;
  private BlobstoreService blobstoreService;
  private UserService userService;

  @Override
  public void init(){
    datastore = DatastoreServiceFactory.getDatastoreService();
    blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    userService = UserServiceFactory.getUserService();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get current user email to generate entity key
    String email = userService.getCurrentUser().getEmail();
    Key userKey = KeyFactory.createKey("User", email);

    try {
      // Get current user details from datastore
      Entity entity = datastore.get(userKey);
      String name = (String) entity.getProperty("name");
      String department = (String) entity.getProperty("department");
      // double casting is needed since integer is stored as long integer in datastore.
      // Reference: https://cloud.google.com/appengine/docs/standard/java/datastore/entities#Properties_and_value_types
      int year = (int) (long) entity.getProperty("year");
      String phone = (String) entity.getProperty("phone");
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

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String email = userService.getCurrentUser().getEmail();
    long updatedTime = System.currentTimeMillis();

    // Fetch user data from filled form on the client side
    String name = request.getParameter("name");
    String department = request.getParameter("department");
    int year = Integer.parseInt(request.getParameter("year"));
    String phone = request.getParameter("phone");
    String bio = request.getParameter("bio");
    String profilePictureUrl = getUploadedFileUrl(request);

    // Build user entity and upsert to datastore
    Entity user = new Entity("User", email);
    user.setProperty("email", email);
    user.setProperty("name", name);
    user.setProperty("department", department);
    user.setProperty("year", year);
    user.setProperty("phone", phone);
    user.setProperty("bio", bio);
    user.setProperty("updatedTime", updatedTime);

    // TODO: fix check when no file is uploaded, current check does not work
    if(profilePictureUrl != null){
      user.setProperty("profilePictureUrl", profilePictureUrl);
    }
    datastore.put(user);
    
    response.sendRedirect(request.getParameter("redirectUrl"));
  }

  // Get URL to the uploaded file
  private String getUploadedFileUrl(HttpServletRequest request) {
    List<BlobKey> blobKeys = blobstoreService.getUploads(request).get("user-img");

    // User submitted form without selecting a file, so we can't get a URL. (devserver)
    if(blobKeys == null || blobKeys.isEmpty()) {
      return null;
    }

    BlobKey blobKey = blobKeys.get(0);
    BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
    // User submitted form without selecting a file, so we can't get a URL. (live server)
    if (blobInfo.getSize() == 0) {
      blobstoreService.delete(blobKey);
      return null;
    }

    // Use ImagesService to get a URL that points to the uploaded file.
    // ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
    // return ImagesServiceFactory.getImagesService().getServingUrl(options);
    return "https://storage.cloud.google.com" + blobInfo.getGsObjectName();
  }
}
