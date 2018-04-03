package com.bitcoin.sign.bitcoindrpc;

import com.bitcoin.common.ByteUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.LinkedList;

public final class RawInput {
  private static final Logger LOGGER = LoggerFactory.getLogger(RawInput.class);
  private String txHash;
  private int txIndex;
  private long scriptSize = 0;
  private String script = "";
  private int sequence = -1;

  public String getTxHash() {
    return txHash;
  }

  public void setTxHash(String txHash) {
    this.txHash = txHash;
  }

  public int getTxIndex() {
    return txIndex;
  }

  public void setTxIndex(int txIndex) {
    this.txIndex = txIndex;
  }

  public long getScriptSize() {
    return scriptSize;
  }

  public void setScriptSize(long scriptSize) {
    this.scriptSize = scriptSize;
  }

  public String getScript() {
    return script;
  }

  public void setScript(String script) {
    this.script = script;
  }

  public int getSequence() {
    return sequence;
  }

  public void setSequence(int sequence) {
    this.sequence = sequence;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((script == null) ? 0 : script.hashCode());
    result = prime * result + (int) (scriptSize ^ (scriptSize >>> 32));
    result = prime * result + sequence;
    result = prime * result + ((txHash == null) ? 0 : txHash.hashCode());
    result = prime * result + txIndex;
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
    RawInput other = (RawInput) obj;
    if (script == null) {
      if (other.script != null) {
        return false;
      }
    } else if (!script.equals(other.script)) {
      return false;
    }
    if (scriptSize != other.scriptSize) {
      return false;
    }
    if (sequence != other.sequence) {
      return false;
    }
    if (txHash == null) {
      if (other.txHash != null) {
        return false;
      }
    } else if (!txHash.equals(other.txHash)) {
      return false;
    }
    return txIndex == other.txIndex;
  }

  @Override
  public String toString() {
    return "RawInput [txHash=" + txHash + ", txIndex=" + txIndex + ", scriptSize=" + scriptSize
        + ", script=" + script + ", sequence=" + sequence + "]";
  }

  /**
   * Encodes this input as a byte array.
   *
   * @return Hex string representing the input.
   */
  public String encode() {
    String tx = "";
    // Tx Hash
    byte[] hashBytes = ByteUtilities.toByteArray(getTxHash());
    hashBytes = ByteUtilities.leftPad(hashBytes, 32, (byte) 0x00);
    hashBytes = ByteUtilities.flipEndian(hashBytes);
    tx += ByteUtilities.toHexString(hashBytes);

    // Tx Index
    byte[] indexBytes =
        ByteUtilities.stripLeadingNullBytes(BigInteger.valueOf(getTxIndex()).toByteArray());
    indexBytes = ByteUtilities.leftPad(indexBytes, 4, (byte) 0x00);
    indexBytes = ByteUtilities.flipEndian(indexBytes);
    tx += ByteUtilities.toHexString(indexBytes);

    // Script Size
    setScriptSize(getScript() == null ? 0 : getScript().length() / 2L);
    byte[] scriptSizeBytes = RawTransaction.writeVariableInt(getScriptSize());
    tx += ByteUtilities.toHexString(scriptSizeBytes);

    // Script
    String script = getScript();
    byte[] scriptBytes = ByteUtilities.toByteArray(getScript());
    tx += ByteUtilities.toHexString(scriptBytes);

    // Sequence
    byte[] sequenceBytes =
        ByteUtilities.stripLeadingNullBytes(BigInteger.valueOf(getSequence()).toByteArray());
    sequenceBytes = ByteUtilities.leftPad(sequenceBytes, 4, (byte) 0xFF);
    sequenceBytes = ByteUtilities.flipEndian(sequenceBytes);
    tx += ByteUtilities.toHexString(sequenceBytes);

    return tx;
  }

