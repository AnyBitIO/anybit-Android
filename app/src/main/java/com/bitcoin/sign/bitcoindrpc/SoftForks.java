package com.bitcoin.sign.bitcoindrpc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SoftForks {
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public Status getEnforce() {
    return enforce;
  }

  public void setEnforce(Status enforce) {
    this.enforce = enforce;
  }

  public Status getReject() {
    return reject;
  }

  public void setReject(Status reject) {
    this.reject = reject;
  }

  public class Status {
    @JsonProperty("status")
    private String status;

    @JsonProperty("found")
    private String found;

    @JsonProperty("required")
    private String required;

    @JsonProperty("window")
    private String window;

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public String getFound() {
      return found;
    }

    public void setFound(String found) {
      this.found = found;
    }

    public String getRequired() {
      return required;
    }

    public void setRequired(String required) {
      this.required = required;
    }

    public String getWindow() {
      return window;
    }

    public void setWindow(String window) {
      this.window = window;
    }
  }

  @JsonProperty("id")
  private String id;

  @JsonProperty("version")
  private String version;

  @JsonProperty("enforce")
  private Status enforce;

  @JsonProperty("reject")
  private Status reject;
}
