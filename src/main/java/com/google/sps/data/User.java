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

/** The class represents user details. */
public final class User {

  private final String userEmail;
  private final String name;
  private final String department;
  private final String bio;
  private final String profilePictureUrl;
  private final long timestamp;

  public User(String userEmail, String name, String department, String bio, String profilePictureUrl, long timestamp) {
    this.userEmail = userEmail;
    this.name = name;
    this.department = department;
    this.bio = bio;
    this.profilePictureUrl = profilePictureUrl;
    this.timestamp = timestamp;
  }
}
