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

/* This class is a marker, containing latitude and longitude values,
and ID. It is intended to be used as a marker on a Google map */
public class Marker {

    /* Information stored for each Marker */
    private final double lat;
    private final double lng;
    private final long id;
    private final String category;

    public Marker(double lat, double lng, long id, String category) {
      this.lat = lat;
      this.lng = lng;
      this.id = id;
      this.category = category;
    }

    public double getLat() {
      return this.lat;
    }

    public double getLng() {
      return this.lng;
    }

    public long getId() {
      return this.id;
    }

    public String getCategory() {
      return this.category;
    }
}
