package com.example.phase2.usermanagementactivities.models;

import com.example.phase2.appcore.Player;
import com.example.phase2.appcore.Property;
import com.example.phase2.appcore.UserManager;
import com.example.phase2.appcore.Weapon;
import com.example.phase2.datamanagement.FileSystem;
import com.example.phase2.exceptions.EmptyNameException;
import com.example.phase2.exceptions.SameNameException;

public class CreatePlayerModel {

    public CreatePlayerModel(){ }

    public Property generateCareerProperty(String career) {
        switch (career) {
            case "Computer Science": {
                return new Property(20, 10, 0, 3);
            }
            case "Life Science": {
                return new Property(15, 15, 0, 3);
            }
            case "Rotman Commerce": {
                return new Property(15, 10, 5, 3);
            }
            case "Engineer": {
                return new Property(15, 10, 0, 7);
            }
        }
        return new Property(0,0,0,0);
    }

    public Property generateWeaponProperty(String weapon){
        switch (weapon){
            case "Pencil":{
                return new Property(5,0,0,0);
            }
            case "Eraser":{
                return new Property(0,5,0,0);
            }
            case "Calculator":{
                return new Property(0,0,3,0);
            }
            case "CheatSheet":{
                return new Property(0,0,0,3);
            }
        }
        return new Property(0,0,0,0);
    }

    public String createPlayer(FileSystem fileSystem, String playerName, String career, String weapon){
        Player player = new Player(playerName, generateCareerProperty(career));
        player.addWeapon(new Weapon(weapon, generateWeaponProperty(weapon)));

        try{
            UserManager.getInstance().getCurUser().addPlayer(player);
        } catch (SameNameException e) {
            e.printStackTrace();
            return "This name has been token, please change a name.";
        } catch (EmptyNameException e) {
            e.printStackTrace();
            return "Player name cannot be empty.";
        }

        fileSystem.save(UserManager.getInstance().getUsers(), "Users.ser");
        return "Successfully create player.";
    }
}
