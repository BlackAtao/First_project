package ATM;

import java.util.ArrayList;
import java.util.Random;
public class Bank {
    private String name;
    private ArrayList<User> users;
    private ArrayList<Account> accounts;

    public Bank(String name){
        this.name=name;
        this.users=new ArrayList<User>();
        this.accounts=new ArrayList<Account>();
    }

    public String getNewUserUUID(){
        //初始化
                String uuid;
                Random rng= new Random();
                int len=6;
                boolean nonUnique = false;

                //一直循环直到找到唯一的ID
                do{

                    //生成号码
                    uuid="";
                    for(int c=0;c<len;c++){
                        uuid+=((Integer)rng.nextInt(10)).toString();
                    }

                    //确保唯一
                    nonUnique = false;
                    for(User u: this.users){
                        if(uuid.compareTo(u.getUUID()) ==0){
                            nonUnique = true;
                            break;
                        }
                    }

                }while(nonUnique);

                return uuid;

    }
    public String getNewAccountUUId(){
        //初始化
        String uuid;
        Random rng= new Random();
        int len=10;
        boolean nonUnique = false;

        //一直循环直到找到唯一的ID
        do{

            //生成号码
            uuid="";
            for(int c=0;c<len;c++){
                uuid+=((Integer)rng.nextInt(10)).toString();
            }

            //确保唯一
            nonUnique = false;
            for(Account a: this.accounts){
                if(uuid.compareTo(a.getUUID()) ==0){
                    nonUnique = true;
                    break;
                }
            }

        }while(nonUnique);

        return uuid;
    }

    public void addAccount(Account anAcct){
        this.accounts.add(anAcct);
    }

    public User addUser(String firstName,String lastName,String pin){

        User newUser =new User(firstName,lastName,pin,this);
        this.users.add(newUser);

        Account newAccount = new Account("工资",newUser,this);
        newUser.addAccount(newAccount);
        this.addAccount(newAccount);

        return newUser;
    }

    public User userLogin(String userID,String pin){

        //遍历用户列表
        for(User u: this.users){
            //检测用户id是否正确
            if(u.getUUID().compareTo(userID)==0&&u.validatePin(pin)){
                return u;
            }
        }

        //如果找不到用户或者密码错误
        return null;
    }

    public String getName(){
        return this.name;
    }
}
