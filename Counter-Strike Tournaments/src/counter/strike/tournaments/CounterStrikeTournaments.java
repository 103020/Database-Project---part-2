package counter.strike.tournaments;

import org.postgresql.Driver; 
import java.sql.*;

/**
 * @author 103020
 */
public class CounterStrikeTournaments implements Main_Interface{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Address, username, and password for the SQL database.
        String url = "";
        String username = "";
        String password = "";
        
        try {
            /* Establish a connection with the database, using the address,
            ** username, and password. 
            */
            Connection db = DriverManager.getConnection(url, username, password);
            
            /* Object used for executing a static SQL statement, and returning 
            ** the results. This object can hold one ResultSet at a time. If 
            ** more are needed, more statement objects need to be created.
            */
            Statement st = db.createStatement();
            
            /* Object used to get a result from a database. the string needs to
            ** be the SQL code for acquiring the desired set. "maintains a 
            ** cursor pointing to its current row of data."
            */            
            ResultSet rs = st.executeQuery("");
            
            /* A while loop, checking if there is another line in the ResultSet,
            ** and continually loops as long as this is true. 
            */
            while (rs.next()){
                // Do Work
            }
            // Closing ResultSet and Statement
            rs.close();
            st.close();
            
        } catch (Exception e) {
            // print out the exception, for debugging.
            System.out.println(e);
        }
    }

    @Override
    public void Send(String s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void Display() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
