package se.chalmers.labyrinth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainMenu extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Skapa referenser till knapparna och koppla till listerner
        Button btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnNewGame.setOnClickListener(this);
    }
    
    public void onClick(View v) {
        switch(v.getId()) {
            // "New Game"-knappen
            case R.id.btnNewGame:
                Intent intent = new Intent(MainMenu.this, Game.class);
                startActivity(intent);
            break;
        }
    }
}