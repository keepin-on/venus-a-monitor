package com.marstek.venusmonitor;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import com.getcapacitor.BridgeActivity;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class MainActivity extends BridgeActivity {

    public class UdpBridge {
        @JavascriptInterface
        public String query(String ip, int port, String payloadJson, int timeoutMs) {
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket();
                socket.setSoTimeout(timeoutMs > 0 ? timeoutMs : 2000);
                InetAddress addr = InetAddress.getByName(ip);
                byte[] sendBytes = payloadJson.getBytes(StandardCharsets.UTF_8);
                DatagramPacket sendPacket = new DatagramPacket(sendBytes, sendBytes.length, addr, port);
                socket.send(sendPacket);

                byte[] recvBuf = new byte[4096];
                DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(recvPacket);
                return new String(recvPacket.getData(), 0, recvPacket.getLength(), StandardCharsets.UTF_8);
            } catch (Exception e) {
                return "{\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}";
            } finally {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.bridge.getWebView().post(() -> {
            WebView wv = this.bridge.getWebView();
            wv.addJavascriptInterface(new UdpBridge(), "VenusUdp");
        });
    }
}
