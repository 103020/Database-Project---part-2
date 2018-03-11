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
        String url = "jdbc:postgresql://horton.elephantsql.com:5432/utiuguru";
        String username = "utiuguru";
        String password = "GYtteUD9M5LUYHJLGXLtREbv34OJhqZO";
        
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
            ResultSet rs = st.executeQuery("SELECT * FROM PEOPLE");
            
            PrintData(rs);
            // Closing ResultSet and Statement
            rs.close();
            st.close();
            
        } catch (Exception e) {
            // print out the exception, for debugging.
            System.out.println(e);
        }
    }
    
    static void PrintData(ResultSet rs){
        try {
            /* object used to get metadata from the ResultSet. In this case:
            ** it is used to get the amount of columns, for use in a general
            ** method for iterating through all the columns, (and getting their
            ** labels as well)
            */
            ResultSetMetaData rsmd = rs.getMetaData();
            int columns = rsmd.getColumnCount();
            
            /* A while loop, checking if there is another line in the ResultSet,
            ** and continually loops as long as this is true. 
            */
            while (rs.next()){
                // Do Work
                for (int i = 1;  i <= columns; i++) {
                    /* Usage of rsmd to get the name of the [i] column, and the 
                    ** data, and do that for each column
                    */
                    System.out.print(rsmd.getColumnName(i) + ": " 
                            + rs.getString(i) + "\t");
                }
                System.out.println("");
            }
        } catch (Exception e){
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
