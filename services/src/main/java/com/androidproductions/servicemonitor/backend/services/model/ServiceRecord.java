/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * This code was generated by https://code.google.com/p/google-apis-client-generator/
 * (build: 2014-04-15 19:10:39 UTC)
 * on 2014-04-24 at 16:17:40 UTC 
 * Modify at your own risk.
 */

package com.androidproductions.servicemonitor.backend.services.model;

/**
 * Model definition for ServiceRecord.
 *
 * <p> This is the Java data model class that specifies how to parse/serialize into the JSON that is
 * transmitted over HTTP when working with the services. For a detailed explanation see:
 * <a href="http://code.google.com/p/google-http-java-client/wiki/JSON">http://code.google.com/p/google-http-java-client/wiki/JSON</a>
 * </p>
 *
 * @author Google, Inc.
 */
@SuppressWarnings("javadoc")
public final class ServiceRecord extends com.google.api.client.json.GenericJson {

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key @com.google.api.client.json.JsonString
  private java.lang.Long lastUpdate;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String serviceGroup;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.String serviceId;

  /**
   * The value may be {@code null}.
   */
  @com.google.api.client.util.Key
  private java.lang.Integer status;

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Long getLastUpdate() {
    return lastUpdate;
  }

  /**
   * @param lastUpdate lastUpdate or {@code null} for none
   */
  public ServiceRecord setLastUpdate(java.lang.Long lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getServiceGroup() {
    return serviceGroup;
  }

  /**
   * @param serviceGroup serviceGroup or {@code null} for none
   */
  public ServiceRecord setServiceGroup(java.lang.String serviceGroup) {
    this.serviceGroup = serviceGroup;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.String getServiceId() {
    return serviceId;
  }

  /**
   * @param serviceId serviceId or {@code null} for none
   */
  public ServiceRecord setServiceId(java.lang.String serviceId) {
    this.serviceId = serviceId;
    return this;
  }

  /**
   * @return value or {@code null} for none
   */
  public java.lang.Integer getStatus() {
    return status;
  }

  /**
   * @param status status or {@code null} for none
   */
  public ServiceRecord setStatus(java.lang.Integer status) {
    this.status = status;
    return this;
  }

  @Override
  public ServiceRecord set(String fieldName, Object value) {
    return (ServiceRecord) super.set(fieldName, value);
  }

  @Override
  public ServiceRecord clone() {
    return (ServiceRecord) super.clone();
  }

}
