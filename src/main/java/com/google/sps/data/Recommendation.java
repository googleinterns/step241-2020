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

package com.google.sps.data;

/** The class represents a recommendation */
public final class Recommendation {

  private final long id;
  private final String name;
  private final String category;
  private final double lat;
  private final double lng;
  private final String description;
  private final int costRating;
  private final int crowdRating;

  public Recommendation(long id, String name, String category, double lat, double lng, 
      String description, int costRating, int crowdRating)
  {
    this.id = id;
    this.name = name;
    this.category = category;
    this.lat = lat;
    this.lng = lng;
    this.description = description;
    this.costRating = costRating;
    this.crowdRating = crowdRating;
  }
}
