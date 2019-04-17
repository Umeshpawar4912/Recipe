package pawar.umesh.cookr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
        Button login,register,guest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        login = (Button) findViewById ( R.id.button );
        register = (Button) findViewById ( R.id.button1 );
        guest = (Button) findViewById ( R.id.button2 );


        login.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent ( MainActivity.this ,Loginwindow.class );
                startActivity ( intent );
            }
        } );

        register.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent ( MainActivity.this ,Registretion.class );
                startActivity ( intent );
            }
        } );

        guest.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent ( MainActivity.this ,GuestRecipeList.class );
                startActivity ( intent );
            }
        } );
    }
}
