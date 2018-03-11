package counter.strike.tournaments;

import org.postgresql.Driver; 
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author 103020
 */
public class CounterStrikeTournaments implements Main_Interface{
    static Connection db;
    static Statement st;
    /* Object used to get a result from a database. the string needs to
    ** be the SQL code for acquiring the desired set. "maintains a 
    ** cursor pointing to its current row of data."
    */     
    static ResultSet rs;
    static Scanner kb;
    static ResultSetMetaData rsmd;
    
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
            db = DriverManager.getConnection(url, username, password);
            
            /* Object used for executing a static SQL statement, and returning 
            ** the results. This object can hold one ResultSet at a time. If 
            ** more are needed, more statement objects need to be created.
            */
            st = db.createStatement();    
            
            /* Instantiates the scanner 'kb' for use for user input.
            */
            kb = new Scanner(System.in);
            /* Creates a boolean to use for continuation check for the program. */
            boolean cont = true;
            
            /* Take userinput and send it on to database server. Keeps going until
            ** the user inputs 'false'
            */
            while (cont) {
                Send(Input());
                Display(rs);
                
                System.out.println("Would you like to make another request? (true/false)");
                cont = kb.nextBoolean();   
            }
            
            // Closing ResultSet, Statement, and Scanner
            rs.close();
            st.close();
            kb.close();
            
        } catch (Exception e) {
            // print out the exception, for debugging.
            System.out.println(e);
        }
    }
    
    /* Method for allowing the user to enter what information they want from the
    ** Database, from a select few choices. Asks user for input in form of an 
    ** int, and verifies that it's correct, else it asks user to try again.
    ** Returns an int based on the choice made.
    */
    static int Input(){
        int j = 0;
        Boolean bool = true;
        while (bool) {
            System.out.println("Enter the number for the option you wish to choose:");
            System.out.println("All coaches, and the team the coach is on: 1");
            System.out.println("All people on a teams that has won at least one tournament: 2");
            System.out.println("All names of teams, and the number of players on each team: 3");
            System.out.println("All teams with more wins than input number: 4");
            j = kb.nextInt();
            if (j <= 4 && j >= 1) {
                bool = false;
            } else {
                System.out.println("Input error! Try Again");
            }
        }
        return j;
    }
    
    /**
     * Method for sending the desired SQL query, based on the input number from
     * Input() method.
     * @param j the input number from the Input() method.
     */
    public static void Send(int j) {
        try {
        switch (j){
            // All coaches, and the team the coach is on
            case 1: rs = st.executeQuery("SELECT * FROM PEOPLE INNER JOIN IN_TEAM " +
                    "ON PEOPLE.EMAIL=IN_TEAM.EMAIL WHERE PEOPLE.EMAIL IN (SELECT TEAMS.COACH FROM TEAMS)");
                break;
            // All people on a teams that has won at least one tournament
            case 2: rs = st.executeQuery("SELECT * FROM PEOPLE INNER JOIN " +
                    "IN_TEAM ON PEOPLE.EMAIL=IN_TEAM.EMAIL WHERE PEOPLE.EMAIL" +
                    " IN (SELECT IN_TEAM.EMAIL FROM IN_TEAM WHERE " +
                    "IN_TEAM.T_NAME IN (SELECT TOURNAMENTS.WINNER FROM TOURNAMENTS))");
                break;
            // All names of teams, and the number of players on each team
            case 3: rs = st.executeQuery("SELECT TEAMS.NAME, TEAMS.PLAYERS FROM TEAMS");
                break;
            // All tournaments with at least x participating teams
            case 4: {
                // creates an arraylist to hold the names of all the tournaments
                ArrayList<String> tournaments = new ArrayList<>();
                System.out.println("How many wins do you wish to filter by?");
                // variable to hold the amount of teams the user wishes to filter by
                int k = kb.nextInt();
                
                // gets al lthe names of tournaments in the database
                rs = st.executeQuery("SELECT TOURNAMENTS.NAME FROM TOURNAMENTS");
                // gets the metadata of the current ResultSet
                rsmd = rs.getMetaData();
                
                System.out.println("Here are all the tournaments with at least "
                + k + " participating teams:");
                try {
                    // Adds all the tournament names to the tournaments ArrayList
                    while (rs.next()){
                        tournaments.add(rs.getString(rsmd.getColumnCount()));                        
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                // Queries to find the amount of participating team for each tournament
                for (String n : tournaments) {
                    rs = st.executeQuery("SELECT COUNT(IN_TOUR.TOUR) FROM IN_TOUR WHERE IN_TOUR.TOUR = '" + n + "'");
                    rsmd = rs.getMetaData();
                    
                    /* This rs.next() is needed, since the courser in ResultSets
                    ** defaults to pointing at tuple 0, but the actual ResultSet
                    ** begins at tuple 1.
                    */
                    rs.next();
                    int participants = rs.getInt(rsmd.getColumnCount());
                    
                    /* prints the name of the tournament, along with the 
                    ** participant count, as long as there are more participants
                    ** than input 'k's
                    */
                    if (participants >= k) {
                        System.out.println(n + "with a participant count of " 
                                +  participants);
                    }
                }
                
                break;
            }
            default: System.out.println("Invalid input!");
        }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This is a method for displaying all the column labels, and the data in 
     * the columns, of a ResultSet.
     * @param rs The ResultSet for which all the data should be displayed
     */
    public static void Display(ResultSet rs) {
        try {
            /* object used to get metadata from the ResultSet. In this case:
            ** it is used to get the amount of columns, for use in a general
            ** method for iterating through all the columns, (and getting their
            ** labels as well)
            */
            rsmd = rs.getMetaData();
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
    
}
