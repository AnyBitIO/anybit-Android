package com.bitcoin.sign.bitcoindrpc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Signature Hash Types.
 *
 * <p>OP_CHECKSIG extracts a non-stack argument from each signature it evaluates, allowing the
 * signer to decide which parts of the transaction to sign. Since the signature protects those parts
 * of the transaction from modification, this lets signers selectively choose to let other people
 * modify their transactions.
 *
 * <p>The various options for what to sign are called signature hash types. There are three base
 * SIGHASH types currently available:
 *
 * <p>SIGHASH_ALL, the default, signs all the inputs and outputs, protecting everything except the
 * signature scripts against modification. SIGHASH_NONE signs all of the inputs but none of the
 * outputs, allowing anyone to change where the satoshis are going unless other signatures using
 * other signature hash flags protect the outputs. SIGHASH_SINGLE signs only this input and only one
 * corresponding output (the output with the same output index number as the input), ensuring nobody
 * can change your part of the transaction but allowing other signers to change their part of the
 * transaction. The corresponding output must exist or the value "1" will be signed, breaking the
 * security scheme.
 *
 * <p>The base types can be modified with the SIGHASH_ANYONECANPAY (anyone can pay) flag, creating
 * three new combined types: SIGHASH_ALL|SIGHASH_ANYONECANPAY signs all of the outputs but only this
 * one input, and it also allows anyone to add or remove other inputs, so anyone can contribute
 * additional satoshis but they cannot change how many satoshis are sent nor where they go.
 * SIGHASH_NONE|SIGHASH_ANYONECANPAY signs only this one input and allows anyone to add or remove
 * other inputs or outputs, so anyone who gets a copy of this input can spend it however they’d
 * like. SIGHASH_SINGLE|SIGHASH_ANYONECANPAY signs only this input and only one corresponding
 * output, but it also allows anyone to add or remove other inputs. Because each input is signed, a
 * transaction with multiple inputs can have multiple signature hash types signing different parts
 * of the transaction. For example, a single-input transaction signed with NONE could have its
 * output changed by the miner who adds it to the block chain. On the other hand, if a two-input
 * transaction has one input signed with NONE and one input signed with ALL, the ALL signer can
 * choose where to spend the satoshis without consulting the NONE signer—but nobody else can modify
 * the transaction.
 */
public enum SigHash {
  /**
   * the default, signs all the inputs and outputs, protecting everything except the signature
   * scripts against modification.
   */
  ALL, /**
   * signs all of the inputs but none of the outputs, allowing anyone to change where the satoshis
   * are going unless other signatures using other signature hash flags protect the outputs.
   */
  NONE, /**
   * signs only this input and only one corresponding output (the output with the same output index
   * number as the input), ensuring nobody can change your part of the transaction but allowing
   * other signers to change their part of the transaction. The corresponding output must exist or
   * the value "1" will be signed, breaking the security scheme.
   */
  SINGLE, /**
   * signs all of the outputs but only this one input, and it also allows anyone to add or remove
   * other inputs, so anyone can contribute additional satoshis but they cannot change how many
   * satoshis are sent nor where they go.
   */
  ALL_ANYONECANPAY, /**
   * signs only this one input and allows anyone to add or remove other inputs or outputs, so anyone
   * who gets a copy of this input can spend it however they’d like.
   */
  NONE_ANYONECANPAY, /**
   * signs only this input and only one corresponding output, but it also allows anyone to add or
   * remove other inputs.
   */
  SINGLE_ANYONECANPAY;

  @JsonValue
  public String toJson() {
    return name().replace("_", "|");
  }

  @JsonCreator
  public static SigHash fromJson(@JsonProperty("") String value) {
    return valueOf(value.replace("|", "_"));
  }
}
