package com.bitcoin.sign.bitcoindrpc;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * A payment or internal accounting entry.
 *
 * @author dquintela
 */
public class Payment {
  /**
   * The account which the payment was credited to or debited from. May be an empty string ("") for
   * the default account
   */
  @JsonProperty("account")
  private String account;
  /**
   * The address paid in this payment, which may be someone else’s address not belonging to this
   * wallet. May be empty if the address is unknown, such as when paying to a non-standard pubkey
   * script
   */
  @JsonProperty("address")
  private String address;
  /**
   * The category of payment/transaction.
   */
  @JsonProperty("category")
  private PaymentCategory category;
  /**
   * A negative bitcoin amount if sending payment; a positive bitcoin amount if receiving payment
   * (including coinbases).
   */
  @JsonProperty("amount")
  private BigDecimal amount;
  /**
   * For an output, the output index (vout) for this output in this transaction. For an input, the
   * output index for the output being spent in its transaction. Because inputs list the output
   * indexes from previous transactions, more than one entry in the details array may have the same
   * output index. Not returned for move category payments
   */
  @JsonProperty("vout")
  private int vout;
  /**
   * If sending payment, the fee paid as a negative bitcoins value. May be 0. Not returned if
   * receiving payment
   */
  @JsonProperty("fee")
  private BigDecimal fee;
  /**
   * The number of confirmations the transaction has received. Will be 0 for unconfirmed and -1 for
   * conflicted
   */
  @JsonProperty("confirmations")
  private long confirmations;
  /**
   * Set to true if the transaction is a coinbase. Not returned for regular transactions
   */
  @JsonProperty("generated")
  private boolean generated;
  /**
   * Only returned for confirmed transactions. The hash of the block on the local best block chain
   * which includes this transaction, encoded as hex in RPC byte order
   */
  @JsonProperty("blockhash")
  private String blockhash;
  /**
   * Only returned for confirmed transactions. The block height of the block on the local best block
   * chain which includes this transaction
   */
  @JsonProperty("blockindex")
  private Long blockindex;
  /**
   * Only returned for confirmed transactions. The block header time (Unix epoch time) of the block
   * on the local best block chain which includes this transaction
   */
  @JsonProperty("blocktime")
  private Date blocktime = new Date();
  /**
   * The TXID of the transaction, encoded as hex in RPC byte order. Not returned for move category
   * payments
   */
  @JsonProperty("txid")
  private String txid;
  /**
   * An array containing the TXIDs of other transactions that spend the same inputs (UTXOs) as this
   * transaction. Array may be empty.
   *
   * <p>The TXID of a conflicting transaction, encoded as hex in RPC byte order
   */
  @JsonProperty("walletconflicts")
  private List<String> walletconflicts = new LinkedList<>();
  /**
   * A Unix epoch time when the transaction was added to the wallet.
   */
  @JsonProperty("time")
  private Date time = new Date();
  /**
   * A Unix epoch time when the transaction was detected by the local node, or the time of the block
   * on the local best block chain that included the transaction.
   */
  @JsonProperty("timereceived")
  private Date timereceived = new Date();
  /**
   * For transaction originating with this wallet, a locally-stored comment added to the
   * transaction. Only returned if a comment was added
   */
  @JsonProperty("comment")
  private String comment;
  /**
   * For transaction originating with this wallet, a locally-stored comment added to the transaction
   * identifying who the transaction was sent to. Only returned if a comment-to was added
   */
  @JsonProperty("to")
  private String to;
  /**
   * Only returned by move category payments. This is the account the bitcoins were moved from or
   * moved to, as indicated by a negative or positive amount field in this payment
   */
  @JsonProperty("otheraccount")
  private String otheraccount;
  @JsonProperty("involvesWatchonly")
  private boolean involvesWatchonly;
  @JsonProperty("abandoned")
  private boolean abandoned;
  @JsonProperty("trusted")
  private boolean trusted;
  @JsonProperty("bip125-replaceable")
  private String bip125replaceable;
  @JsonProperty("label")
  private String label;

  public boolean isAbandoned() {
    return abandoned;
  }

  public boolean isTrusted() {
    return trusted;
  }

  public String getBip125replaceable() {
    return bip125replaceable;
  }

