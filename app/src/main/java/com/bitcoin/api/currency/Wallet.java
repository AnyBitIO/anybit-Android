package com.bitcoin.api.currency;


import com.bitcoin.sign.bitcoindrpc.Outpoint;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Wallet interface.
 *
 * @author Tom
 */
public interface Wallet extends OfflineWallet {

  /**
   * Provides a multi-sig account for the given addresses. There may be additional addresses
   * involved if provided in the currency configuration. The order of the addresses may matter
   * depending on the currency.
   *
   * @param addresses Addresses to include in the multi-sig script.
   * @param name      Associate the account with this user key
   */
  String getMultiSigAddress(Iterable<String> addresses, String name) throws Exception;

  class Recipient {
    private String recipientAddress;
    private BigDecimal amount;

    public String getRecipientAddress() {
      return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
      this.recipientAddress = recipientAddress;
    }

    public BigDecimal getAmount() {
      return amount;
    }

    public void setAmount(BigDecimal amount) {
      this.amount = amount;
    }
  }


  /**
   * Sign the provided transaction, the address' private key will be generated with the provided
   * account name.
   *
   * @return Same transaction with new signature data
   */
  String signTransaction(String transaction, Map<String, String> addressKey, Outpoint[] outputs, String coinType)throws Exception;

  class TransactionDetails {
    private String txHash = "";
    private Date txDate = null;
    private String[] fromAddress = new String[0];
    private String[] toAddress = new String[0];
    private BigDecimal amount;
    private String data;
    private boolean confirmed = false;

    public long getConfirmations() {
      return confirmations;
    }

    public void setConfirmations(long confirmations) {
      this.confirmations = confirmations;
    }

    public long getMinConfirmations() {
      return minConfirmations;
    }

    public void setMinConfirmations(long minConfirmations) {
      this.minConfirmations = minConfirmations;
    }

    private long confirmations = 0;
    private long minConfirmations = 0;

    public String getTxHash() {
      return txHash;
    }

    public void setTxHash(String txHash) {
      this.txHash = txHash;
    }

    public Date getTxDate() {
      return Date.from(txDate.toInstant());
    }

    public void setTxDate(Date txDate) {
      this.txDate = Date.from(txDate.toInstant());
    }

    public String getData() {
      return data;
    }

    public void setData(String data) {
      this.data = data;
    }

    /**
     * Lists the senders involved in the transaction.
     */
    public String[] getFromAddress() {
      String[] retArray = new String[fromAddress.length];
      System.arraycopy(fromAddress, 0, retArray, 0, fromAddress.length);
      return retArray;
    }

    public void setFromAddress(String[] fromAddress) {
      this.fromAddress = new String[fromAddress.length];
      System.arraycopy(fromAddress, 0, this.fromAddress, 0, fromAddress.length);
    }

    /**
     * Lists the recipients involved in the transaction.
     */
    public String[] getToAddress() {
      String[] retArray = new String[toAddress.length];
      System.arraycopy(toAddress, 0, retArray, 0, toAddress.length);
      return retArray;
    }

    public void setToAddress(String[] toAddress) {
      this.toAddress = new String[toAddress.length];
      System.arraycopy(toAddress, 0, this.toAddress, 0, toAddress.length);
    }

    public BigDecimal getAmount() {
      return amount;
    }

    public void setAmount(BigDecimal amount) {
      this.amount = amount;
    }

    public boolean isConfirmed() {
      return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
      this.confirmed = confirmed;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((amount == null) ? 0 : amount.hashCode());
      result = prime * result + Arrays.hashCode(fromAddress);
      result = prime * result + Arrays.hashCode(toAddress);
      result = prime * result + ((txDate == null) ? 0 : txDate.hashCode());
      result = prime * result + ((txHash == null) ? 0 : txHash.hashCode());
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
      TransactionDetails other = (TransactionDetails) obj;
      if (amount == null) {
        if (other.amount != null) {
          return false;
        }
      } else if (!amount.equals(other.amount)) {
        return false;
      }
      if (!Arrays.equals(fromAddress, other.fromAddress)) {
        return false;
      }
      if (!Arrays.equals(toAddress, other.toAddress)) {
        return false;
      }
      if (txDate == null) {
        if (other.txDate != null) {
          return false;
        }
      } else if (!txDate.equals(other.txDate)) {
        return false;
      }
      if (txHash == null) {
        if (other.txHash != null) {
          return false;
        }
      } else if (!txHash.equals(other.txHash)) {
        return false;
      }
      return true;
    }

    @Override
    public String toString() {
      return "TransactionDetails{" + "txHash='" + txHash + '\'' + ", txDate=" + txDate
          + ", fromAddress=" + Arrays.toString(fromAddress) + ", toAddress=" + Arrays
          .toString(toAddress) + ", amount=" + amount + ", data='" + data + '\'' + ", confirmed="
          + confirmed + ", confirmations=" + confirmations + ", minConfirmations="
          + minConfirmations + '}';
    }
  }

}
