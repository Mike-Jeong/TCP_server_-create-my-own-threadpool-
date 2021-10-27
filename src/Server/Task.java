package Server;

public class Task {

    int number;
    int client_id;

    public Task(int input_num, int client_id) {
        this.number = input_num;
        this.client_id = client_id;
    }

    public int getnumber() {
        return number;
    }

    public int getclient_id() {
        return client_id;
    }

    public void setnumber(int input_num) {
        this.number = input_num;
    }

}
