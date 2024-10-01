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
        return this.getMessageById(messageId);
    }
}

