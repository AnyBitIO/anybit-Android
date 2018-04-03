package com.bitcoin.sign.bitcoindrpc;

import com.googlecode.jsonrpc4j.JsonRpcMethod;

import java.util.Map;

/**
 * Block Chain RPCs.
 *
 * @author dquintela
 */
public interface BlockChainRpc {

  /**
   * GetBlockChainInfo Added in Bitcoin Core 0.9.2
   *
   * <p>The getblockchaininfo RPC provides information about the current state of the block chain.
   */
  @JsonRpcMethod("getblockchaininfo")
  BlockChainInfo getblockchaininfo();

  /**
   * GetBlockCount
   *
   * <p>The getblockcount RPC returns the number of blocks in the local best block chain.
   *
   * @return The number of blocks in the local best block chain.
   */
  @JsonRpcMethod("getblockcount")
  long getBlockCount();

  /**
   * GetBlockHash
   *
   * <p>The getBlockHash RPC returns the header hash of a block at the given height in the local
   * best block chain.
   *
   * @param blockHeight The height of the block whose header hash should be returned. The height of
   *                    the hardcoded genesis block is 0
   */
  @JsonRpcMethod("getblockhash")
  String getBlockHash(long blockHeight);

  @JsonRpcMethod("getblock")
  Map<String, Object> getBlock(String blockHash);
}
