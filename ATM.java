package ATM;

import java.util.Scanner;
public class ATM {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Bank theBank = new Bank("董文韬小银行");

        User aUser = theBank.addUser("文韬", "董", "111");

        Account newAccount = new Account("私房钱", aUser, theBank);
        aUser.addAccount(newAccount);
        theBank.addAccount(newAccount);

        User curUser;
        while (true) {

            //待在登录界面，直到登录成功
            curUser = ATM.mainMenuPrompt(theBank, sc);
            //待在主菜单直到用户停止
            ATM.printUserMenu(curUser, sc);
        }
    }

    public static User mainMenuPrompt(Bank theBank, Scanner sc) {
        //初始化
        String userID;
        String pin;
        User authUser;

        //提示用户输入id和密码的组合直到输入正确
        do {
            System.out.printf("\n\n欢迎来到 %s\n", theBank.getName());
            System.out.print("请输入账号:");
            userID = sc.nextLine();
            System.out.print("请输入密码:");
            pin = sc.nextLine();

            //尝试获取与ID和pin组合对应的user对象
            authUser = theBank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("\n**错误的账号密码组合 请重试**");
            }

        } while (authUser == null); //一直重复直到成功登陆

        return authUser;
    }

    public static void printUserMenu(User theUser, Scanner sc) {

        //打印用户账户的概要
        theUser.printAccountSummary();

        //初始化
        int choice;

        //用户菜单
        do {
            System.out.printf("%s先生您好, 请问您要进行什么操作?\n", theUser.getLastName());
            System.out.println("  1) 显示账户交易记录");
            System.out.println("  2) 取钱");
            System.out.println("  3) 存钱");
            System.out.println("  4) 转账");
            System.out.println("  5) 创建新账户");
            System.out.println("  6) 退出登录");
            System.out.println();
            System.out.print("您的选择是: ");
            choice = sc.nextInt();

            if (choice < 1 || choice > 6) {
                System.out.println("**选择错误，请在1-6之间选择**");
            }
        } while (choice < 1 || choice > 6);

        switch (choice) {
            case 1:
                ATM.showTransHistory(theUser, sc);
                break;
            case 2:
                ATM.withdrawFunds(theUser, sc);
                break;
            case 3:
                ATM.depositFunds(theUser, sc);
                break;
            case 4:
                ATM.transferFunds(theUser, sc);
                break;
            case 5:
                break;
            case 6:
                sc.nextLine();
                break;
        }

        //重新显示菜单除非用户想要停止
        if (choice != 6) {
            ATM.printUserMenu(theUser, sc);
        }
    }

    // 功能一 显示账户的交易历史
    public static void showTransHistory(User theUser, Scanner sc) {

        int theAcct;

        //查看账户的交易历史
        do {
            System.out.printf("请在 1-%d 中选一个账户来显示其具体的交易记录:", theUser.numAccounts());
            theAcct = sc.nextInt() - 1;
            if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
                System.out.println("**错误的账户 请重试**");
            }
        } while (theAcct < 0 || theAcct >= theUser.numAccounts());

        theUser.printAcctTransHistory(theAcct);
    }

    //转账
    public static void transferFunds(User theUser, Scanner sc) {

        //初始化
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        //获取要转钱的账户
        do {
            System.out.printf("请在 1-%d 中选一个账户发起转账:",theUser.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("**错误的账户 请重新输入**");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);


        //获取接收钱的账户
        do {
            System.out.printf("请在 1-%d 中选一个账户来接收转账:",theUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("**错误的账户 请重新输入**");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());

        //获取转移金额
        do {
            System.out.printf("请输入转账金额 (上限 $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("**金额必须大于0**");
            } else if (amount > acctBal) {
                System.out.printf("**金额不能大于账户剩余金额-- $%.02f**\n", acctBal);
            }
        } while (amount < 0 || amount > acctBal);

        //最后，开始转钱
        theUser.addAcctTransaction(fromAcct, -1 * amount, String.format("向账户%s的转账", theUser.getAcctUUID(toAcct)));
        theUser.addAcctTransaction(toAcct, amount, String.format("来自账户%s的转账", theUser.getAcctUUID(toAcct)));
    }


    //从账户中取钱
    public static void withdrawFunds(User theUser,Scanner sc) {

        //初始化
        int fromAcct;
        double amount;
        double acctBal;
        String memo;

        //获取要取钱的账户
        do {
            System.out.printf("请在 1-%d 中选一个账户取钱：" ,theUser.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= theUser.numAccounts()) {
                System.out.println("**错误的账户 请重新输入**");
            }
        } while (fromAcct < 0 || fromAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(fromAcct);

        //获取钱数
        do{
            System.out.printf("请输入取钱金额 (上限 $%.02f): $", acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("**金额必须大于0**");
            } else if (amount > acctBal) {
                System.out.printf("**金额不能大于账户剩余金额-- $%.02f**\n" , acctBal);
            }
        }while(amount< 0||amount > acctBal);

        sc.nextLine();

        System.out.print("请输入备忘录：");
        memo = sc.nextLine();


        theUser.addAcctTransaction(fromAcct,-1*amount,memo);

    }

    //将资金存入账户
    public static void depositFunds(User theUser,Scanner sc){

        //初始化
        int toAcct;
        double amount;
        double acctBal;
        String memo;

        //获取要存入的账户
        do {
            System.out.printf("请在 1-%d 中选一个账户存钱：" ,theUser.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= theUser.numAccounts()) {
                System.out.println("**错误的账户 请重新输入**");
            }
        } while (toAcct < 0 || toAcct >= theUser.numAccounts());
        acctBal = theUser.getAcctBalance(toAcct);

        //获取钱数
        do{
            System.out.printf("请输入要存入的金额: $");//这里视频没改
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("**金额必须大于0**");
            }
        }while(amount< 0);

        sc.nextLine();

        System.out.print("请输入备忘录：");
        memo = sc.nextLine();


        theUser.addAcctTransaction(toAcct,amount,memo);

    }

    //添加为用户添加新账户
//    public static void addNewAccount(){
//
//    }

}
