package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.*;
import java.io.*;

public class SocialBlogDAO{
    public Account registerNewUser(Account newAccount){
        // Call the testValidity method first to make sure we can add
        // this account.
        String name = newAccount.getUsername();
        String pass = newAccount.getPassword();
       
        boolean validAccount = testAccountValidity(name, pass);

        if(validAccount){
            try{
                
                Connection connection = ConnectionUtil.getConnection();
                String sql = "INSERT INTO Account(username, password) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, name);
                preparedStatement.setString(2, pass);
                preparedStatement.executeUpdate();
              
                ResultSet rs = preparedStatement.getGeneratedKeys();

                if(rs.next()){
                    int account_id = (int) rs.getLong(1);
                    return new Account(account_id, name, pass);
                }
            }catch(SQLException e){ 
                System.out.println(e.getMessage());
            }
        }
        
       return null; 
    }

    public Account userLogin(Account user){
        String query = "SELECT * FROM Account";
        Connection connection = ConnectionUtil.getConnection();
        try{
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            String rsName = new String();
            String rsPass = new String();
            int rsAccount_id = 0;

            while(rs.next()){
               rsName = rs.getString("username"); 
               rsPass = rs.getString("password");
               rsAccount_id = rs.getInt("account_id");
               

               if(rsName.equals(user.getUsername()) && rsPass.equals(user.getPassword())){
                   return new Account(rsAccount_id, rsName, rsPass);
               }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message createMessage(Message message){
        int postedBy = message.getPosted_by();
        String text = message.getMessage_text();
        long timePosted = message.getTime_posted_epoch();

        if(!isMessageValid(message)){
            return null;
        }

        try{
            String sql = "INSERT INTO Message(posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            Connection connection = ConnectionUtil.getConnection();

            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, postedBy);
            ps.setString(2, text);
            ps.setLong(3, timePosted);

            ps.executeUpdate();

            ResultSet rsKeys = ps.getGeneratedKeys();

            if(rsKeys.next()){
                int message_id = (int) rsKeys.getLong(1);
                return new Message(message_id, postedBy, text, timePosted);
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;

    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        int messageId = 0;
        int postedBy = 0;
        String text = new String();
        long time = 0;

        try{
            String query = "SELECT * FROM Message";
            PreparedStatement ps = connection.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                messageId = rs.getInt("message_id");
                postedBy = rs.getInt("posted_by");
                text = rs.getString("message_text");
                time = rs.getLong("time_posted_epoch");

                Message msg = new Message(messageId, postedBy, text, time);
                messages.add(msg);
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return messages;
    }   
    
    public Message getMessageById(int messageId){
        Connection connection = ConnectionUtil.getConnection();

        try{
            String query = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, messageId);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message requestedMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return requestedMessage;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
    }

    private boolean testAccountValidity(String name, String pass){
        // Username cannot be blank
        if(name.equals(null) || name.length() == 0){
           return false;
        }

        // Password must be at least 4 characters long
        if(pass.length() < 4){
            return false;
        }

        Connection connection = ConnectionUtil.getConnection();

        // Can't use a Username that's already been taken
        try{
            String sql ="SELECT username FROM Account;";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery(); 
            String currentUsername = new String();

            while(rs.next()){
                currentUsername = rs.getString("username");

                if(currentUsername.equals(name)){
                    return false;
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return true;
    }

    private boolean isMessageValid(Message message){
        int postedBy = message.getPosted_by();
        String text = message.getMessage_text();
        // Is the message blank or greater than 255 characters?
        // If so, reject
        if(text.equals("") || text.length() > 255){
            return false;
        }
        
        // Make sure message was posted by a real, existing user
        try{
            Connection connection = ConnectionUtil.getConnection();
            String query = "SELECT posted_by FROM Message";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
               if(postedBy == rs.getInt("posted_by")){
                   return true;
               }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return true;
    }

    public void queryTable(){

        Connection connection = ConnectionUtil.getConnection();
        String currentAccounts = new String();
        String query = "SELECT * FROM Account";
        
        try{
            FileWriter fw = new FileWriter("query.txt", true);
            
            PreparedStatement psQuery = connection.prepareStatement(query);
            ResultSet rs = psQuery.executeQuery();

            String rsName = "";
            String rsPass = "";
            int rsAccountID = 0;
            int rsRowNum = 0;

            while(rs.next()){
                rsRowNum = rs.getRow();
                rsAccountID = rs.getInt("account_id");
                rsName = rs.getString("username");
                rsPass = rs.getString("password");

                currentAccounts = "Row: " + rsRowNum + " Account: " + rsAccountID + " Name: " + 
                    rsName + " Pass: " + rsPass + "\n";
                   
                fw.write(currentAccounts, 0, currentAccounts.length());
        }

        fw.close();
        }catch(IOException e){
            System.out.println(e);
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    public void writeToFile(String fileName, String inputToFile){
        String result = new String();

        try{
            FileWriter writer = new FileWriter(fileName, true);
            result = inputToFile + "\n";

            writer.write(result);
            writer.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }

            
            

}

