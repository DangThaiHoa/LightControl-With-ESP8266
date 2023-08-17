package dangthaihoa.lightcontrol_with_esp8266;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button led1, led2, btnModeLed1, btnModeLed2;
    TextView textLed1,textLed2, date, time, errorDS1302;
    int sLed1, sLed2;
    ImageView imageLed1, imageLed2, configTimeLed1;
    String path = "LightControl/ESP8266/DATA/";
    DatabaseReference refDate, refTime, refErrorDS1302, refLed1, refLed2, refModeLed1;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorDS1302 = findViewById(R.id.textDS1302Error);
        date = findViewById(R.id.Date);
        time = findViewById(R.id.Time);
        textLed1 = findViewById(R.id.textLedBang);
        textLed2 = findViewById(R.id.textLedNha);
        imageLed1 = findViewById(R.id.imageLedBang);
        imageLed2 = findViewById(R.id.imageLedNha);
        led1 = findViewById(R.id.btnLedBang);
        led2 = findViewById(R.id.btnLedNha);
        configTimeLed1 = findViewById(R.id.config_led_bang);
        btnModeLed1 = findViewById(R.id.mode_led_bang);

        database = FirebaseDatabase.getInstance();
        refDate = database.getReference(path + "DS1302/date");
        refTime = database.getReference(path + "DS1302/time");
        refErrorDS1302 = database.getReference(path + "DS1302/error");
        refLed1 = database.getReference(path + "LED/ledBang");
        refLed2 = database.getReference(path + "LED/ledNha");
        refModeLed1 = database.getReference(path + "MODE/ledBang");

        refModeLed1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String mode = snapshot.getValue(String.class);
                if(mode.equals("Auto")){
                    btnModeLed1.setText(R.string.auto);
                }else{
                    btnModeLed1.setText(R.string.manual);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnModeLed1.setOnClickListener(view1 -> {
            if(btnModeLed1.getText().equals("Tự Động")){
                refModeLed1.setValue("Manual");
            }else{
                refModeLed1.setValue("Auto");
            }
        });

        ConfigTime();

        ReadDateTime();

        ControlLed();
    }

    private void ConfigTime() {

        configTimeLed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, denbang_config.class));
            }
        });

    }

    private void ReadDateTime(){

        refErrorDS1302.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String getError = dataSnapshot.getValue(String.class);
                assert getError != null;
                if(getError.equals("Read/Connect/Battery")){
                    errorDS1302.setVisibility(View.VISIBLE);
                    date.setText(R.string.is_null);
                    time.setText(R.string.is_null);
                }else{
                    errorDS1302.setVisibility(View.INVISIBLE);
                    refDate.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String getDate = dataSnapshot.getValue(String.class);
                            date.setText(getDate);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    refTime.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String getTime = dataSnapshot.getValue(String.class);
                            time.setText(getTime);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ControlLed() {

        refLed1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer statusLed1 = dataSnapshot.getValue(Integer.class);
                assert statusLed1 != null;
                if (statusLed1 == 1) {
                    textLed1.setText("Bật");
                    led1.setText("Tắt");
                    led1.setBackgroundColor(Color.parseColor("red"));
                    imageLed1.setImageResource(R.drawable.lamp_on);
                    sLed1 = 0;
                } else {
                    textLed1.setText("Tắt");
                    led1.setText("Bật");
                    led1.setBackgroundColor(Color.parseColor("green"));
                    imageLed1.setImageResource(R.drawable.lamp_off);
                    sLed1 = 1;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refLed2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer statusLed2 = dataSnapshot.getValue(Integer.class);
                assert statusLed2 != null;
                if (statusLed2 == 1) {
                    textLed2.setText("Bật");
                    led2.setText("Tắt");
                    led2.setBackgroundColor(Color.parseColor("red"));
                    imageLed2.setImageResource(R.drawable.lamp_on);
                    sLed2 = 0;
                } else {
                    textLed2.setText("Tắt");
                    led2.setText("Bật");
                    led2.setBackgroundColor(Color.parseColor("green"));
                    imageLed2.setImageResource(R.drawable.lamp_off);
                    sLed2 = 1;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        led1.setOnClickListener(view -> {
            refLed1.setValue(sLed1);
        });

        led2.setOnClickListener(view -> {
            refLed2.setValue(sLed2);
        });
    }
}