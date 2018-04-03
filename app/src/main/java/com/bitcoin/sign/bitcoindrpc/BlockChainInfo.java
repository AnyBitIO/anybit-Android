package com.bitcoin.sign.bitcoindrpc;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Information about the current state of the local block chain.
 *
 * @author dquintela
 */
public class BlockChainInfo {
  /**
   * The hash of the header of the highest validated block in the best block chain, encoded as hex
   * in RPC byte order. This is identical to the string returned by the getbestblockhash RPC
   */
  @JsonProperty("bestblockhash")
  private String bestblockhash;

  /**
   * The number of validated blocks in the local best block chain. For a new node with just the
   * hardcoded genesis block, this will be 0
   */
  @JsonProperty("blocks")
  private long blocks;

  /**
   * The name of the block chain. One of main for mainnet, test for testnet, or regtest for regtest
   */
  @JsonProperty("chain")
  private BlockChainName chain;

  /**
   * Highest block available when pruning is enabled on the node.
   */
  @JsonProperty("pruneheight")
  private long pruneheight;

  /**
   * The estimated number of block header hashes checked from the genesis block to this block,
   * encoded as big-endian hex.
   */
  @JsonProperty("chainwork")
  private String chainwork;

  /**
   * The difficulty of the highest-height block in the best block chain.
   */
  @JsonProperty("difficulty")
  private BigDecimal difficulty;

  /**
   * The number of validated headers in the local best headers chain. For a new node with just the
   * hardcoded genesis block, this will be zero. This number may be higher than the number of
   * blocks.
   */
  @JsonProperty("headers")
  private long headers;

  @JsonProperty("pruned")
  private String pruned;

  @JsonProperty("softforks")
  private SoftForks[] softforks;

  public String getMediantime() {
    return mediantime;
  }

  public void setMediantime(String mediantime) {
    this.mediantime = mediantime;
  }

  @JsonProperty("mediantime")
  private String mediantime;

  /**
   * Estimate of what percentage of the block chain transactions have been verified so far, starting
   * at 0.0 and increasing to 1.0 for fully verified. May slightly exceed 1.0 when fully synced to
   * account for transactions in the memory pool which have been verified before being included in a
   * block
   */
  @JsonProperty("verificationprogress")
  private BigDecimal verificationprogress;

  public HashMap<String, Object> getBip9_softforks() {
    return bip9_softforks;
  }

  public void setBip9_softforks(HashMap<String, Object> bip9_softforks) {
    this.bip9_softforks = bip9_softforks;
  }

  @JsonProperty("bip9_softforks")
  private HashMap<String, Object> bip9_softforks;

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
    BlockChainInfo other = (BlockChainInfo) obj;
    if (bestblockhash == null) {
      if (other.bestblockhash != null) {
        return false;
      }
    } else if (!bestblockhash.equals(other.bestblockhash)) {
      return false;
    }
    if (blocks != other.blocks) {
      return false;
    }
    if (pruneheight != other.pruneheight) {
      return false;
    }
    if (chain != other.chain) {
      return false;
    }
    if (chainwork == null) {
      if (other.chainwork != null) {
        return false;
      }
    } else if (!chainwork.equals(other.chainwork)) {
      return false;
    }
    if (difficulty == null) {
      if (other.difficulty != null) {
        return false;
      }
    } else if (!difficulty.equals(other.difficulty)) {
      return false;
    }
    if (headers != other.headers) {
      return false;
    }
    if (pruned == null) {
      if (other.pruned != null) {
        return false;
      }
    } else if (!pruned.equals(other.pruned)) {
      return false;
    }
    if (verificationprogress == null) {
      if (other.verificationprogress != null) {
        return false;
      }
    } else if (!verificationprogress.equals(other.verificationprogress)) {
      return false;
    }
    return true;
  }

  public String getBestblockhash() {
    return bestblockhash;
  }

  public long getBlocks() {
    return blocks;
  }

  public long getPruneheight() {
    return pruneheight;
  }

  public BlockChainName getChain() {
    return chain;
  }

  public String getChainwork() {
    return chainwork;
  }

  public BigDecimal getDifficulty() {
    return difficulty;
  }

  public long getHeaders() {
    return headers;
  }

  public String getPruned() {
    return pruned;
  }

  /**
   * Softforks.
   */
  public SoftForks[] getSoftforks() {
    SoftForks[] returnArray = new SoftForks[softforks.length];
    System.arraycopy(softforks, 0, returnArray, 0, softforks.length);
    return returnArray;
  }

  /**
   * Softforks.
   */
  public void setSoftforks(SoftForks[] softforks) {
    this.softforks = new SoftForks[softforks.length];
    System.arraycopy(softforks, 0, this.softforks, 0, this.softforks.length);
  }

  public BigDecimal getVerificationprogress() {
    return verificationprogress;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((bestblockhash == null) ? 0 : bestblockhash.hashCode());
    result = prime * result + (int) (blocks ^ (blocks >>> 32));
    result = prime * result + ((chain == null) ? 0 : chain.hashCode());
    result = prime * result + ((chainwork == null) ? 0 : chainwork.hashCode());
    result = prime * result + ((difficulty == null) ? 0 : difficulty.hashCode());
    result = prime * result + (int) (headers ^ (headers >>> 32));
    result = prime * result + ((pruned == null) ? 0 : pruned.hashCode());
    result =
            prime * result + ((verificationprogress == null) ? 0 : verificationprogress.hashCode());
    return result;
  }

  public void setBestblockhash(String bestblockhash) {
    this.bestblockhash = bestblockhash;
  }

  public void setBlocks(long blocks) {
    this.blocks = blocks;
  }

  public void setPruneheight(long pruneheight) {
    this.pruneheight = pruneheight;
  }

  public void setChain(BlockChainName chain) {
    this.chain = chain;
  }

  public void setChainwork(String chainwork) {
    this.chainwork = chainwork;
  }

  public void setDifficulty(BigDecimal difficulty) {
    this.difficulty = difficulty;
  }

  public void setHeaders(long headers) {
    this.headers = headers;
  }

  public void setPruned(String pruned) {
    this.pruned = pruned;
  }

  public void setVerificationprogress(BigDecimal verificationprogress) {
    this.verificationprogress = verificationprogress;
  }

  @Override
  public String toString() {
    return "BlockChainInfo [chain=" + chain + ", blocks=" + blocks + ", pruneheight=" + pruneheight
            + ", headers=" + headers + ", bestblockhash=" + bestblockhash + ", difficulty="
            + difficulty + ", verificationprogress=" + verificationprogress + ", chainwork="
            + chainwork + ", pruned=" + pruned + "]";
  }
}