  /**
   * Parses a hex string that represents an input, and converts it into a RawInput
   *
   * @param txData Hex string representing the input
   * @return RawInput generated from the input.
   */
  public static RawInput parse(String txData) {
    RawInput input = new RawInput();
    byte[] rawTx = ByteUtilities.toByteArray(txData);
    int buffPointer = 0;

    byte[] hashBytes = ByteUtilities.readBytes(rawTx, buffPointer, 32);
    buffPointer += 32;
    hashBytes = ByteUtilities.flipEndian(hashBytes);
    input.setTxHash(ByteUtilities.toHexString(hashBytes));

    byte[] indexBytes = ByteUtilities.readBytes(rawTx, buffPointer, 4);
    buffPointer += 4;
    indexBytes = ByteUtilities.flipEndian(indexBytes);
    input.setTxIndex(new BigInteger(1, indexBytes).intValue());

    RawTransaction.VariableInt varScriptSize = RawTransaction.readVariableInt(rawTx, buffPointer);
    buffPointer += varScriptSize != null ? varScriptSize.getSize() : 0;
    input.setScriptSize(varScriptSize != null ? varScriptSize.getValue() : 0);

    byte[] scriptBytes = ByteUtilities.readBytes(rawTx, buffPointer, (int) input.getScriptSize());
    buffPointer += input.getScriptSize();
    input.setScript(ByteUtilities.toHexString(scriptBytes));

    byte[] sequenceBytes = ByteUtilities.readBytes(rawTx, buffPointer, 4);
    //buffPointer += 4;
    sequenceBytes = ByteUtilities.flipEndian(sequenceBytes);
    input.setSequence(new BigInteger(1, sequenceBytes).intValue());

    return input;
  }

  /**
   * Returns the size of the encoded data
   *
   * @return Size of the encoded data.
   */
  public long getDataSize() {
    int sizeSize = RawTransaction.writeVariableInt(getScriptSize()).length;
    // Tx Hash + Index + scriptSize + Script + sequence
    return 32 + 4 + sizeSize + getScriptSize() + 4;
  }

  /**
   * Copies the current RawInput
   *
   * @return A new copy of this RawInput.
   */
  public RawInput copy() {
    RawInput input = new RawInput();

    input.setTxHash(getTxHash());
    input.setTxIndex(getTxIndex());
    input.setScriptSize(getScriptSize());
    input.setScript(getScript());
    input.setSequence(getSequence());

    return input;
  }

  /**
   * Removes the multi-sig redeem script from the input signature so that more signatures can be
   * appended.
   *
   * @param redeemScript Script that we want to remove.
   */
  public void stripMultiSigRedeemScript(String redeemScript) {
    Iterable<String> stackItems = getSignatures(this.getScript(), redeemScript);

    LOGGER.debug("Rebuilding script with stack: " + stackItems);
    StringBuilder myScriptString = new StringBuilder();
    for (String item : stackItems) {
      byte[] itemBytes = ByteUtilities.toByteArray(item);
      byte[] prefixBytes = RawTransaction.writeVariableStackInt(itemBytes.length);
      myScriptString.append(ByteUtilities.toHexString(prefixBytes));
      myScriptString.append(ByteUtilities.toHexString(itemBytes));
    }

    setScript(myScriptString.toString());
  }

  public static Iterable<String> getSignatures(String signedScript, String redeemScript) {
    LinkedList<String> stackItems = new LinkedList<>();
    byte[] myScript = ByteUtilities.toByteArray(signedScript);
    int bufferPointer = 0;
    RawTransaction.VariableInt stackItemSize;
    String stackItem;
    while (bufferPointer < myScript.length) {
      stackItemSize = RawTransaction.readVariableStackInt(myScript, bufferPointer);
      bufferPointer += stackItemSize != null ? stackItemSize.getSize() : 0;
      stackItem = ByteUtilities.toHexString(ByteUtilities.readBytes(myScript, bufferPointer,
          (int) (stackItemSize != null ? stackItemSize.getValue() : 0)));
      bufferPointer += stackItemSize != null ? stackItemSize.getValue() : 0;
      if (!stackItem.equalsIgnoreCase(redeemScript)) {
        stackItems.add(stackItem);
      }
    }

    return stackItems;
  }

  /**
   * Returns the number of signatures already on the redeemscript.
   */
  public int numberOfSigners(String redeemScript) {
    return numberOfSigners(this.getScript(), redeemScript);
  }

  public static int numberOfSigners(String signedScript, String redeemScript) {
    LinkedList<String> stackItems = new LinkedList<>();
    byte[] myScript = ByteUtilities.toByteArray(signedScript);
    int bufferPointer = 0;
    RawTransaction.VariableInt stackItemSize;
    String stackItem;
    while (bufferPointer < myScript.length) {
      stackItemSize = RawTransaction.readVariableStackInt(myScript, bufferPointer);
      bufferPointer += stackItemSize != null ? stackItemSize.getSize() : 0;
      stackItem = ByteUtilities.toHexString(ByteUtilities.readBytes(myScript, bufferPointer,
          (int) (stackItemSize != null ? stackItemSize.getValue() : 0)));
      bufferPointer += stackItemSize != null ? stackItemSize.getValue() : 0;
      if (!stackItem.equalsIgnoreCase(redeemScript) && stackItem != null && !stackItem.isEmpty()) {
        stackItems.add(stackItem);
      }
    }

    return stackItems.size();
  }
}
