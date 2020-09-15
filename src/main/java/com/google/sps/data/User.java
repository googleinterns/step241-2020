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

  private final String email;
  private final String name;
  private final String department;
  private final int year;
  private final String phone;
  private final String bio;
  private final String profilePictureUrl;
  private final long updatedTime;

  public User(String email, String name, String department, int year, String phone, String bio, String profilePictureUrl, long updatedTime) {
    this.email = email;
    this.name = name;
    this.department = department;
    this.year = year;
    this.phone = phone;
    this.bio = bio;
    this.profilePictureUrl = profilePictureUrl;
    this.updatedTime = updatedTime;
  }

  public User(String email, String name, String department, int year, String profilePictureUrl, long updatedTime) {
    this(email, name, department, year, 0, "", profilePictureUrl, updatedTime);
  }
}
