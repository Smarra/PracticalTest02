package com.example.practicaltest02;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket == null) {
            Log.e("TAG", "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e("TAG", "[COMMUNICATION THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            Log.i("TAG", "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type!");
//            String city = bufferedReader.readLine();
//            String informationType = bufferedReader.readLine();
            HashMap<String, BitcoinModel> data = serverThread.getData();
            BitcoinModel weatherForecastInformation = null;
                Log.i("TAG", "[COMMUNICATION THREAD] Getting the information from the webservice...");
                HttpClient httpClient = new DefaultHttpClient();
                String pageSourceCode = "";
//                if(false) {
//                    HttpPost httpPost = new HttpPost(Constants.WEB_SERVICE_ADDRESS);
//                    List<NameValuePair> params = new ArrayList<>();
//                    params.add(new BasicNameValuePair("q", city));
//                    params.add(new BasicNameValuePair("mode", Constants.WEB_SERVICE_MODE));
//                    params.add(new BasicNameValuePair("APPID", Constants.WEB_SERVICE_API_KEY));
//                    UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
//                    httpPost.setEntity(urlEncodedFormEntity);
//                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
//
//                    pageSourceCode = httpClient.execute(httpPost, responseHandler);
//                } else {
//                    HttpGet httpGet = new HttpGet(Constants.WEB_SERVICE_ADDRESS + "?q=" + city + "&APPID=" + Constants.WEB_SERVICE_API_KEY + "&units=" + Constants.UNITS);
                    HttpGet httpGet = new HttpGet("https://api.coindesk.com/v1/bpi/currentprice/EUR.json");
                    HttpResponse httpGetResponse = httpClient.execute(httpGet);
                    HttpEntity httpGetEntity = httpGetResponse.getEntity();
                    if (httpGetEntity != null) {
                        pageSourceCode = EntityUtils.toString(httpGetEntity);

                    }
//                }

                if (pageSourceCode == null) {
                    Log.e("TAG", "[COMMUNICATION THREAD] Error getting the information from the webservice!");
                    return;
                } else
                    Log.i("TAG", pageSourceCode );

                // Updated for openweather API
//                if (false) {
//                    Document document = Jsoup.parse(pageSourceCode);
//                    Element element = document.child(0);
//                    Elements elements = element.getElementsByTag(Constants.SCRIPT_TAG);
//                    for (Element script : elements) {
//                        String scriptData = script.data();
//                        if (scriptData.contains(Constants.SEARCH_KEY)) {
//                            int position = scriptData.indexOf(Constants.SEARCH_KEY) + Constants.SEARCH_KEY.length();
//                            scriptData = scriptData.substring(position);
//                            JSONObject content = new JSONObject(scriptData);
//                            JSONObject currentObservation = content.getJSONObject(Constants.CURRENT_OBSERVATION);
//                            String value = currentObservation.getString(Constants.VALUE);
//                            weatherForecastInformation = new BitcoinModel(
//                                    value
//                            );
//                            serverThread.setData(city, weatherForecastInformation);
//                            break;
//                        }
//                    }
//                } else {
                    JSONObject content = new JSONObject(pageSourceCode);

//                    JSONArray weatherArray = content.getJSONArray(Constants.WEATHER);
                    JSONObject weather;
                    String condition = "";
//                    for (int i = 0; i < weatherArray.length(); i++) {
//                        weather = weatherArray.getJSONObject(i);
//                        Log.i("TAG", pageSourceCode );

//                        condition += weather.getString(Constants.MAIN) + " : " + weather.getString(Constants.DESCRIPTION);
//
//                        if (i < weatherArray.length() - 1) {
//                            condition += ";";
//                        }
//                    }

//                    JSONObject main = content.getJSONObject(Constants.MAIN);
//                    String temperature = main.getString(Constants.TEMP);

//                    JSONObject wind = content.getJSONObject(Constants.WIND);
//                    String windSpeed = wind.getString(Constants.SPEED);
//
//                    weatherForecastInformation = new BitcoinModel(
//                            value
//                    );
//                    serverThread.setData(city, weatherForecastInformation);
//                }
//            if (weatherForecastInformation == null) {
//                Log.e("TAG", "[COMMUNICATION THREAD] Weather Forecast Information is null!");
//                return;
//            }
//            String result = null;
//            switch(informationType) {
//                case Constants.ALL:
//                    result = weatherForecastInformation.toString();
//                    break;
//                default:
//                    result = "[COMMUNICATION THREAD] Wrong information type (all / temperature / wind_speed / condition / humidity / pressure)!";
//            }
            printWriter.println("Result");
            printWriter.flush();
        } catch (IOException ioException) {
            Log.e("TAG", "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();
        } catch (JSONException jsonException) {
            Log.e("TAG", "[COMMUNICATION THREAD] An exception has occurred: " + jsonException.getMessage());
            jsonException.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e("TAG", "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                    ioException.printStackTrace();
                }
            }
        }
    }

}
