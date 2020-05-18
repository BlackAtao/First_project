package ATM;

import java.util.Date;
public class Transaction {

    private double amount;//金额
    private Date timestamp;//时间
    private String memo;//交易记录
    private Account inAccount;//交易账户

    public Transaction(double amount,Account inAccount){

        this.amount=amount;
        this.inAccount=inAccount;
        this.timestamp=new Date();
        this.memo="";
    }

    public Transaction(double amount,String memo,Account inAccount){

        //调用上面的函数
        this(amount,inAccount);

        this.memo=memo;
    }

    //获取交易金额
    public double getAmount(){
        return this.amount;
    }

    //获取交易字符串摘要
    public String getSummaryLine(){
        if(this.amount>=0){
            return String.format("%s : +$%.02f : %s",this.timestamp.toString(),this.amount,this.memo);
        }else{
            return String.format("%s : -$%.02f : %s",this.timestamp.toString(),-this.amount,this.memo);
        }
    }
}
