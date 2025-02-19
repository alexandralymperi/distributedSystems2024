package gr.hua.dit.ds.ds2024Team77.payload;

//The MessageResponse class is used to send simple messages in response to requests.
public class MessageResponse {

    private String message; //The message to be sent in response

    //Constructor
    public MessageResponse(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(){
        this.message = message;
    }
}
