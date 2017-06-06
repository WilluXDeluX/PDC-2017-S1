/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Game;

//import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Callum
 */
public class GameDataBace
{
    Connection conn = null;
    String url;
    String username;
    String password;
    Statement statement;
    ResultSet re;
    
    public GameDataBace() {
        url = "jdbc:derby:GameDB;";
        username = "pdc";
        password ="pdc";
    }
    
    public void autoConnectGame()
    {
        try 
        {
            conn = DriverManager.getConnection(url, username, password);
            statement = conn.createStatement();
            
        } catch (SQLException ex)
        {
          System.err.println("SQL Exception!!!: " + ex.getMessage());   
        }
        
        
    }
    
    public static void main(String[] args) {
        GameDataBace start = new GameDataBace();
        start.autoConnectGame();
        String name;
        String stats;
        String monsters;
        int damageClass;
        int currentRoom;
        
        ResultSet rs;
        String sql = "SELECT * FROM SAVEGAME";
        String getCount = "SELECT COUNT(*) FROM SAVEGAME";
        String changeValues = "UPDATE SAVEGAME SET column = 'value' WHERE PLAYERNAME IS null";
        try {
            rs = start.statement.executeQuery(getCount);
            rs.next();
            System.out.println(rs.getInt(1));
            
            rs = start.statement.executeQuery(sql);
            
            while(rs.next())
            {
                name = rs.getString(1);
                stats = rs.getString(2);
                damageClass = rs.getInt(3);
                currentRoom = rs.getInt(4);
                monsters = rs.getString(5);
                System.out.println(name + "\n" + stats + "\n" + damageClass + "\n" + currentRoom + "\n" + monsters);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GameDataBace.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
}
