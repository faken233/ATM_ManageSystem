import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ATM {
    private ArrayList<Account> accountArrayList = new ArrayList<>();
    Scanner in = new Scanner(System.in);
    private Account loginAccount;
    public void start()// 启动ATM
    {
        while (true) {
        System.out.println("欢迎您进入ATM!");
        System.out.println("1.用户登陆");
        System.out.println("2.用户开户");
        System.out.println("请选择:");
        int command = in.nextInt();


            switch (command)
            {
                case 1:
                    //用户登录
                    login();
                    break;
                case 2:
                    //用户开户
                    createAccount();
                    break;
                case 3:
                    System.out.println("感谢使用ATM,下次再见");
                    System.exit(1);
                default:
                    System.out.println("没有此操作!");
            }
        }
    }
    private void login()
    {
        System.out.println("==系统登陆==");
        if(accountArrayList.isEmpty())
        {
            System.out.println("系统无账户,不允许登陆!");
            return;
        }

        while (true) {
            System.out.println("请输入卡号");
            String cardID = in.next();
            Account account = getAccountByCardId(cardID);
            if( account == null)
            {
                System.out.println("您输入的卡号不存在,请确认");
            }
            else
            {
                while (true) {
                    System.out.println("请输入密码");
                    String password = in.next();
                    if( account.getPassword().equals(password) )
                    {
                        System.out.println(account.getUserName() + "登陆成功,卡号" + account.getCardID());
                        loginAccount = account;
                        showUserCommand();
                        return;
                    }
                    else
                    {
                        System.out.println("您输入的密码不正确,请重试!");
                    }
                }
            }
        }
    }
    private void createAccount()//开户
    {
        Account account = new Account();

        System.out.println("输入您的账户名称");
        account.setUserName(in.next());

        while (true) {
            System.out.println("请输入您的性别");
            char sex = in.next().charAt(0);
            if( sex == '男' || sex == '女')
            {
                account.setSex(sex);
                break;
            }
            else
            {
                System.out.println("您输入了一个无效性别");
            }
        }

        while (true) {
            System.out.println("请输入您的账户密码");
            String password = in.next();
            System.out.println("请再次输入您的账户密码");
            String password1 = in.next();
            if(password1.equals(password))
            {
                account.setPassword(password);
                break;
            }
            else
            {
                System.out.println("两次密码收入不一致!");
            }
        }

        System.out.println("请设置您的限额");
        account.setLimit(in.nextDouble());

        account.setCardID(createCardID());

        accountArrayList.add(account);

        System.out.println("恭喜您," + account.getUserNameFormally() + "开户成功, 您的卡号为" + account.getCardID());
    }
    private void showUserCommand()
    {
        while (true) {
            System.out.println(loginAccount.getUserNameFormally() + ",请选择要执行的业务");
            System.out.println("1.查询");
            System.out.println("2.存款");
            System.out.println("3.取款");
            System.out.println("4.转账");
            System.out.println("5.密码修改");
            System.out.println("6.退出");
            System.out.println("7.注销当前账户");
            int command = in.nextInt();
            switch(command)
            {
                case 1:
                    showLoginAccount();
                    break;
                case 2:
                    depositMoney();
                    break;
                case 3:
                    drawMoney();
                    break;
                case 4:
                    transferAccount();
                    break;
                case 5:
                    PasswordEdit();
                    break;
                case 6:
                    System.out.println(loginAccount.getUserName() + "已退出系统");
                    return;
                case 7:
                    if (deleteAccount())
                    {
                        return;
                    }
                    else
                    {
                        break;
                    }

                default:
                    System.out.println("无效业务!");
            }
        }
    }

    private boolean deleteAccount() {
        System.out.println("您确定要注销您的账户吗?1--yes/2--no");
        int key = in.nextInt();
        if (key == 1)
        {
            while (true) {
                System.out.println("请输入密码以确定操作");
                String password = in.next();
                if (password.equals(loginAccount.getPassword()))
                {
                    accountArrayList.remove(loginAccount);
                    System.out.println("已经注销账户,现在返回登录界面");
                    return true;
                }
                else
                {
                    System.out.println("密码错误,是否重新输入?1--yes/2--no");
                    int key1 = in.nextInt();
                    if (key1 == 2)
                    {
                        System.out.println("取消注销,返回主页面");
                        return false;
                    }
                }
            }
        }
        else
        {
            System.out.println("取消注销,返回主页面");
            return false;
        }
    }

    private void PasswordEdit() {
        while (true) {
            System.out.println("请输入当前密码");
            String formerPassword = in.next();
            if (formerPassword.equals(loginAccount.getPassword()))
            {
                System.out.println("请输入新密码");
                String newPassword = in.next();
                System.out.println("请再次输入新密码");
                if (newPassword.equals(in.next()))
                {
                    loginAccount.setPassword(newPassword);
                    System.out.println("成功修改密码!");
                    return;
                }
            }
            else
            {
                System.out.println("密码不正确,是否再次输入?1--yes/2--no");
                int key = in.nextInt();
                if (key == 2)
                {
                    break;
                }
            }
        }
    }

    private void transferAccount() {
        //1.判断是否有两个以上账户
        if (accountArrayList.size() < 2)
        {
            System.out.println("系统中账户不足两个,无法进行转账!");
            return;
        }
        //2.判断账户余额是否为0
        if (loginAccount.getMoney()==0)
        {
            System.out.println("账户余额为零!");
            return;
        }
        //3.开始转账
        while (true) {
            System.out.println("请输入你要转账的对象的卡号:");
            String toAccountCardID = in.next();
            Account toAccount = getAccountByCardId(toAccountCardID);
            if (toAccount == null)
            {
                //不存在账户
                System.out.println("未找到此账户!您还要继续转账吗?1--yes/2--no");
                int key = in.nextInt();
                if (key == 2)
                {
                    break;
                }
            }
            else
            {
                //存在账户,确定名字
                System.out.println("请输入转账的对象户主姓名进行确认");
                String toAccountUserName = in.next();
                if (toAccountUserName.equals(toAccount.getUserName()))
                {
                    //确认名字,开始转账
                    //现有两个账户:loginAcc和toAcc
                    System.out.println("成功确认转账对象");
                    while (true) {
                        System.out.println("请输入转账金额");
                        double money = in.nextDouble();
                        //与取款逻辑相同
                        if ( money <= loginAccount.getMoney())
                        {
                            loginAccount.setMoney(loginAccount.getMoney() - money);
                            toAccount.setMoney(toAccount.getMoney() + money);
                            System.out.println("成功转账!您当前余额为" + loginAccount.getMoney() + "元,成功转账" + money + "元");
                            System.out.println("正在返回主界面");
                            return;
                        }
                        else
                        {
                            System.out.println("取款过多!您要修改转账金额吗?1--yes/2--no");
                            int key = in.nextInt();
                            if (key == 2)
                            {
                                break;
                            }
                        }
                    }
                }
                else
                {
                    System.out.println("名字错误!您还要继续转账吗?1--yes/2--no");
                    int key = in.nextInt();
                    if (key == 2)
                    {
                        break;
                    }
                }
            }
        }
    }

    private void drawMoney() {
        if( loginAccount.getMoney() <= 100.0)
        {
            System.out.println("当前余额少于100元,不允许取钱!");
            return;
        }
        while (true) {
            System.out.println("==正在取钱==");
            System.out.println("请输入取款金额:");
            double drawMoney = in.nextDouble();
            if(drawMoney <= loginAccount.getMoney())
            {
                if (drawMoney <= loginAccount.getLimit())
                {
                    loginAccount.setMoney(loginAccount.getMoney() - drawMoney);
                    System.out.println("已取款" + drawMoney + "元,现余额为" + loginAccount.getMoney() + "元");
                    System.out.println("您还要继续取钱吗?1--yes/2--no");
                    int key = in.nextInt();
                    if (key == 2)
                    {
                        break;
                    }
                }
                else
                {
                    System.out.println("单次取款超限额!");
                    System.out.println("您还要继续取钱吗?1--yes/2--no");
                    int key = in.nextInt();
                    if (key == 2)
                    {
                        break;
                    }
                }
            }
            else
            {
                System.out.println("余额不足!");
                System.out.println("您还要继续取钱吗?1--yes/2--no");
                int key = in.nextInt();
                if (key == 2)
                {
                    break;
                }
            }
        }
    }

    private void depositMoney() {
        System.out.println("==正在存款==");
        System.out.println("请输入存款金额");
        double money = in.nextDouble();
        loginAccount.setMoney(money + loginAccount.getMoney());
        System.out.println("成功存钱,现账户余额:" + loginAccount.getMoney());
    }

    private void showLoginAccount() {
        System.out.println("==当前登陆账户信息如下==");
        System.out.println("户主:" + loginAccount.getUserName());
        System.out.println("卡号:" + loginAccount.getCardID());
        System.out.println("性别:" + loginAccount.getSex());
        System.out.println("余额:" + loginAccount.getMoney());
        System.out.println("每次取现额度:" + loginAccount.getLimit());
    }

    private String createCardID()//生成一个随机的八位数卡号
    {
        while (true) {
            Random random = new Random();
            String cardID = "";
            for (int i = 0; i < 8; i++) {
                int data = random.nextInt(10);
                cardID += data;
            }
            Account acc = getAccountByCardId(cardID);
            if(acc == null)return cardID;
        }
    }

    private Account getAccountByCardId(String cardID)//根据卡号寻找账户
    {
        for (Account account : accountArrayList) {
            if (account.getCardID().equals(cardID))
                return account;
        }
        return null;
    }
}
