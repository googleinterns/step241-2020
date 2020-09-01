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

import com.google.sps.data.Matrix;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/recommender")
public class RecommenderServlet extends HttpServlet {

  private final int FACTORS_N = 3;
  private final String[] factors = {"price", "crowd", "distance"};

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Matrix resultMatrix = recommendationMatix().multiply(preferenceMatrix(request));
    // TODO: return recommendation based on the final points for each recommendation
  }

  private Matrix recommendationMatix(){
    // TODO: construct matrix of which represents the recommendations' points for every factor
    return new Matrix(0,0);
  }

  // construct a matrix of size FACTORS_N x 1 which represents user's points
  // every coloumn represents factor considered
  private Matrix preferenceMatrix(HttpServletRequest request){
    Matrix preference = new Matrix(FACTORS_N, 1);
    for(int i = 0;  i < FACTORS_N; i++) {
      int point = Integer.parseInt(request.getParameter(factors[i]));
      preference.setValue(point, i, 1);
    }
    return preference;
  }
}
