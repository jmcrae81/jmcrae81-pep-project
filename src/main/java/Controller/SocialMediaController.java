package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;
import Service.SocialBlogService;

import java.io.*;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    SocialBlogService sbService;

    public SocialMediaController(){
        sbService = new SocialBlogService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        
        app.post("/register", this::registerUserHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::newMessageHandler);
        app.get("/messages", this::retrieveAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
<<<<<<< HEAD
        
=======
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}",this::updateMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);

>>>>>>> 20230c3 (version 0.1)
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerUserHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = sbService.registerNewUser(account);
        
        if(newAccount == null){
            ctx.status(400);
        }else{
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(newAccount));
        }
    }

    private void loginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account validLogin = sbService.userLogin(account);

        if(validLogin == null){
            ctx.status(401);
        }else{
            ctx.json(mapper.writeValueAsString(validLogin));
            ctx.status(200);
        }
    }

    private void newMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = sbService.createMessage(message);

        if(newMessage == null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(newMessage));
            ctx.status(200);
        }
    }

    private void retrieveAllMessagesHandler(Context ctx){
        ctx.json(sbService.getAllMessages());
        ctx.status(200);
        
    }

    private void getMessageByIdHandler(Context ctx)throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper(); 
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message returnedMessage = sbService.getMessageById(id);
        
        if(returnedMessage != null){
            ctx.json(mapper.writeValueAsString(returnedMessage));
        }

        ctx.status(200);

    }

    private void deleteMessageByIdHandler(Context ctx)throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper(); 
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = sbService.deleteMessageById(id);
        
        if(deletedMessage != null){
            ctx.json(mapper.writeValueAsString(deletedMessage));
        }

        ctx.status(200);
    }
    private void updateMessageByIdHandler(Context ctx)throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message input = mapper.readValue(ctx.body(), Message.class);
        int messageNum = Integer.parseInt(ctx.pathParam("message_id"));

        Message updatedMessage = sbService.updateMessageById(messageNum, input.getMessage_text());

        if(updatedMessage == null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(updatedMessage));
            ctx.status(200);
        }
    }
    
    private void getMessagesByUserHandler(Context ctx){
        
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));

        ctx.json(sbService.getMessagesByUser(accountId));
        ctx.status(200);

    }

    private void writeToFile(String fileName, String output){

        try{
            FileWriter fw = new FileWriter(fileName, true);
            String text = output + "\n";
            fw.write(text, 0, text.length());
            fw.close();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
}
