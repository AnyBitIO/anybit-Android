package com.bitcoin.sign;

import com.bitcoin.sign.bitcoindrpc.BitcoindRpc;
import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.ProxyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Static connection to a bitcoind RPC server.
 *
 * @author Tom
 */
public class BitcoinResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(BitcoinResource.class);
  private static final BitcoinResource serverResource = new BitcoinResource();
  private BitcoinConfiguration config;
  private JsonRpcHttpClient client;
  private BitcoindRpc bitcoindRpc;

  public static BitcoinResource getResource() {
    return serverResource;
  }

  private static BitcoindRpc innerRpc;
  private static LinkedList<Lock> requestLocks = new LinkedList<>();
  private int concurrentRpcRequests = 10;

  private BitcoinResource() {
    this.config = new BitcoinConfiguration();
    concurrentRpcRequests = config.getMaxNodeConnections();

    try {
      // Set up our RPC authentication
      Authenticator.setDefault(new Authenticator() {
        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(config.getDaemonUser(),
              config.getDaemonPassword().toCharArray());
        }
      });

      this.client = new JsonRpcHttpClient(new URL(config.getDaemonConnectionString()));

    } catch (MalformedURLException e) {
      LOGGER.error(null, e);
    }
  }

  public BitcoinResource(BitcoindRpc rpc) {
    this.bitcoindRpc = rpc;
  }

  public void setBitcoindRpc(BitcoindRpc rpc) {
    this.bitcoindRpc = rpc;
  }

  /**
   * Get an RPC object that is connected to a bitcoind node.
   *
   * @return RPC object
   */
  public synchronized BitcoindRpc getBitcoindRpc() {
    if (bitcoindRpc == null) {
      // We're creating multiple reentrant locks to allow a limited number of requests to run at the same time
      requestLocks.clear();
      for (int i = 0; i < concurrentRpcRequests; i++) {
        requestLocks.add(new ReentrantLock());
      }
      innerRpc = ProxyUtil.createClientProxy(getClass().getClassLoader(), BitcoindRpc.class, client);
      // Proxy wrapped around the jsonrpc4j calls, will only make the actual call to the node
      // when it can obtain a lock. Otherwise it will wait for a small amount of time before
      // trying the next availble lock.
      this.bitcoindRpc = (BitcoindRpc) Proxy
          .newProxyInstance(getClass().getClassLoader(), new Class<?>[]{BitcoindRpc.class},
              (o, method, objects) -> {
                int lockNumber = 0;
                while (true) {
                  if (requestLocks.get(lockNumber).tryLock()) {
                    try {
                      return method.invoke(innerRpc, objects);
                    } catch (Exception e) {
                      LOGGER.error("Problem invoking RPC call", e);
                      // We don't want the invocation error, we want the node response.
                      LOGGER.error("Throwing", e.getCause());
                      throw e.getCause();
                    } finally {
                      requestLocks.get(lockNumber).unlock();
                    }
                  } else {
                    lockNumber++;
                    lockNumber %= requestLocks.size();
                    LOGGER.debug(
                        "Failed to obtain bitcoinRpc lock, trying the next one: " + lockNumber);
                    Thread.sleep(1);
                  }
                }
              });
    }

    return this.bitcoindRpc;
  }
}
