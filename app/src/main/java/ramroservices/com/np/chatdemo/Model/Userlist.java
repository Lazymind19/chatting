package ramroservices.com.np.chatdemo.Model;

public class Userlist  {
    public String username,id,status;

    public Userlist() {
    }

    public Userlist(String username, String id, String status) {
        this.username = username;
        this.id = id;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
