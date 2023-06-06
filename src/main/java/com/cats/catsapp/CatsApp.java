/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.cats.catsapp;

import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author naya
 */
public class CatsApp {

    public static void main(String[] args) throws IOException {
        int optionMenu = -1;
        String[] buttons = {"1. See cats", "2. Exit"};
        
        do{
            //Main menu
            String option = (String) JOptionPane.showInputDialog(null, 
                    "Kitten Java", "Main Menu", 
                    JOptionPane.INFORMATION_MESSAGE, null,
                    buttons, buttons[0]);
            
            for(int i=0; i < buttons.length; i++){
                if(option.equals(buttons[i])){
                    optionMenu = i;
                }
            }
            
            switch(optionMenu){
                case 0:
                    CatsService.seeCats();
                    break;
                default:
                    break;
            }
            
        }while(optionMenu != 1);
    }
}
