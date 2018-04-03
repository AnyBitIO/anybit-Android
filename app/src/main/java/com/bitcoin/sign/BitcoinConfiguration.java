package com.bitcoin.sign;

import com.bitcoin.api.currency.CurrencyConfiguration;
import com.bitcoin.api.currency.SigningType;
import com.bitcoin.api.validation.ValidatorConfiguration;
import com.bitcoin.common.EnvironmentVariableParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Properties;

public class BitcoinConfiguration implements CurrencyConfiguration, ValidatorConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(BitcoinConfiguration.class);
    private String daemonConnectionString = "http://127.0.0.1:18332";
    private int minConfirmations = 6;
    private int maxConfirmations = 9999999;
    private int minSignatures = 1;
    private int satoshiPerByteFee = 50000;
    private String[] multiSigKeys = {};
    private String[] multiSigAccounts = {};
    private int maxDeterministicAddresses = 100;
    private int rescanTimer = 60;
    private String daemonUser;
    private String daemonPassword;
    private BigDecimal maxAmountPerHour = BigDecimal.ZERO;
    private BigDecimal maxAmountPerDay = BigDecimal.ZERO;
    private BigDecimal maxAmountPerTransaction = BigDecimal.ZERO;
    private int maxNodeConnections = 10;

    private String serverPrivateKey = "b0837faed56bc7c48dc29d564b1c030f03eee53b0317c53d784c8f40654821c6";

    private boolean configLoaded = false;

    public BitcoinConfiguration() {
        //loadConfig();
    }

    private static int getIntProp(Properties prop, String value, int defaultValue) {
        try {
            return Integer.parseInt(EnvironmentVariableParser.resolveEnvVars(prop.getProperty(value)));
        } catch (Exception e) {
            LOGGER.warn(null, e);
            return defaultValue;
        }
    }

    private synchronized void loadConfig() {
        if (!configLoaded) {
            FileInputStream propertiesFile = null;
            String propertiesFilePath = "./cosigner-bitcoin.properties";
            try {
                Properties cosignerProperties = new Properties();
                propertiesFile = new FileInputStream(propertiesFilePath);

                cosignerProperties.load(propertiesFile);
                propertiesFile.close();

                // daemonConnectionString
                daemonConnectionString = EnvironmentVariableParser.resolveEnvVars(
                        cosignerProperties.getProperty("daemonConnectionString", daemonConnectionString));

                // maxNodeConnections
                maxNodeConnections =
                        getIntProp(cosignerProperties, "maxNodeConnections", maxNodeConnections);

                // minConfirmations
                minConfirmations = getIntProp(cosignerProperties, "minConfirmations", minConfirmations);

                // minSignatures
                minSignatures = getIntProp(cosignerProperties, "minSignatures", minSignatures);

                // maxConfirmations
                maxConfirmations = getIntProp(cosignerProperties, "maxConfirmations", maxConfirmations);

                // satoshiPerByteFee
                satoshiPerByteFee = getIntProp(cosignerProperties, "satoshiPerByteFee", satoshiPerByteFee);

                // multiSigKeys
                String arrayParser = EnvironmentVariableParser
                        .resolveEnvVars(cosignerProperties.getProperty("multiSigKeys"));
                if (arrayParser != null) {
                    multiSigKeys = arrayParser.split("[|]");
                }

                // multiSigAccounts
                arrayParser = EnvironmentVariableParser
                        .resolveEnvVars(cosignerProperties.getProperty("multiSigAccounts"));
                if (arrayParser != null) {
                    multiSigAccounts = arrayParser.split("[|]");
                }

                // maxDeterministicAddresses
                maxDeterministicAddresses =
                        getIntProp(cosignerProperties, "maxDeterministicAddresses", maxDeterministicAddresses);

                // rescanTimer
                rescanTimer = getIntProp(cosignerProperties, "rescanTimer", rescanTimer);

                // daemonUser
                daemonUser = EnvironmentVariableParser
                        .resolveEnvVars(cosignerProperties.getProperty("daemonUser", ""));

                // daemonPassword
                daemonPassword = EnvironmentVariableParser
                        .resolveEnvVars(cosignerProperties.getProperty("daemonPassword", ""));

                // maxAmountPerHour
                maxAmountPerHour = new BigDecimal(EnvironmentVariableParser.resolveEnvVars(
                        cosignerProperties.getProperty("maxAmountPerHour", maxAmountPerHour.toPlainString())));

                // maxAmountPerDay
                maxAmountPerDay = new BigDecimal(EnvironmentVariableParser.resolveEnvVars(
                        cosignerProperties.getProperty("maxAmountPerDay", maxAmountPerDay.toPlainString())));

                // maxAmountPerTransaction
                maxAmountPerTransaction = new BigDecimal(EnvironmentVariableParser.resolveEnvVars(
                        cosignerProperties
                                .getProperty("maxAmountPerTransaction", maxAmountPerTransaction.toPlainString())));

                // serverPrivateKey
                serverPrivateKey = EnvironmentVariableParser
                        .resolveEnvVars(cosignerProperties.getProperty("serverPrivateKey", serverPrivateKey));
                LOGGER.info("cosigner-bitcoin configuration loaded.");
            } catch (IOException e) {
                if (propertiesFile != null) {
                    try {
                        propertiesFile.close();
                    } catch (IOException e1) {
                        LOGGER.warn(null, e1);
                    }
                }
                LOGGER.info("Could not load cosigner-bitcoin configuration from " + propertiesFilePath
                        + ", using defaults.");
            }
            configLoaded = true;
        }
    }

    @Override
    public String getCurrencySymbol() {
        return "BTC";
    }

    @Override
    public SigningType getSigningType() {
        return SigningType.SENDALL;
    }

    public int getMinConfirmations() {
        return minConfirmations;
    }

    public int getMaxConfirmations() {
        return maxConfirmations;
    }

    public String getDaemonConnectionString() {
        return daemonConnectionString;
    }

    public int getMaxNodeConnections() {
        return maxNodeConnections;
    }

    public int getMinSignatures() {
        return minSignatures;
    }

    public int getSatoshiPerByteFee() {
        return satoshiPerByteFee;
    }

    @Override
    public boolean hasMultipleSenders() {
        return true;
    }

    @Override
    public boolean hasMultipleRecipients() {
        return true;
    }

    /**
     * Returns the multi-sig keys from the configuration.
     */
    public String[] getMultiSigKeys() {
        String[] retArray = new String[multiSigKeys.length];
        System.arraycopy(multiSigKeys, 0, retArray, 0, multiSigKeys.length);
        return retArray;
    }

    /**
     * Returns addresses that will be appended to list of signers used in multi-sig address.
     *
     * @return Array of addresses.
     */
    public String[] getMultiSigAccounts() {
        String[] retArray = new String[multiSigAccounts.length];
        System.arraycopy(multiSigAccounts, 0, retArray, 0, multiSigAccounts.length);
        return retArray;
    }

    public String getServerPrivateKey() {
        return serverPrivateKey;
    }

    public String getDaemonUser() {
        return daemonUser;
    }

    public String getDaemonPassword() {
        return daemonPassword;
    }

    public int getMaxDeterministicAddresses() {
        return maxDeterministicAddresses;
    }

    @Override
    public BigDecimal getMaxAmountPerHour() {
        return maxAmountPerHour;
    }

    @Override
    public BigDecimal getMaxAmountPerDay() {
        return maxAmountPerDay;
    }

    @Override
    public BigDecimal getMaxAmountPerTransaction() {
        return maxAmountPerTransaction;
    }

    public int getRescanTimer() {
        return rescanTimer;
    }
}
