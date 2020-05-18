package ATM;

import java.util.ArrayList;
public class Account {
    private String name;
    private String uuid;
    private User holder;
    private ArrayList<Transaction> transactions;

    public Account(String name,User holder,Bank theBank){

        this.name=name;
        this.holder=holder;

        this.uuid= theBank.getNewAccountUUId();

        this.transactions = new ArrayList<Transaction>();//初始化交易


    }

    //获取账户id
    public String getUUID(){
        return this.uuid;
    }

    //获取账户摘要
    public String getSummaryLine(){

        //获取账户余额
        double balance=this.getBalance();

        //根据余额是否为负数，用不同格式
        if(balance >=0){
            return String.format("%s : $%.02f :%s",this.uuid,balance,this.name);
        }else{
            return String.format("%s : $(%.02f) :%s",this.uuid,balance,this.name);
        }
    }

    //获取这个账户这次交易之后的余额
    public double getBalance(){
        double balance =0;
        for(Transaction t: this.transactions){
            balance +=t.getAmount();
        }
        return balance;
    }

    //打印这个账户的交易记录
    public void printTransHistory(){
        System.out.printf("\n账户%s的交易历史\n",this.uuid);
        for(int t=this.transactions.size()-1;t >= 0;t--){
            System.out.println(this.transactions.get(t).getSummaryLine());
        }
        System.out.println();
    }

    public void addTransaction(double amount,String memo){

        //创建新的交易对象并且将它加到列表中去
        Transaction newTrans = new Transaction(amount,memo,this);
        this.transactions.add(newTrans);
    }
}
