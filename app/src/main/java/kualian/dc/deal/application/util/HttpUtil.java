package kualian.dc.deal.application.util;

import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;


public class HttpUtil 
{

	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	/**
	 * get方法
	 * @param urlString
	 *            访问的url，必须以http开头
	 * @return 非法url传入，将返回null，其他返回页面源码
	 */
	public static String get(String urlString) {
		return HttpUtil.get(urlString, null);
	}

	/**
	 * get方法
	 * @param urlString
	 *            访问的url，必须以http开头
	 * @param encode
	 *            编码类型,如果为null,则不做编码处理
	 * @return 非法url传入，将返回null，其他返回页面源码
	 */
	public static String get(String urlString, String encode) {

		StringBuffer stringBuffer = new StringBuffer();


		if (urlString.equalsIgnoreCase("")) {
			return null;
		} else if (urlString.toLowerCase().startsWith("http://")
				|| urlString.toLowerCase().startsWith("https://")) {

		} else {
			return null;
		}

		HttpURLConnection httpConnection = null;
		URL url;
		int code;
		try {
			url = new URL(urlString);
			if (urlString.toLowerCase().startsWith("https://")) {
				setHttpsURLConnection();
			}
			httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Connection", "close");
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			OutputStreamWriter writer = new OutputStreamWriter(
					httpConnection.getOutputStream());
			writer.close();
			code = httpConnection.getResponseCode();
		} catch (Exception e) {
			return null;
		}

		if (code == HttpURLConnection.HTTP_OK) {
			BufferedReader reader = null;
			InputStreamReader inputStreamReader1 = null;
			InputStreamReader inputStreamReader2 = null;

			if (encode == null) {
				try {
					inputStreamReader1 = new InputStreamReader(
							httpConnection.getInputStream());
				} catch (IOException e) {
					if (inputStreamReader1 != null) {
						try {
							inputStreamReader1.close();
						} catch (IOException e1) {
						}
					}
				}
			} else {
				try {
					inputStreamReader2 = new InputStreamReader(
							httpConnection.getInputStream(), encode);
				} catch (IOException e) {
					if (inputStreamReader2 != null) {
						try {
							inputStreamReader2.close();
						} catch (IOException e2) {
						}
					}
				}
			}

			try {
				String strCurrentLine;
				if (encode == null) {
					reader = new BufferedReader(inputStreamReader1);
				} else {
					reader = new BufferedReader(inputStreamReader2);
				}
				while ((strCurrentLine = reader.readLine()) != null) {
					stringBuffer.append(strCurrentLine).append("\n");
				}
				if (stringBuffer.length() > 100) {
				} else {
				}
			} catch (IOException e) {
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (Exception e) {
					}
				}
				if (inputStreamReader1 != null) {
					try {
						inputStreamReader1.close();
					} catch (Exception e) {
					}
				}
				if (inputStreamReader2 != null) {
					try {
						inputStreamReader2.close();
					} catch (Exception e) {
					}
				}
			}
		}

