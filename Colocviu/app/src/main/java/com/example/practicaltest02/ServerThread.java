package com.example.practicaltest02;

import android.provider.SyncStateContract;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread {
    private int port = 0;
    private ServerSocket serverSocket = null;
    private HashMap<String, BitcoinModel> data = null;

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public ServerThread(int port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ioException) {
            Log.e("TAG", "An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();
        }

        this.data = new HashMap<>();
    }

    public synchronized void setData(String city, BitcoinModel weatherForecastInformation) {
        this.data.put(city, weatherForecastInformation);
    }

    public synchronized HashMap<String, BitcoinModel> getData() {
        return data;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Log.i("TAG", "[SERVER THREAD] Waiting for a client invocation...");
                Socket socket = serverSocket.accept();
                Log.i("TAG", "[SERVER THREAD] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                communicationThread.start();
            }
        } catch (IOException ioException) {
            Log.e("TAG", "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
            ioException.printStackTrace();
        }
    }
}
