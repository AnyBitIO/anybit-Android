package com.bitcoin.sign.bitcoindrpc;

import com.bitcoin.common.ByteUtilities;
import com.bitcoin.sign.BitcoinResource;
import com.bitcoin.sign.common.BitcoinTools;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to convert between a raw transaction and the data structure represented here.
 *
 * @author dorgky
 */
public class RawTransaction {
  private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RawTransaction.class);
  private int version;
  private long inputCount = 0;
  private List<RawInput> inputs = new LinkedList<>();
  private long outputCount = 0;
  private List<RawOutput> outputs = new LinkedList<>();
  private long lockTime;

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public long getInputCount() {
    return inputCount;
  }

  public void setInputCount(long inputCount) {
    this.inputCount = inputCount;
  }

  public List<RawInput> getInputs() {
    return inputs;
  }

  public void setInputs(List<RawInput> inputs) {
    this.inputs = new LinkedList<>();
    this.inputs.addAll(inputs);
  }

  public long getOutputCount() {
    return outputCount;
  }

  public void setOutputCount(long outputCount) {
    this.outputCount = outputCount;
  }

  public List<RawOutput> getOutputs() {
    return outputs;
  }

  public void setOutputs(List<RawOutput> outputs) {
    this.outputs = new LinkedList<>();
    this.outputs.addAll(outputs);
  }

  public long getLockTime() {
    return lockTime;
  }

  public void setLockTime(long lockTime) {
    this.lockTime = lockTime;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (int) (inputCount ^ (inputCount >>> 32));
    result = prime * result + ((inputs == null) ? 0 : inputs.hashCode());
    result = prime * result + (int) (lockTime ^ (lockTime >>> 32));
    result = prime * result + (int) (outputCount ^ (outputCount >>> 32));
    result = prime * result + ((outputs == null) ? 0 : outputs.hashCode());
    result = prime * result + version;
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
    RawTransaction other = (RawTransaction) obj;
    if (inputCount != other.inputCount) {
      return false;
    }
    if (inputs == null) {
      if (other.inputs != null) {
        return false;
      }
    } else if (!inputs.equals(other.inputs)) {
      return false;
    }
    if (lockTime != other.lockTime) {
      return false;
    }
    if (outputCount != other.outputCount) {
      return false;
    }
    if (outputs == null) {
      if (other.outputs != null) {
        return false;
      }
    } else if (!outputs.equals(other.outputs)) {
      return false;
    }
    return version == other.version;
  }

  @Override
  public String toString() {
    return "RawTransaction [version=" + version + ", inputCount=" + inputCount + ", inputs="
        + inputs + ", outputCount=" + outputCount + ", outputs=" + outputs + ", lockTime="
        + lockTime + "]";
  }

  /**
   * Returns a String representing the raw transaction.
   *
   * @return Hex string representing the raw transaction.
   */
  public String encode() {
    StringBuilder tx = new StringBuilder();

    // Version
    byte[] versionBytes =
        ByteUtilities.stripLeadingNullBytes(BigInteger.valueOf(getVersion()).toByteArray());
    versionBytes = ByteUtilities.leftPad(versionBytes, 4, (byte) 0x00);
    versionBytes = ByteUtilities.flipEndian(versionBytes);
    tx.append(ByteUtilities.toHexString(versionBytes));

    // Number of inputs
    setInputCount(getInputs().size());
    byte[] inputSizeBytes = writeVariableInt(getInputCount());
    tx.append(ByteUtilities.toHexString(inputSizeBytes));

    // Inputs
    for (int i = 0; i < getInputCount(); i++) {
      tx.append(getInputs().get(i).encode());
    }

    // Number of outputs
    setOutputCount(getOutputs().size());
    byte[] outputSizeBytes = writeVariableInt(getOutputCount());
    tx.append(ByteUtilities.toHexString(outputSizeBytes));

    // Outputs
    for (int i = 0; i < getOutputCount(); i++) {
      tx.append(getOutputs().get(i).encode());
    }

    // Lock Time
    byte[] lockBytes =
        ByteUtilities.stripLeadingNullBytes(BigInteger.valueOf(getLockTime()).toByteArray());
    lockBytes = ByteUtilities.leftPad(lockBytes, 4, (byte) 0x00);
    lockBytes = ByteUtilities.flipEndian(lockBytes);
    String sult = ByteUtilities.toHexString(lockBytes);
    tx.append(ByteUtilities.toHexString(lockBytes));

    return tx.toString();
  }

  /**
   * Decode a raw trasaction.
   *
   * @param txData Hex string representing the transaction.
   * @return Corresponding RawTransaction object.
   */
  public static RawTransaction parse(String txData) {
    RawTransaction tx = new RawTransaction();
    byte[] rawTx = ByteUtilities.toByteArray(txData);
    int buffPointer = 0;

    // Version
    byte[] version = ByteUtilities.readBytes(rawTx, buffPointer, 4);
    buffPointer += 4;
    version = ByteUtilities.flipEndian(version);
    tx.setVersion(new BigInteger(1, version).intValue());

    // Number of inputs
    VariableInt varInputCount = readVariableInt(rawTx, buffPointer);
    buffPointer += varInputCount != null ? varInputCount.getSize() : 0;
    tx.setInputCount(varInputCount != null ? varInputCount.getValue() : 0);

    // Parse inputs
    for (long i = 0; i < tx.getInputCount(); i++) {
      byte[] inputData = Arrays.copyOfRange(rawTx, buffPointer, rawTx.length);
      RawInput input = RawInput.parse(ByteUtilities.toHexString(inputData));
      buffPointer += input.getDataSize();
      tx.getInputs().add(input);
    }

    // Get the number of outputs
    VariableInt varOutputCount = readVariableInt(rawTx, buffPointer);
    buffPointer += varOutputCount != null ? varOutputCount.getSize() : 0;
    tx.setOutputCount(varOutputCount != null ? varOutputCount.getValue() : 0);

    // Parse outputs
    for (long i = 0; i < tx.getOutputCount(); i++) {
      byte[] outputData = Arrays.copyOfRange(rawTx, buffPointer, rawTx.length);
      RawOutput output = RawOutput.parse(ByteUtilities.toHexString(outputData));
      buffPointer += output.getDataSize();
      tx.getOutputs().add(output);
    }

    // Parse lock time
    byte[] lockBytes = ByteUtilities.readBytes(rawTx, buffPointer, 4);
    //buffPointer += 4;
    lockBytes = ByteUtilities.flipEndian(lockBytes);
    tx.setLockTime(new BigInteger(1, lockBytes).longValue());

    return tx;
  }

  public static class VariableInt {
    int size = 0;
    long value = 0;

    public int getSize() {
      return size;
    }

    public void setSize(int size) {
      this.size = size;
    }

    public long getValue() {
      return value;
    }

    public void setValue(long value) {
      this.value = value;
    }
  }

  /**
   * Parse a byte array of data to extract a variable length integer.
   *
   * @param data  Byte array that contains the integer.
   * @param start Position of integer in the byte array.
   * @return Information about the integer
   */
  public static VariableInt readVariableInt(byte[] data, int start) {
    if (data == null || data.length <= start) {
      return new VariableInt();
    }
    int checkSize = 0xFF & data[start];
    VariableInt varInt = new VariableInt();
    varInt.setSize(0);

    if (checkSize < 0xFD) {
      varInt.setSize(1);
      varInt.setValue(checkSize);
      return varInt;
    }

    if (checkSize == 0xFD) {
      varInt.setSize(3);
    } else if (checkSize == 0xFE) {
      varInt.setSize(5);
    } else if (checkSize == 0xFF) {
      varInt.setSize(9);
    }

    if (varInt.getSize() == 0) {
      return null;
    }

    byte[] newData = ByteUtilities.readBytes(data, start + 1, varInt.getSize() - 1);
    newData = ByteUtilities.flipEndian(newData);
    varInt.setValue(new BigInteger(1, newData).longValue());
    return varInt;
  }

  /**
   * Similar to variable integers, this reads a variable integer that is being pushed on the stack
   * in a signature script.
   *
   * @param data  Byte array containing the data to be pushed.
   * @param start Position of the integer.
   * @return Information about the value and size of the integer.
   */
  public static VariableInt readVariableStackInt(byte[] data, int start) {
    int checkSize = 0xFF & data[start];
    VariableInt varInt = new VariableInt();
    varInt.setSize(0);

    if (checkSize < 0x4C) {
      varInt.setSize(1);
      varInt.setValue(checkSize);
      return varInt;
    }

    if (checkSize == 0x4C) {
      varInt.setSize(2);
    } else if (checkSize == 0x4D) {
      varInt.setSize(3);
    } else if (checkSize == 0x4E) {
      varInt.setSize(5);
    } else {
      // Just process the byte and advance
      varInt.setSize(1);
      varInt.setValue(0);
      return varInt;
    }

    if (varInt.getSize() == 0) {
      return null;
    }

    byte[] newData = ByteUtilities.readBytes(data, start + 1, varInt.getSize() - 1);
    newData = ByteUtilities.flipEndian(newData);
    varInt.setValue(new BigInteger(1, newData).longValue());
    return varInt;
  }

  /**
   * Similar to variable integers, this reads a variable integer that is being pushed on the stack
   * in a redeem script.
   *
   * @param data  Byte array containing the data to be pushed.
   * @param start Position of the integer.
   * @return Information about the value and size of the integer, -1 if not a push OP.
   */
  public static VariableInt readOpCodeInt(byte[] data, int start) {
    int checkSize = 0xFF & data[start];
    VariableInt varInt = new VariableInt();
    varInt.setSize(0);

    if (checkSize == 0x00) {
      varInt.setSize(1);
      varInt.setValue(checkSize);
      return varInt;
    }

    if (checkSize >= 0x51 && checkSize <= 0x60) {
      varInt.setSize(1);
      varInt.setValue(checkSize - 0x50);
      return varInt;
    }

    if (checkSize == 0x4C) {
      varInt.setSize(2);
    } else if (checkSize == 0x4D) {
      varInt.setSize(3);
    } else if (checkSize == 0x4E) {
      varInt.setSize(5);
    } else {
      // Just process the byte and advance
      varInt.setSize(1);
      varInt.setValue(checkSize);
      return varInt;
    }

    if (varInt.getSize() == 0) {
      return null;
    }

    byte[] newData = ByteUtilities.readBytes(data, start + 1, varInt.getSize() - 1);
    newData = ByteUtilities.flipEndian(newData);
    varInt.setValue(new BigInteger(1, newData).longValue());
    return varInt;
  }

  /**
   * Encode an integer into a byte array with variable length encoding.
   *
   * @param data Integer data to encode.
   * @return Byte array representation.
   */
  public static byte[] writeVariableInt(long data) {
    byte[] newData;

    if (data < 0x00FD) {
      newData = new byte[1];
      newData[0] = (byte) (data & 0xFF);
    } else if (data <= 0xFFFF) {
      newData = new byte[3];
      newData[0] = (byte) 0xFD;
    } else if (data <= 4294967295L /* 0xFFFFFFFF */) {
      newData = new byte[5];
      newData[0] = (byte) 0xFE;
    } else {
      newData = new byte[9];
      newData[0] = (byte) 0xFF;
    }

    byte[] intData = BigInteger.valueOf(data).toByteArray();
    intData = ByteUtilities.stripLeadingNullBytes(intData);
    intData = ByteUtilities.leftPad(intData, newData.length - 1, (byte) 0x00);
    intData = ByteUtilities.flipEndian(intData);

    System.arraycopy(intData, 0, newData, 1, newData.length - 1);

    return newData;
  }

  /**
   * Encode a stack push integer into a byte array with variable length encoding.
   *
   * @param data Integer data to encode.
   * @return Byte array representation.
   */
  public static byte[] writeVariableStackInt(long data) {
    byte[] newData;

    if (data < 0x4C) {
      newData = new byte[1];
      newData[0] = (byte) (data & 0xFF);
    } else if (data <= 0xFF) {
      newData = new byte[2];
      newData[0] = (byte) 0x4C;
    } else if (data <= 0xFFFF) {
      newData = new byte[3];
      newData[0] = (byte) 0x4D;
    } else {
      newData = new byte[5];
      newData[0] = (byte) 0x4E;
    }

    byte[] intData = BigInteger.valueOf(data).toByteArray();
    intData = ByteUtilities.stripLeadingNullBytes(intData);
    intData = ByteUtilities.leftPad(intData, newData.length - 1, (byte) 0x00);
    intData = ByteUtilities.flipEndian(intData);

    System.arraycopy(intData, 0, newData, 1, newData.length - 1);

    return newData;
  }

  /**
   * Creates a copy of the current raw transaction.
   *
   * @return A copy of the current object.
   */
  public RawTransaction copy() {
    RawTransaction rawTx = new RawTransaction();

    rawTx.setVersion(getVersion());
    rawTx.setInputCount(getInputCount());
    for (RawInput input : getInputs()) {
      rawTx.getInputs().add(input.copy());
    }
    rawTx.setOutputCount(getOutputCount());
    for (RawOutput output : getOutputs()) {
      rawTx.getOutputs().add(output.copy());
    }
    rawTx.setLockTime(getLockTime());

    return rawTx;
  }

  /**
   * Strips the inputs of their scripts, preparing them for signing.
   *
   * @param tx Transaction to prepare for signing.
   * @return Transaction with no script attached to its inputs.
   */
  public static RawTransaction stripInputScripts(RawTransaction tx) {
    RawTransaction rawTx = tx.copy();

    for (RawInput input : rawTx.getInputs()) {
      input.setScript("");
      input.setScriptSize(0);
    }

    return rawTx;
  }

  /**
   * Parses non-standard signature scripts for signing.
   *
   * @param originalScript The script provided in the original output.
   * @return The altered script which will be used in signing.
   */
  public static String prepareSigScript(String originalScript) {
    String modifiedScript = "";
    int scriptPosition;
    int scriptSectionStart = 0;
    boolean foundCheckSig = false;
    byte[] scriptBytes = ByteUtilities.toByteArray(originalScript);
    for (scriptPosition = 0; scriptPosition < scriptBytes.length; scriptPosition++) {
      // Look for CHECKSIGs
      if ((scriptBytes[scriptPosition] & 0xFF) >= 0xAC
          && (scriptBytes[scriptPosition] & 0xFF) <= 0xAF && !foundCheckSig) {
        // Found one, backtrack to find the 0xAB, set it as the start position.
        foundCheckSig = true;
        for (int i = (scriptPosition - 1); i >= 0; i--) {
          if ((scriptBytes[scriptPosition] & 0xFF) == 0xAB) {
            scriptSectionStart = i + 1; // Get the one after the CODESEP, 0 if we don't find one.
            break;
          }
        }
      } else {
        // Check if the script contains stack arguments, skip them.
        if ((scriptBytes[scriptPosition] & 0xFF) >= 0x01
            && (scriptBytes[scriptPosition] & 0xFF) <= 0x4B) {
          // This byte is the size
          scriptPosition += scriptBytes[scriptPosition] & 0xFF;
        } else if ((scriptBytes[scriptPosition] & 0xFF) == 0x4C) {
          // Next byte is the size
          scriptPosition++;
          scriptPosition += scriptBytes[scriptPosition] & 0xFF;
        } else if ((scriptBytes[scriptPosition] & 0xFF) == 0x4D) {
          // Next 2 bytes are the size
          scriptPosition++;
          byte[] sizeBytes = ByteUtilities.readBytes(scriptBytes, scriptPosition, 2);
          sizeBytes = ByteUtilities.flipEndian(sizeBytes);
          int size = new BigInteger(1, sizeBytes).intValue();
          scriptPosition++;
          scriptPosition += size;
        } else if ((scriptBytes[scriptPosition] & 0xFF) == 0x4E) {
          // Next 4 bytes are the size
          scriptPosition++;
          byte[] sizeBytes = ByteUtilities.readBytes(scriptBytes, scriptPosition, 4);
          sizeBytes = ByteUtilities.flipEndian(sizeBytes);
          int size = new BigInteger(1, sizeBytes).intValue();
          scriptPosition += 3;
          scriptPosition += size;
        } else if ((scriptBytes[scriptPosition] & 0xFF) == 0xAB) {
          // If the CHECKSIG was found and we find any 0xAB's, remove them.
          if (scriptSectionStart <= scriptPosition) {
            // If start > position then we got two 0xAB's
            // in a row, skip the copy
            byte[] copyArray =
                Arrays.copyOfRange(scriptBytes, scriptSectionStart, scriptPosition - 1);
            modifiedScript += ByteUtilities.toHexString(copyArray);
          }
          scriptSectionStart = scriptPosition + 1;
        }
      }
    }
    return modifiedScript;
  }

  /**
   * Assuming standard scripts, return the address.
   *
   * @param script Standard pubkey script to be decoded.
   * @return The address that the redeem script corresponds to.
   */
  public static String decodePubKeyScript(String script) {
    if (script == null) {
      return null;
    }

    // Regular address
    Pattern pattern = Pattern.compile("^76a914(.{40})88ac$");
    Matcher matcher = pattern.matcher(script);
    if (matcher.matches()) {
      String addressBytes = matcher.group(1);

      String networkBytes =
          BitcoinResource.getResource().getBitcoindRpc().getblockchaininfo().getChain()
              == BlockChainName.main ? NetworkBytes.P2PKH.toString() :
              NetworkBytes.P2PKH_TEST.toString();

      return BitcoinTools.encodeAddress(addressBytes, networkBytes);
    }

    pattern = Pattern.compile("^a914(.{40})87$");
    matcher = pattern.matcher(script);
    if (matcher.matches()) {
      String addressBytes = matcher.group(1);

      String networkBytes =
          BitcoinResource.getResource().getBitcoindRpc().getblockchaininfo().getChain()
              == BlockChainName.main ? NetworkBytes.P2SH.toString() :
              NetworkBytes.P2SH_TEST.toString();

      return BitcoinTools.encodeAddress(addressBytes, networkBytes);
    }

    return null;
  }

  public static long getRedeemScriptKeyCount(String script) {
    byte[] scriptBytes = ByteUtilities.toByteArray(script);
    int buffPointer = 0;

    LOGGER.debug("Attempting to decode: " + script);
    if (script == null) {
      return 0L;
    }

    LinkedList<String> stack = new LinkedList<>();
    LinkedList<String> publicKeys = new LinkedList<>();
    VariableInt varInt;
    try {
      while ((varInt = readOpCodeInt(scriptBytes, buffPointer)) != null) {
        LOGGER.debug("VAL: " + varInt.getValue());
        if (readVariableStackInt(scriptBytes, buffPointer).getValue() == 0 && varInt.getSize() == 1
            && varInt.getValue() > 16) {
          LOGGER.debug("OPCODE: " + varInt.getValue());
          // We got an opcode.
          if (varInt.getValue() == 0xae) {
            LOGGER.debug("OP_CHECKMULTISIG");
            // OP_CHECKMULTISIG, process the stack.
            long numberKeys = Long.parseLong(stack.getLast());
            LOGGER.debug("NUMBER OF KEYS: " + numberKeys);
            stack.removeLast();

            if (numberKeys > stack.size()) {
              // Data's garabage, we won't even try to read it. Bail out.
              LOGGER.debug("Error reading script: " + stack.size());
              return 0L;
            }

            for (int i = 0; i < numberKeys; i++) {
              String pubKey = stack.getLast();
              stack.removeLast();

              byte[] keyBytes = ByteUtilities.toByteArray(pubKey);
              keyBytes = ByteUtilities.stripLeadingNullBytes(keyBytes);
              publicKeys.add(ByteUtilities.toHexString(keyBytes));
            }

            long requiredKeys = Long.parseLong(stack.getLast());
            LOGGER.debug("[Required Keys] " + requiredKeys);
            return requiredKeys;
          } else {
            // Non-standard script. Bail out.
            LOGGER.debug("Non-standard script, cannot process");
            return 0L;
          }
        } else {
          // Push it.
          LOGGER.debug("Pushing stack variable");
          if (readVariableStackInt(scriptBytes, buffPointer).getValue() == 0
              && varInt.getSize() == 1 && varInt.getValue() <= 16) {
            LOGGER.debug("Pushing: " + ((Long) varInt.getValue()).toString());
            stack.add(((Long) varInt.getValue()).toString());
            buffPointer += varInt.getSize();
          } else {
            buffPointer += varInt.getSize();
            LOGGER.debug("Pushing: " + ByteUtilities.toHexString(
                ByteUtilities.readBytes(scriptBytes, buffPointer, (int) varInt.getValue())));
            stack.add(ByteUtilities.toHexString(
                ByteUtilities.readBytes(scriptBytes, buffPointer, (int) varInt.getValue())));
            buffPointer += varInt.getValue();
          }
        }
      }
    } catch (Exception e) {
      LOGGER.debug("Bad script caused exception, cannot process", e);
      return 0L;
    }

    // We ran out of script without seeing what we expected. Bail out.
    return 0L;
  }

  /**
   * Assuming standard scripts, decodes a multi-sig redeem script into its public keys.
   */
  public static Iterable<String> decodeRedeemScript(String script) {
    byte[] scriptBytes = ByteUtilities.toByteArray(script);
    int buffPointer = 0;

    LOGGER.debug("Attempting to decode: " + script);
    if (script == null) {
      return new LinkedList<>();
    }

    LinkedList<String> stack = new LinkedList<>();
    LinkedList<String> publicKeys = new LinkedList<>();
    VariableInt varInt;
    try {
      while ((varInt = readOpCodeInt(scriptBytes, buffPointer)) != null) {
        LOGGER.debug("VAL: " + varInt.getValue());
        if (readVariableStackInt(scriptBytes, buffPointer).getValue() == 0 && varInt.getSize() == 1
            && varInt.getValue() > 16) {
          LOGGER.debug("OPCODE: " + varInt.getValue());
          // We got an opcode.
          if (varInt.getValue() == 0xae) {
            LOGGER.debug("OP_CHECKMULTISIG");
            long numberKeys = Long.parseLong(stack.getLast());
            LOGGER.debug("NUMBER OF KEYS: " + numberKeys);
            stack.removeLast();

            if (numberKeys > stack.size()) {
              // Data's garabage, we won't even try to read it. Bail out.
              LOGGER.debug("Error reading script: " + stack.size());
              return new LinkedList<>();
            }

            for (int i = 0; i < numberKeys; i++) {
              String pubKey = stack.getLast();
              stack.removeLast();

              byte[] keyBytes = ByteUtilities.toByteArray(pubKey);
              keyBytes = ByteUtilities.stripLeadingNullBytes(keyBytes);
              publicKeys.add(ByteUtilities.toHexString(keyBytes));
            }

            return publicKeys;
          } else {
            // Non-standard script. Bail out.
            LOGGER.debug("Non-standard script, cannot process");
            return new LinkedList<>();
          }
        } else {
          // Push it.
          LOGGER.debug("Pushing stack variable");
          if (readVariableStackInt(scriptBytes, buffPointer).getValue() == 0
              && varInt.getSize() == 1 && varInt.getValue() <= 16) {
            LOGGER.debug("Pushing: " + ((Long) varInt.getValue()).toString());
            stack.add(((Long) varInt.getValue()).toString());
            buffPointer += varInt.getSize();
          } else {
            buffPointer += varInt.getSize();
            LOGGER.debug("Pushing: " + ByteUtilities.toHexString(
                ByteUtilities.readBytes(scriptBytes, buffPointer, (int) varInt.getValue())));
            stack.add(ByteUtilities.toHexString(
                ByteUtilities.readBytes(scriptBytes, buffPointer, (int) varInt.getValue())));
            buffPointer += varInt.getValue();
          }
        }
      }
    } catch (Exception e) {
      LOGGER.debug("Bad script caused exception, cannot process", e);
      return new LinkedList<>();
    }

    // We ran out of script without seeing what we expected. Bail out.
    return new LinkedList<>();
  }

  public static void main(String[] args){
    byte[] result = writeVariableStackInt(2);
    System.out.println(ByteUtilities.toHexString(result));
  }
}