  public void setBip125replaceable(String bip125replaceable) {
    this.bip125replaceable = bip125replaceable;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getBlockhash() {
    return blockhash;
  }

  public void setBlockhash(String blockhash) {
    this.blockhash = blockhash;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public PaymentCategory getCategory() {
    return category;
  }

  public void setCategory(PaymentCategory category) {
    this.category = category;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public int getVout() {
    return vout;
  }

  public void setVout(int vout) {
    this.vout = vout;
  }

  public BigDecimal getFee() {
    return fee;
  }

  public void setFee(BigDecimal fee) {
    this.fee = fee;
  }

  public long getConfirmations() {
    return confirmations;
  }

  public void setConfirmations(long confirmations) {
    this.confirmations = confirmations;
  }

  public boolean isGenerated() {
    return generated;
  }

  public void setGenerated(boolean generated) {
    this.generated = generated;
  }

  public String getBlockHash() {
    return blockhash;
  }

  public void setBlockHash(String blockhash) {
    this.blockhash = blockhash;
  }

  public Long getBlockindex() {
    return blockindex;
  }

  public void setBlockindex(Long blockindex) {
    this.blockindex = blockindex;
  }

  public Date getBlocktime() {
    return Date.from(blocktime.toInstant());
  }

  public void setBlocktime(Date blocktime) {
    this.blocktime = Date.from(blocktime.toInstant());
  }

  public String getTxid() {
    return txid;
  }

  public void setTxid(String txid) {
    this.txid = txid;
  }

  public List<String> getWalletconflicts() {
    return walletconflicts;
  }

  public void setWalletconflicts(List<String> walletconflicts) {
    this.walletconflicts = new LinkedList<>();
    this.walletconflicts.addAll(walletconflicts);
  }

  public Date getTime() {
    return Date.from(time.toInstant());
  }

  public void setTime(Date time) {
    this.time = Date.from(time.toInstant());
  }

  public Date getTimereceived() {
    return Date.from(timereceived.toInstant());
  }

  public void setTimereceived(Date timereceived) {
    this.timereceived = Date.from(timereceived.toInstant());
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getOtheraccount() {
    return otheraccount;
  }

  public void setOtheraccount(String otheraccount) {
    this.otheraccount = otheraccount;
  }

  public boolean isInvolvesWatchonly() {
    return involvesWatchonly;
  }

  public void setInvolvesWatchonly(boolean involvesWatchonly) {
    this.involvesWatchonly = involvesWatchonly;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Payment payment = (Payment) obj;

    if (vout != payment.vout) {
      return false;
    }
    if (confirmations != payment.confirmations) {
      return false;
    }
    if (generated != payment.generated) {
      return false;
    }
    if (involvesWatchonly != payment.involvesWatchonly) {
      return false;
    }
    if (account != null ? !account.equals(payment.account) : payment.account != null) {
      return false;
    }
    if (address != null ? !address.equals(payment.address) : payment.address != null) {
      return false;
    }
    if (category != payment.category) {
      return false;
    }
    if (amount != null ? !amount.equals(payment.amount) : payment.amount != null) {
      return false;
    }
    if (fee != null ? !fee.equals(payment.fee) : payment.fee != null) {
      return false;
    }
    if (blockhash != null ? !blockhash.equals(payment.blockhash) : payment.blockhash != null) {
      return false;
    }
    if (blockindex != null ? !blockindex.equals(payment.blockindex) : payment.blockindex != null) {
      return false;
    }
    if (blocktime != null ? !blocktime.equals(payment.blocktime) : payment.blocktime != null) {
      return false;
    }
    if (txid != null ? !txid.equals(payment.txid) : payment.txid != null) {
      return false;
    }
    if (walletconflicts != null ? !walletconflicts.equals(payment.walletconflicts) :
        payment.walletconflicts != null) {
      return false;
    }
    if (time != null ? !time.equals(payment.time) : payment.time != null) {
      return false;
    }
    if (timereceived != null ? !timereceived.equals(payment.timereceived) :
        payment.timereceived != null) {
      return false;
    }
    if (comment != null ? !comment.equals(payment.comment) : payment.comment != null) {
      return false;
    }
    if (to != null ? !to.equals(payment.to) : payment.to != null) {
      return false;
    }
    return otheraccount != null ? otheraccount.equals(payment.otheraccount) :
        payment.otheraccount == null;

  }

  @Override
  public int hashCode() {
    int result = account != null ? account.hashCode() : 0;
    result = 31 * result + (address != null ? address.hashCode() : 0);
    result = 31 * result + (category != null ? category.hashCode() : 0);
    result = 31 * result + (amount != null ? amount.hashCode() : 0);
    result = 31 * result + vout;
    result = 31 * result + (fee != null ? fee.hashCode() : 0);
    result = 31 * result + (int) (confirmations ^ (confirmations >>> 32));
    result = 31 * result + (generated ? 1 : 0);
    result = 31 * result + (blockhash != null ? blockhash.hashCode() : 0);
    result = 31 * result + (blockindex != null ? blockindex.hashCode() : 0);
    result = 31 * result + (blocktime != null ? blocktime.hashCode() : 0);
    result = 31 * result + (txid != null ? txid.hashCode() : 0);
    result = 31 * result + (walletconflicts != null ? walletconflicts.hashCode() : 0);
    result = 31 * result + (time != null ? time.hashCode() : 0);
    result = 31 * result + (timereceived != null ? timereceived.hashCode() : 0);
    result = 31 * result + (comment != null ? comment.hashCode() : 0);
    result = 31 * result + (to != null ? to.hashCode() : 0);
    result = 31 * result + (otheraccount != null ? otheraccount.hashCode() : 0);
    result = 31 * result + (involvesWatchonly ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Payment [account=" + account + ", address=" + address + ", category=" + category
        + ", amount=" + amount + ", vout=" + vout + ", fee=" + fee + ", confirmations="
        + confirmations + ", generated=" + generated + ", blockhash=" + blockhash + ", blockindex="
        + blockindex + ", blocktime=" + blocktime + ", txid=" + txid + ", walletconflicts="
        + walletconflicts + ", time=" + time + ", timereceived=" + timereceived + ", comment="
        + comment + ", to=" + to + ", otheraccount=" + otheraccount + ", involvesWatchonly="
        + involvesWatchonly + "]";
  }

  public enum PaymentCategory {
    /**
     * if sending payment.
     */
    send,
    /**
     * if this wallet received payment in a regular transaction.
     */
    receive,
    /**
     * if a matured and spendable coinbase.
     */
    generate,
    /**
     * if a coinbase that is not spendable yet.
     */
    immature,
    /**
     * if a coinbase from a block that’s not in the local best block chain.
     */
    orphan,
    /**
     * if an off-block-chain move made with the move RPC.
     */
    move
  }
}
