package com.example.jegerima.SIDWeb;

/**
 * Created by Jegerima on 26/01/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.widget.Toast;

/**
 * Created by Cesar on 25/01/2015.
 */
public class Accelerometer {


    //Para calcular movimiento del acelerometro
    private long last_update , last_movement ;
    private float prevX , prevY, prevZ;
    private float curX , curY , curZ;
    private long ultimo;

    public Accelerometer(){
        last_update = 0;
        last_movement = 0;
        prevX = 0;
        prevY = 0;
        prevZ = 0;
        curX = 0;
        curY = 0;
        curZ = 0;
    }

    public boolean update(Context context, SensorEvent event){


        synchronized (this) {
            long current_time = event.timestamp;

            curX = event.values[0];
            curY = event.values[1];
            curZ = event.values[2];

            if (prevX == 0 && prevY == 0 && prevZ == 0) {
                last_update = current_time;
                last_movement = current_time;
                prevX = curX;
                prevY = curY;
                prevZ = curZ;
            }

            long time_difference = current_time - last_update;
            if (time_difference > 0) {
                float movement = Math.abs((curX + curY + curZ) - (prevX - prevY - prevZ)) / time_difference;
                int limit = 1111111111;
                float min_movement = 2.1E-6f;
                if (movement > min_movement) {
                    if ((current_time - last_movement) >= limit && (current_time-ultimo)>limit) {
                        //Toast.makeText(context, "Hay movimiento de " + movement + "  " + (current_time - last_movement), Toast.LENGTH_SHORT).show();
                        ultimo=current_time;
                        return true;
                    }
                    last_movement = current_time;
                }
                prevX = curX;
                prevY = curY;
                prevZ = curZ;
                last_update = current_time;
            }

        }
        return false;
    }

}