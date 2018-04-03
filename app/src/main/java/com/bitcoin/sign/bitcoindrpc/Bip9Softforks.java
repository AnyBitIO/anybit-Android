package com.bitcoin.sign.bitcoindrpc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bip9Softforks {
  @JsonProperty("id")
  private String id;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("status")
  private String status;
}
