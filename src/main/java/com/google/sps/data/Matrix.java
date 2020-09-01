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

/** A class representing a matrix with n row(s) and m coloumn(s) */
public final class Matrix {

  private final int[][] data;
  private final int n;
  private final int m;

  // creates a matrix of size n x m with empty values
  public Matrix(int n, int m) {
    this(new int[n][m], n, m);
  }

  // creates a matrix of size n x m with given values
  public Matrix(int[][] data, int n, int m) {
    this.data = data;
    this.n = n;
    this.m = m;
  }

  // set the value at row i and coloumn j
  public void setValue(int value, int i, int j) {
    data[i][j] = value;
  }

  // get the value at row i and coloumn j
  public int getValue(int i, int j) {
    return data[i][j];
  }

  // return the number of row in the matrix
  public int getRowSize() {
    return n;
  }

  // return the number of coloumn in the matrix
  public int getColoumnSize() {
    return m;
  }

  // return a matrix which is the result of transposing this matrix
  public Matrix transpose(){
    Matrix transposed = new Matrix(m, n);
    for(int i = 0; i < n; i++) {
      for(int j = 0; j < m; j++) {
        transposed.setValue(data[i][j], j, i);
      }
    }
    return transposed;
  }

  // return a matrix which is the result of multiplying
  // this matrix and another matrix
  public Matrix multiply(Matrix that) {
    if(m != that.getRowSize()) {
      return null;
    }
    int result_n = this.getRowSize();
    int result_m = that.getColoumnSize();
    Matrix result = new Matrix(result_n, result_m);
    for(int i = 0; i < result_n; i++) {
      for (int j = 0; j < result_m; j++) {
        int value = 0;
        for(int k = 0; k < this.getColoumnSize(); k++) {
          value += this.getValue(i, k) * that.getValue(k, j);
        }
        result.setValue(value, i, j);
      }
    }
    return result;
  }
}