		httpConnection.disconnect();
		return stringBuffer.toString();
	}

	/**
	 * post方法
	 *
	 * @param urlStr
	 *            访问的url
	 * @param parameters
	 *            post数据，格式param1=value1&param2=value2...
	 * @return 非法url传入，将返回null，其他返回页面源码
	 * @throws Exception 
	 */
	public static String post(String urlStr, String parameters) throws Exception {
		return HttpUtil.post(urlStr, parameters, "UTF-8");
	}

	public static String httpClientPost(String urlString, String parameters) {
		return null;
	}

	/**
	 * http和https统一方法
	 * @param urlStr
	 * @param parameters
	 * @param encode
	 * @return
	 * @throws Exception
	 */
	public static String post(String urlStr, String parameters, String encode) throws Exception {
		StringBuffer stringBuffer = new StringBuffer();

		HttpURLConnection httpConnection = null;
		OutputStreamWriter outputStreamWriter = null;
		URL url;
		int code;
		try {
			url = new URL(urlStr);
			if (urlStr.toLowerCase().startsWith("https://")) {
				setHttpsURLConnection();
			}
			httpConnection = (HttpURLConnection) url.openConnection();

			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Length",
					String.valueOf(parameters.length()));
			httpConnection.setRequestProperty("Content-Type",
					"application/json");
			httpConnection.setRequestProperty("Connection", "close");
			httpConnection.setConnectTimeout(10000);
			httpConnection.setDoOutput(true);
			httpConnection.setDoInput(true);
			//String namepasswd = wallet.getUsername()+":"+wallet.getPassword();
			//String authStr = new String(Base64.encode(namepasswd.getBytes(Charset.forName("ISO8859-1"))));
			//httpConnection.setRequestProperty("Authorization", "Basic " + authStr);

			outputStreamWriter = new OutputStreamWriter(
					httpConnection.getOutputStream(),encode);
			outputStreamWriter.write(parameters);
			outputStreamWriter.flush();

			code = httpConnection.getResponseCode();
		} catch (Exception e) {
			logger.error("http请求出错了",e);
			if (outputStreamWriter != null) {
				try {
					outputStreamWriter.close();
				} catch (IOException ea) {
				}
			}
			httpConnection.disconnect();
			throw new Exception(e);
		} finally {
			if (outputStreamWriter != null) {
				try {
					outputStreamWriter.close();
				} catch (IOException e) {
				}
			}
		}

		if (code == HttpURLConnection.HTTP_OK) {
			BufferedReader reader = null;
			InputStreamReader inputStreamReader1 = null;
			InputStreamReader inputStreamReader2 = null;
			if (encode == null) {
				try {
					inputStreamReader1 = new InputStreamReader(
							httpConnection.getInputStream());
				} catch (IOException e) {
					if (inputStreamReader1 != null) {
						try {
							inputStreamReader1.close();
						} catch (IOException e1) {
						}
					}
					httpConnection.disconnect();
				}
			} else {
				try {
					inputStreamReader2 = new InputStreamReader(
							httpConnection.getInputStream(), encode);
				} catch (IOException e) {
					if (inputStreamReader2 != null) {
						try {
							inputStreamReader2.close();
						} catch (IOException e2) {
						}
					}
					httpConnection.disconnect();
				}
			}
			try {
				String strCurrentLine;
				if (encode == null) {
					reader = new BufferedReader(inputStreamReader1);
				} else {
					reader = new BufferedReader(inputStreamReader2);
				}
				while ((strCurrentLine = reader.readLine()) != null) {
					//stringBuffer.append(strCurrentLine).append("\n");
					stringBuffer.append(strCurrentLine);
				}
				if (stringBuffer.length() > 100) {
				} else {
				}
			} catch (Exception e) {
				if (reader != null) {
					try {
						reader.close();
					} catch (Exception ed) {
					}
				}
				if (inputStreamReader1 != null) {
					try {
						inputStreamReader1.close();
					} catch (Exception ef) {
					}
				}
				if (inputStreamReader2 != null) {
					try {
						inputStreamReader2.close();
					} catch (Exception eg) {
					}
				}
				httpConnection.disconnect();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (Exception e) {
					}
				}
				if (inputStreamReader1 != null) {
					try {
						inputStreamReader1.close();
					} catch (Exception e) {
					}
				}
				if (inputStreamReader2 != null) {
					try {
						inputStreamReader2.close();
					} catch (Exception e) {
					}
				}
			}
		}
		httpConnection.disconnect();
		return stringBuffer.toString();
	}

	private static void setHttpsURLConnection() throws Exception {
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				return true;
			}
		};
		trustAllHttpsCertificates();
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}

	// post 调用
	//public static String post(String urlString, Map<String, String> paramsMap,
	//		String encode) throws Exception {
	//	return HttpUtil.post(urlString,
	//			HttpUtil.getEncodeParams(paramsMap, encode), encode);
	//}

	// get 调用
	public static String postDataToGet(Map<String, String> formValues,
			String gatewayUrl, String enocode)
			throws UnsupportedEncodingException {
		StringBuffer urlBuffer = new StringBuffer();
		if (gatewayUrl.indexOf("?") > 0) {
			if (gatewayUrl.endsWith("&")) {
				// 如果末尾跟着&,去掉
				urlBuffer.append(gatewayUrl.substring(0,
						gatewayUrl.length() - 1));
			} else {
				urlBuffer.append(gatewayUrl);
			}
		} else {
			urlBuffer.append(gatewayUrl);
			urlBuffer.append("?");
		}
		for (String key : formValues.keySet()) {
			if (!urlBuffer.toString().endsWith("?")) {
				// 不是以?结尾,则加上&
				urlBuffer.append("&");
			}
			urlBuffer.append(key);
			urlBuffer.append("=");
			if (formValues.get(key) != null) {
				urlBuffer
						.append(URLEncoder.encode(formValues.get(key), enocode));
			}
		}

		return urlBuffer.toString();
	}

	public static String getEncodeParams(Map<String, String> paramsMap,
			String enocode) {
		StringBuffer paramsBuffer = new StringBuffer();
		for (String key : paramsMap.keySet()) {
			if (!paramsBuffer.toString().equals("")) {
				// 不是以?结尾,则加上&
				paramsBuffer.append("&");
			}

			paramsBuffer.append(key);
			paramsBuffer.append("=");
			if (paramsMap.get(key) != null) {
				try {
					paramsBuffer.append(URLEncoder.encode(paramsMap.get(key),
							enocode));
				} catch (UnsupportedEncodingException e) {
					paramsBuffer.append(paramsMap.get(key));
				}
			}
		}
		return paramsBuffer.toString();
	}

	private static void trustAllHttpsCertificates() throws Exception {
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
		javax.net.ssl.TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext
				.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc
				.getSocketFactory());
	}

	public static class miTM implements javax.net.ssl.TrustManager,
			javax.net.ssl.X509TrustManager {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(
				java.security.cert.X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType)
				throws java.security.cert.CertificateException {
		}
	}

	/*public static void main(String[] args) throws Exception {
	}*/
}
