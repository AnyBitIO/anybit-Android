package com.bitcoin.api.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Server {
  @JsonProperty
  private String serverLocation;
  @JsonProperty
  private int serverRpcPort;
  @JsonProperty
  private int serverListeningPort;
  @JsonProperty
  private boolean isOriginator;
  @JsonProperty
  private String serverId;
  @JsonProperty
  private long lastCommunication;
  @JsonProperty
  private Map<String, ServerStatus> currencyStatus;
  @JsonProperty
  private String sigR;
  @JsonProperty
  private String sigS;
  @JsonProperty
  private String sigV;

  public String getServerLocation() {
    return serverLocation;
  }

  public void setServerLocation(String serverLocation) {
    this.serverLocation = serverLocation;
  }

  public int getServerRpcPort() {
    return serverRpcPort;
  }

  public void setServerRpcPort(int serverRpcPort) {
    this.serverRpcPort = serverRpcPort;
  }

  public int getServerListeningPort() {
    return serverListeningPort;
  }

  public void setServerListeningPort(int serverListeningPort) {
    this.serverListeningPort = serverListeningPort;
  }

  public boolean isOriginator() {
    return isOriginator;
  }

  public void setOriginator(boolean isOriginator) {
    this.isOriginator = isOriginator;
  }

  public String getServerId() {
    return serverId;
  }

  public void setServerId(String serverId) {
    this.serverId = serverId;
  }

  public long getLastCommunication() {
    return lastCommunication;
  }

  public void setLastCommunication(long lastCommunication) {
    this.lastCommunication = lastCommunication;
  }

  public Map<String, ServerStatus> getCurrencyStatus() {
    return currencyStatus;
  }

  public void setCurrencyStatus(Map<String, ServerStatus> currencyStatus) {
    this.currencyStatus = currencyStatus;
  }

  public String getSigR() {
    return sigR;
  }

  public void setSigR(String sigR) {
    this.sigR = sigR;
  }

  public String getSigS() {
    return sigS;
  }

  public void setSigS(String sigS) {
    this.sigS = sigS;
  }

  public String getSigV() {
    return sigV;
  }

  public void setSigV(String sigV) {
    this.sigV = sigV;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((serverId == null) ? 0 : serverId.hashCode());
    result = prime * result + serverListeningPort;
    result = prime * result + serverRpcPort;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Server other = (Server) obj;
    if (serverId == null) {
      if (other.serverId != null) {
        return false;
      }
    } else if (!serverId.equals(other.serverId)) {
      return false;
    }
    if (serverListeningPort != other.serverListeningPort) {
      return false;
    }
    return serverRpcPort == other.serverRpcPort;
  }

  @Override
  public String toString() {
    return "Server [serverLocation=" + serverLocation + ", serverRpcPort=" + serverRpcPort
        + ", serverListeningPort=" + serverListeningPort + ", isOriginator=" + isOriginator
        + ", serverId=" + serverId + ", lastCommunication=" + lastCommunication + ", sigR=" + sigR
        + ", sigS=" + sigS + ", sigV=" + sigV + "]";
  }

}
