package ATM;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.security.MessageDigest;

public class User {
    private String firstName;
    private String lastName;
    private String uuid;// 用户的id
    private byte pinHash[];// 存储密码
    private ArrayList<Account> accounts;//用户的账户列表

    public User(String firstName,String lastName,String pin,Bank theBank){

        //设置用户的名字
        this.firstName = firstName;
        this.lastName = lastName;

        //为了安全，采取MD5算法
        try{
            //MD5是常用的加密算法，也经常用于校验信息完整
            //获取一个MD5转换器
            MessageDigest md = MessageDigest.getInstance("MD5");

            this.pinHash = md.digest(pin.getBytes());
        }catch (NoSuchAlgorithmException e){
            System.err.println("error,caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        //为用户创建一个新的唯一的ID
        this.uuid = theBank.getNewUserUUID();

        //创建空的账户列表
        this.accounts = new ArrayList<Account>();

        //打印创建信息
        System.out.printf("新用户%s%s的账号 %s 已创建.\n",lastName,firstName,this.uuid);

    }

    public void addAccount(Account anAcct){
        this.accounts.add(anAcct);
    }

    public String getUUID(){
        return this.uuid;
    }

    //验证密码
    public boolean validatePin(String aPin){
        try{
            MessageDigest md=MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(aPin.getBytes()),this.pinHash);
        }catch (NoSuchAlgorithmException e){
            System.err.println("error,caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

    public String getLastName(){
        return this.lastName;
    }

    //打印账户摘要
    public void printAccountSummary(){
        System.out.printf("\n\n%s先生的账户摘要\n",this.lastName);
        for(int a=0;a<this.accounts.size();a++){
            System.out.printf("%d) %s\n",a+1,this.accounts.get(a).getSummaryLine());
        }
        System.out.println();
    }

    //获取用户账户的数量
    public int numAccounts(){
        return this.accounts.size();
    }

    //打印特定帐户的交易历史记录
    public void printAcctTransHistory(int acctIdx){
        this.accounts.get(acctIdx).printTransHistory();
    }

    //获取特定账户的余额
    public double getAcctBalance(int acctIdx){
        return this.accounts.get(acctIdx).getBalance();
    }

    //获取特定账户的uuid
    public String getAcctUUID(int acctIdx){
        return this.accounts.get(acctIdx).getUUID();
    }

    //对特定账户增加一笔交易
    public void addAcctTransaction(int acctIdx,double amount, String memo){
        this.accounts.get(acctIdx).addTransaction(amount,memo);
    }
}
