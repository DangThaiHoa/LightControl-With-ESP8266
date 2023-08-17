package dangthaihoa.lightcontrol_with_esp8266;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class denbang_config extends AppCompatActivity {

    TimePicker timeOn, timeOff;
    Button btn_confirm;
    String path = "LightControl/ESP8266/DATA/";
    DatabaseReference refTimeOff, refTimeOn;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denbang_config);

        timeOn = findViewById(R.id.timepicker_led_bang_on);
        timeOff = findViewById(R.id.timepicker_led_bang_off);
        btn_confirm = findViewById(R.id.confirm_button_bang);

        timeOn.setIs24HourView(true);
        timeOff.setIs24HourView(true);

        database = FirebaseDatabase.getInstance();
        refTimeOff = database.getReference(path + "TIME/OFF");
        refTimeOn = database.getReference(path + "TIME/ON");

        GetTime();
        SetTime();
    }

    private void SetTime() {

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refTimeOff.child("Hour").setValue(timeOff.getHour());
                refTimeOff.child("Minute").setValue(timeOff.getMinute());
                refTimeOn.child("Hour").setValue(timeOn.getHour());
                refTimeOn.child("Minute").setValue(timeOn.getMinute());

                startActivity(new Intent(denbang_config.this,MainActivity.class));
            }
        });

    }

    private void GetTime() {

        refTimeOff.child("Hour").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int hour = snapshot.getValue(Integer.class);
                timeOff.setHour(hour);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refTimeOff.child("Minute").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int minute = snapshot.getValue(Integer.class);
                timeOff.setMinute(minute);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refTimeOn.child("Hour").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int hour = snapshot.getValue(Integer.class);
                timeOn.setHour(hour);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refTimeOn.child("Minute").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int minute = snapshot.getValue(Integer.class);
                timeOn.setMinute(minute);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}