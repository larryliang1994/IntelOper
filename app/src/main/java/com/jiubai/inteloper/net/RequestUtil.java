package com.jiubai.inteloper.net;

import android.util.Log;

import com.jiubai.inteloper.common.DataTypeConverter;
import com.jiubai.inteloper.config.Config;
import com.jiubai.inteloper.config.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by larry on 28/06/2017.
 */

public class RequestUtil {
    public static boolean keepListen = false;

    public static void request(final byte[] input, final int responseCode, final int msgLength,
                               final boolean longResponse, final RequestCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress netAddress = InetAddress.getByName(Constants.SERVER_URL);
                    Socket socket = new Socket(netAddress, Constants.PORT);
                    socket.setSoTimeout(Constants.REQUEST_TIMEOUT);

                    Charset cs = Charset.forName("GBK");
                    byte[] userName = (Config.UserName + "\0").getBytes(cs);
                    byte[] userName_offset = new byte[20 - userName.length];
                    byte[] new_input = DataTypeConverter.concatAll(userName, userName_offset, input);

                    // 发送数据
                    DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    outputStream.write(new_input);
                    outputStream.flush();

                    // 接收数据
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                    byte[] totalMsgContent = null;
                    int totalMsgNum = 0;
                    boolean empty = true;

                    while (true) {
                        byte[] opCodeBytes = new byte[4]; // 操作码字节
                        inputStream.readFully(opCodeBytes);

                        // 如果出现了多余字节，就往后读一位，丢弃第0位
                        while(DataTypeConverter.byte2int(opCodeBytes) != 3
                                && DataTypeConverter.byte2int(opCodeBytes) != responseCode) {
                            byte[] offset = new byte[1];

                            inputStream.readFully(offset);

                            opCodeBytes[0] = opCodeBytes[1];
                            opCodeBytes[1] = opCodeBytes[2];
                            opCodeBytes[2] = opCodeBytes[3];
                            opCodeBytes[3] = offset[0];
                        }

                        byte[] msgNumBytes = new byte[4]; // 消息数字节
                        inputStream.readFully(msgNumBytes);

                        int opCode = DataTypeConverter.byte2int(opCodeBytes);
                        int msgNum = DataTypeConverter.byte2int(msgNumBytes);

                        // 如果收到实时告警数据，需要跳过并等待所需数据
                        if (opCode == 3) {
                            if (msgNum != -1) {
                                byte[] temp = new byte[msgNum * (4 + 20 + 500)];
                                inputStream.readFully(temp);
                            }
                        } else if (opCode == responseCode) {
                            // -1表示数据结束
                            if (msgNum == -1) {

                                Log.i(Constants.TAG, "msgNum = " + msgNum);

                                outputStream.close();
                                inputStream.close();
                                socket.close();

                                break;
                            }

                            if (msgNum < -1) {
                                Log.i(Constants.TAG, "msgNum = " + msgNum);

                                if (msgNum == -99) {
                                    callback.error("数据查询失败", new Exception());
                                } else {
                                    callback.success(msgNum, null);
                                }

                                outputStream.close();
                                inputStream.close();
                                socket.close();

                                return;
                            }

                            byte[] msgContent = new byte[msgNum * msgLength];
                            inputStream.readFully(msgContent);

                            if (empty) {
                                totalMsgContent = DataTypeConverter.copyAll(msgContent);
                                empty = false;
                            } else {
                                totalMsgContent = DataTypeConverter.concatAll(totalMsgContent, msgContent);
                            }

                            totalMsgNum += msgNum;

                            if (!longResponse) {
                                outputStream.close();
                                inputStream.close();
                                socket.close();

                                break;
                            }
                        }
                    }

                    Log.i(Constants.TAG, "msgNum = " + totalMsgNum);

                    callback.success(totalMsgNum, totalMsgContent);
                } catch (IOException e) {
                    callback.error("error", e);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void listen(final int requestCode, final int msgLength,
                              final RequestCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress netAddress = InetAddress.getByName(Constants.SERVER_URL);
                    Socket socket = new Socket(netAddress, Constants.PORT);
                    socket.setKeepAlive(true);
                    socket.setSoTimeout(Constants.LISTEN_TIMEOUT);

                    keepListen = true;

                    // 接收数据
                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());

                    while (keepListen) {
                        byte[] opCodeBytes = new byte[4]; // 操作码字节
                        inputStream.readFully(opCodeBytes);

                        // 如果出现了多余字节，就往后读一位，丢弃第0位
                        while(DataTypeConverter.byte2int(opCodeBytes) != 3) {
                            byte[] offset = new byte[1];

                            inputStream.readFully(offset);

                            opCodeBytes[0] = opCodeBytes[1];
                            opCodeBytes[1] = opCodeBytes[2];
                            opCodeBytes[2] = opCodeBytes[3];
                            opCodeBytes[3] = offset[0];
                        }

                        byte[] msgNumBytes = new byte[4]; // 消息数字节
                        inputStream.readFully(msgNumBytes);

                        int opCode = DataTypeConverter.byte2int(opCodeBytes);
                        int msgNum = DataTypeConverter.byte2int(msgNumBytes);

                        byte[] msgContent = new byte[msgNum * msgLength];
                        inputStream.readFully(msgContent);

                        callback.success(msgNum, msgContent);
                    }
                } catch (IOException e) {
                    callback.error("error", e);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface RequestCallback {
        void success(int msgNum, byte[] msgContent);
        void error(String info, Exception exception);
    }
}
