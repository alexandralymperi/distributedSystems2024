package gr.hua.dit.ds.ds2024Team77.payload;

public class MessageResponse {

    private String message;

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
