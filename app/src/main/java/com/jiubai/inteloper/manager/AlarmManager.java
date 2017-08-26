package com.jiubai.inteloper.manager;

import android.os.Handler;
import android.os.Message;

import com.badoo.mobile.util.WeakHandler;
import com.jiubai.inteloper.bean.Alarm;
import com.jiubai.inteloper.common.DataTypeConverter;
import com.jiubai.inteloper.config.Constants;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by larry on 20/08/2017.
 *
 * 监听线程，连接socket，持续监听实时告警，然后更新alarms
 * 守护线程，每N秒检测监听线程是否存活
 */

public class AlarmManager {
    private static int GUARD_THREAD_INTERVAL = 5000;

    private static AlarmManager instance;

    public static ArrayList<Alarm> alarms;

    private Thread listenThread;
    private WeakHandler guardThread;
    private boolean keepListen = true;

    private Socket socket;
    private DataInputStream inputStream;

    private AlarmManager() {}

    public static AlarmManager getInstance() {
        if (instance == null) {
            instance = new AlarmManager();
            alarms = new ArrayList<>();
        }

        return instance;
    }

    public void startListen() {
        initListenThread();

        initGuardThread();

        keepListen = true;
        listenThread.start();
        guardThread.sendEmptyMessage(0);
    }

    public void stopListen() {
        keepListen = false;
        guardThread.sendEmptyMessage(1);
    }

    private void initListenThread() {
        listenThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress netAddress = InetAddress.getByName(Constants.SERVER_URL);
                    socket = new Socket(netAddress, Constants.PORT);
                    socket.setKeepAlive(true);
                    socket.setSoTimeout(Constants.LISTEN_TIMEOUT);

                    // 接收数据
                    inputStream = new DataInputStream(socket.getInputStream());

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

                        decodeAlarm(opCode, msgNum);

                        if (guardThread == null) {
                            initGuardThread();

                            guardThread.sendEmptyMessage(0);
                        }
                    }

                    listenThread = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initGuardThread() {
        guardThread = new WeakHandler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if (message.what == 0) {
                    if (listenThread == null || !listenThread.isAlive()) {
                        initListenThread();
                        listenThread.start();
                    }

                    guardThread.sendEmptyMessageDelayed(0, GUARD_THREAD_INTERVAL);
                } else if (message.what == 1) {
                    guardThread.removeMessages(0);
                    guardThread = null;
                }

                return false;
            }
        });
    }

    private void decodeAlarm(int opCode, int msgNum) throws IOException {
        byte[] status_b;
        byte[] occur_time;
        byte[] warn_str;

        for (int i = 0; i < msgNum; i++) {

            status_b = new byte[4];
            occur_time = new byte[20];
            warn_str = new byte[500];

            inputStream.readFully(status_b);
            inputStream.readFully(occur_time);
            inputStream.readFully(warn_str);

            int status = DataTypeConverter.byte2int(status_b);
            String occurTime = new String(occur_time, "GBK");
            String warnStr = new String(warn_str, "GBK");

            Alarm alarm = findAlarm(warnStr);

            if (alarm == null) {
                // 新增
                alarms.add(new Alarm(status, occurTime, warnStr));
            } else {
                // 原来不正常，现在正常，删掉
                if (alarm.getStatus() != Alarm.STATUS_NORMAL && status == Alarm.STATUS_NORMAL) {
                    deleteAlarm(warnStr);
                }
                // 原来正常，现在不正常，修改
                else if (alarm.getStatus() == Alarm.STATUS_NORMAL && status != Alarm.STATUS_NORMAL) {
                    editAlarm(status, occurTime, warnStr);
                }
                // 原来不正常，现在不正常，修改
                else if (alarm.getStatus() != Alarm.STATUS_NORMAL && status != Alarm.STATUS_NORMAL) {
                    editAlarm(status, occurTime, warnStr);
                }
                // 原来正常，现在正常，删掉
                else if (alarm.getStatus() == Alarm.STATUS_NORMAL && status == Alarm.STATUS_NORMAL) {
                    deleteAlarm(warnStr);
                }
            }
        }

    }

    private Alarm findAlarm(String warnStr) {
        for (Alarm alarm: alarms) {
            if (alarm.getWarnStr().equals(warnStr)) {
                return alarm;
            }
        }

        return null;
    }

    private void deleteAlarm(String warnStr) {
        for (int i = 0; i < alarms.size(); i++) {
            if (alarms.get(i).getWarnStr().equals(warnStr)) {
                alarms.remove(i);
                return;
            }
        }
    }

    private void editAlarm(int status, String occurTime, String warnStr) {
        for (int i = 0; i < alarms.size(); i++) {
            if (alarms.get(i).getWarnStr().equals(warnStr)) {
                alarms.get(i).setStatus(status);
                alarms.get(i).setOccurTime(occurTime);
                alarms.get(i).setWarnStr(warnStr);
                return;
            }
        }
    }

    public int getAbnormalNum() {
        int abnormalNum = 0;

        for (Alarm alarm: alarms) {
            if (alarm.getStatus() != Alarm.STATUS_NORMAL) {
                abnormalNum++;
            }
        }

        return abnormalNum;
    }
}
