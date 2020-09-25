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

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.sps.data.Recommendation;
import com.google.gson.Gson;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public final class RecommenderServletTest {
    
  private final LocalServiceTestHelper helper =
      new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
  private final DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
  private final RecommenderServlet myServlet = new RecommenderServlet();
  
  @Before
  public void setUp() {
    helper.setUp();
    myServlet.init();
    // Restaurant Recommendations
    Entity restaurantA = new Entity("Recommendation");
    restaurantA.setProperty("place-name", "Restaurant A");
    restaurantA.setProperty("latitude", 51.0000000000000);
    restaurantA.setProperty("longitude", -0.1000000000000);
    restaurantA.setProperty("category", "restaurants");
    restaurantA.setProperty("description", "A great place to eat");
    restaurantA.setProperty("cost-rating", 1);
    restaurantA.setProperty("crowd-rating", 2);
    Entity restaurantB = new Entity("Recommendation");
    restaurantB.setProperty("place-name", "Restaurant B");
    restaurantB.setProperty("latitude", 52.0);
    restaurantB.setProperty("longitude", -0.2);
    restaurantB.setProperty("category", "restaurants");
    restaurantB.setProperty("description", "A lovely place to eat");
    restaurantB.setProperty("cost-rating", 2);
    restaurantB.setProperty("crowd-rating", 2);
    Entity restaurantC = new Entity("Recommendation");
    restaurantC.setProperty("place-name", "Restaurant C");
    restaurantC.setProperty("latitude", 53.0);
    restaurantC.setProperty("longitude", -0.3);
    restaurantC.setProperty("category", "restaurants");
    restaurantC.setProperty("description", "A wonderful place to eat");
    restaurantC.setProperty("cost-rating", 3);
    restaurantC.setProperty("crowd-rating", 3);
    Entity restaurantD = new Entity("Recommendation");
    restaurantD.setProperty("place-name", "Restaurant D");
    restaurantD.setProperty("latitude", 54.0);
    restaurantD.setProperty("longitude", -0.4);
    restaurantD.setProperty("category", "restaurants");
    restaurantD.setProperty("description", "A charming place to eat");
    restaurantD.setProperty("cost-rating", 4);
    restaurantD.setProperty("crowd-rating", 3);
    Entity restaurantE = new Entity("Recommendation");
    restaurantE.setProperty("place-name", "Restaurant E");
    restaurantE.setProperty("latitude", 55.0);
    restaurantE.setProperty("longitude", -0.5);
    restaurantE.setProperty("category", "restaurants");
    restaurantE.setProperty("description", "A popular place to eat");
    restaurantE.setProperty("cost-rating", 5);
    restaurantE.setProperty("crowd-rating", 5);
    
    // Places to Study
    Entity studyPlaceA = new Entity("Recommendation");
    studyPlaceA.setProperty("place-name", "Study Place A");
    studyPlaceA.setProperty("latitude", 60.0);
    studyPlaceA.setProperty("longitude", -0.6);
    studyPlaceA.setProperty("category", "study-places");
    studyPlaceA.setProperty("description", "Really great place to study.");
    studyPlaceA.setProperty("cost-rating", 1);
    studyPlaceA.setProperty("crowd-rating", 1);
    Entity studyPlaceB = new Entity("Recommendation");
    studyPlaceB.setProperty("place-name", "Study Place B");
    studyPlaceB.setProperty("latitude", 70.0);
    studyPlaceB.setProperty("longitude", -0.7);
    studyPlaceB.setProperty("category", "study-places");
    studyPlaceB.setProperty("description", "A very popular place to study.");
    studyPlaceB.setProperty("cost-rating", 5);
    studyPlaceB.setProperty("crowd-rating", 5);
    
    // Places to Visit
    Entity placeToVisitA = new Entity("Recommendation");
    placeToVisitA.setProperty("place-name", "Place To Visit A");
    placeToVisitA.setProperty("latitude", 40.2);
    placeToVisitA.setProperty("longitude", -0.2);
    placeToVisitA.setProperty("category", "places-to-visit");
    placeToVisitA.setProperty("description", "A great place to visit.");
    placeToVisitA.setProperty("cost-rating", 3);
    placeToVisitA.setProperty("crowd-rating", 3);
    Entity placeToVisitB = new Entity("Recommendation");
    placeToVisitB.setProperty("place-name", "Place To Visit B");
    placeToVisitB.setProperty("latitude", 40.3);
    placeToVisitB.setProperty("longitude", -0.3);
    placeToVisitB.setProperty("category", "places-to-visit");
    placeToVisitB.setProperty("description", "A lovely place to visit.");
    placeToVisitB.setProperty("cost-rating", 5);
    placeToVisitB.setProperty("crowd-rating", 4);
    Entity placeToVisitC = new Entity("Recommendation");
    placeToVisitC.setProperty("place-name", "Place To Visit C");
    placeToVisitC.setProperty("latitude", 40.4);
    placeToVisitC.setProperty("longitude", -0.4);
    placeToVisitC.setProperty("category", "places-to-visit");
    placeToVisitC.setProperty("description", "A nice place to visit.");
    placeToVisitC.setProperty("cost-rating", 4);
    placeToVisitC.setProperty("crowd-rating", 5);
    Entity placeToVisitD = new Entity("Recommendation");
    placeToVisitD.setProperty("place-name", "Place To Visit D");
    placeToVisitD.setProperty("latitude", 40.5);
    placeToVisitD.setProperty("longitude", -0.5);
    placeToVisitD.setProperty("category", "places-to-visit");
    placeToVisitD.setProperty("description", "An interesting place to visit.");
    placeToVisitD.setProperty("cost-rating", 2);
    placeToVisitD.setProperty("crowd-rating", 3);
    Entity placeToVisitE = new Entity("Recommendation");
    placeToVisitE.setProperty("place-name", "Place To Visit E");
    placeToVisitE.setProperty("latitude", 40.6);
    placeToVisitE.setProperty("longitude", -0.6);
    placeToVisitE.setProperty("category", "places-to-visit");
    placeToVisitE.setProperty("description", "A charming place to visit.");
    placeToVisitE.setProperty("cost-rating", 1);
    placeToVisitE.setProperty("crowd-rating", 2);
    Entity placeToVisitF = new Entity("Recommendation");
    placeToVisitF.setProperty("place-name", "Place To Visit F");
    placeToVisitF.setProperty("latitude", 40.7);
    placeToVisitF.setProperty("longitude", -0.7);
    placeToVisitF.setProperty("category", "places-to-visit");
    placeToVisitF.setProperty("description", "A wonderful place to visit.");
    placeToVisitF.setProperty("cost-rating", 2);
    placeToVisitF.setProperty("crowd-rating", 2);
    
    // Store recommendations in datastore
    ds.put(restaurantA);
    ds.put(restaurantB);
    ds.put(restaurantC);
    ds.put(restaurantD);
    ds.put(restaurantE);
    ds.put(studyPlaceA);
    ds.put(studyPlaceB);
    ds.put(placeToVisitA);
    ds.put(placeToVisitB);
    ds.put(placeToVisitC);
    ds.put(placeToVisitD);
    ds.put(placeToVisitE);
    ds.put(placeToVisitF);
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }
  
  // Function to mock the request and response
  public String getPersonalisedRecommendations (String category, String costRating, String crowdRating) throws Exception {
    // Mock request and mock response
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    when(request.getParameter("category")).thenReturn(category);
    when(request.getParameter("cost-rating")).thenReturn(costRating);
    when(request.getParameter("crowd-rating")).thenReturn(crowdRating);
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    when(response.getWriter()).thenReturn(writer);
    myServlet.doGet(request, response);
    writer.flush();
    return stringWriter.toString();
  }

  @Test
  public void allFiveReturned() throws Exception {
    // When there are exactly five recommendations of that category, the algorithm should return  
    // all 5 recommendations. The coordinate formed by (cost-rating, crowd-rating) should be closest
    // to the coordinate of user preference for the first recommendation, then respectively for 
    // the remaining 4 (lowest Euclidean distance)
    // 'Top 5' recommendations, ordered using the Euclidean distance (ED) algorithm
    Collection<Recommendation> expected = Arrays.asList(
      new Recommendation("Restaurant C", "restaurants", 53.0, -0.3, "A wonderful place to eat", 3, 3), // ED = 0
      new Recommendation("Restaurant D", "restaurants", 54.0, -0.4, "A charming place to eat", 4, 3), // ED = 1
      new Recommendation("Restaurant B", "restaurants", 52.0, -0.2, "A lovely place to eat", 2, 2), // ED = 2
      new Recommendation("Restaurant A", "restaurants", 51.0, -0.1, "A great place to eat", 1, 2), // ED = 5
      new Recommendation("Restaurant E", "restaurants", 55.0, -0.5, "A popular place to eat", 5, 5) // ED = 8
    );
    String personalisedRecommendation = getPersonalisedRecommendations("restaurants", "3", "3");
    // Convert expected to json, to compare
    String json = new Gson().toJson(expected);
    // Check if json returned from recommender servlet is as expected
    assertEquals(json + "\n", personalisedRecommendation);
  }

  @Test
  public void lessThanFiveReturned() throws Exception{
    // When there are less than five recommendations of that category, the algorithm should return the number that is 
    // there, in the most 'relevant' order based on Euclidean distance from user preference
    // For 'study-places' category, there are only 2 recommendations

    // Only 2 recommendations answers as there are only two for this category, ordered by 'relevance' using the 
    // Euclidean distance (ED) algorithm
    Collection<Recommendation> expected = Arrays.asList(
      new Recommendation("Study Place A", "study-places", 60.0, -0.6, "Really great place to study.", 1, 1), // ED = 5
      new Recommendation("Study Place B", "study-places", 70.0, -0.7, "A very popular place to study.", 5, 5) // ED = 13
    );
    String personalisedRecommendation = getPersonalisedRecommendations("study-places", "2", "3");
    // Convert expected to json, to compare
    String json = new Gson().toJson(expected);
    // Check if json returned from recommender servlet is as expected
    assertEquals(json + "\n", personalisedRecommendation);
  }  

  @Test
  public void noRecommendationsReturned() throws Exception{
    // When there are no recommendations of that category, the algorithm should return no recommendations.

    // Empty collection of recommendations
    Collection<Recommendation> expected = Arrays.asList();
    String personalisedRecommendation = getPersonalisedRecommendations("bars-and-clubs", "5", "3");
    // Convert expected to json, to compare
    String json = new Gson().toJson(expected);
    // Check if json returned from recommender servlet is as expected
    assertEquals(json + "\n", personalisedRecommendation);
  }
  
  @Test
  public void onlyFiveReturned() throws Exception{
    // When there are more than five recommendations of that category, the algorithm should return  
    // only the 5 recommendations that are most relevant (have the smallest Euclidean distance from user preference).

    // Expected top 5 recommendations
    Collection<Recommendation> expected = Arrays.asList(
      new Recommendation("Place To Visit E", "places-to-visit", 40.6, -0.6, "A charming place to visit.", 1, 2), // ED = 1
      new Recommendation("Place To Visit F", "places-to-visit", 40.7, -0.7, "A wonderful place to visit.", 2, 2), // ED = 2
      new Recommendation("Place To Visit D", "places-to-visit", 40.5, -0.5, "An interesting place to visit.", 2, 3), // ED = 5
      new Recommendation("Place To Visit A", "places-to-visit", 40.2, -0.2, "A great place to visit.", 3, 3), // ED = 8
      new Recommendation("Place To Visit C", "places-to-visit", 40.4, -0.4, "A nice place to visit.", 4, 5) // ED = 25
    );
    String personalisedRecommendation = getPersonalisedRecommendations("places-to-visit", "1", "1");
    // Convert expected to json, to compare
    String json = new Gson().toJson(expected);
    // Check if json returned from recommender servlet is as expected
    assertEquals(json + "\n", personalisedRecommendation);
  }  
}
