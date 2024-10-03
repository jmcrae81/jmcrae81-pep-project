package Service;

import DAO.SocialBlogDAO;
import Model.Account;
import Model.Message;
import java.util.List;

public class SocialBlogService{

    SocialBlogDAO socialBlogDAO;

    public SocialBlogService(){
        
        socialBlogDAO = new SocialBlogDAO();
    }

    public Account registerNewUser(Account account){
        return this.socialBlogDAO.registerNewUser(account);
    }

    public Account userLogin(Account user){
        return this.socialBlogDAO.userLogin(user);
    }

    public Message createMessage(Message message){
        return this.socialBlogDAO.createMessage(message);
    }

    public List<Message> getAllMessages(){
        return this.socialBlogDAO.getAllMessages();
    }
    
    public Message getMessageById(int messageId){
        return this.socialBlogDAO.getMessageById(messageId);
    }

    public Message deleteMessageById(int messageId){
        return this.socialBlogDAO.deleteMessageById(messageId);
    }

    public Message updateMessageById(int messageNum, String messageText){
        return this.socialBlogDAO.updateMessageById(messageNum, messageText);
    }
    
    public List<Message> getMessagesByUser(int accountId){
        return this.socialBlogDAO.getMessagesByUser(accountId);
    }
}

