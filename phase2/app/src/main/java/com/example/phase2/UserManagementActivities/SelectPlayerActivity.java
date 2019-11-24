package com.example.phase2.UserManagementActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phase2.Initializable;
import com.example.phase2.AppCoreClasses.Player;
import com.example.phase2.R;
import com.example.phase2.AppCoreClasses.User;
import com.example.phase2.AppCoreClasses.UserManager;
import com.example.phase2.stage1.MazeActivity;
import com.example.phase2.stage2.TreasureHuntActivity;
import com.example.phase2.stage3.BattleActivity;

import java.util.Iterator;
import java.util.Set;

/** An activity to select which player you want to use. */
public class SelectPlayerActivity extends SuperActivity implements View.OnClickListener, Initializable {
    private User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    /**
     * Use Iterator pattern.
     */
    public void initSpinner(){
        Spinner players = (Spinner) findViewById(R.id.players);
        Set<String> playerNamesSet = curUser.getPlayers().keySet();
        Iterator<String> playerNamesIterator = playerNamesSet.iterator();
        String[] playerNames = new String[playerNamesSet.size()];
        int curIndex = 0;
        while(playerNamesIterator.hasNext()){
            playerNames[curIndex++] = playerNamesIterator.next();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, playerNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        players.setAdapter(adapter);
        players.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            TextView stageTextView = findViewById(R.id.curStage);
            TextView propertyTextView = findViewById(R.id.property);
            TextView livesTextView = findViewById(R.id.curLives);
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                stageTextView.setText("Current at Stage: " + String.valueOf(curUser.getPlayers().get(playerNames[position]).getCurStage()));
                propertyTextView.setText(curUser.getPlayers().get(playerNames[position]).getProperty().toString());
                livesTextView.setText("Current remaining lives:" + String.valueOf(curUser.getPlayers().get(playerNames[position]).getLivesRemain()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                stageTextView.setText("");
                propertyTextView.setText("");
            }
        });
    }

    /**
     * Check whether this player could continue his/her game. If finished all games, return false. Otherwise, return true.
     * @param playerName the name of the player.
     * @return a boolean value shows whether this player could continue playing the game.
     */
    public boolean checkPlayerAvailable(String playerName){
        if (curUser.getPlayers().containsKey(playerName)){
            Player player;
            player = curUser.getPlayers().get(playerName);
            if (player.getLivesRemain() <= 0){
                Toast.makeText(this, "This player is dead.", Toast.LENGTH_LONG).show();
                return false;
            }
            else if(player.getCurStage() == 4){
                Toast.makeText(this, "This player has finished game.", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        Spinner playerNames = findViewById(R.id.players);
        String curPlayerName = null;
        if(playerNames.getSelectedItem() != null)
            curPlayerName = playerNames.getSelectedItem().toString();

        switch (v.getId()){
            case R.id.back:
                startActivity(new Intent(SelectPlayerActivity.this, ChooseOrCreatePlayerActivity.class));
                break;
            case R.id.start:
                if(curUser.getPlayers().containsKey(curPlayerName)) {
                    if (checkPlayerAvailable(curPlayerName)) {
                        curUser.setCurPlayer(curUser.getPlayers().get(curPlayerName));
                        if(curUser.getPlayers().get(curPlayerName).getCurStage() == 1)
                            startActivity(new Intent(SelectPlayerActivity.this, MazeActivity.class));
                        if(curUser.getPlayers().get(curPlayerName).getCurStage() == 2)
                            startActivity(new Intent(SelectPlayerActivity.this, TreasureHuntActivity.class));
                        if(curUser.getPlayers().get(curPlayerName).getCurStage() == 3)
                            startActivity(new Intent(SelectPlayerActivity.this, BattleActivity.class));
                    }
                }
                else{
                    Toast.makeText(this, "Please create a new player first.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void init() {
        super.init();
        setContentView(R.layout.activity_select_player);

        curUser = UserManager.getInstance().getCurUser();

        Button startButton = findViewById(R.id.start);
        Button backButton = findViewById(R.id.back);

        startButton.setOnClickListener(this);
        backButton.setOnClickListener(this);

        initSpinner();
    }
}
