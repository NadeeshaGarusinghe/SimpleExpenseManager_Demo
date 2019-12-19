package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by lenovo on 12/19/2019.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query= "create table account( accountNo varchar(20) primary key, bankName varchar(50), accountHolderName varchar(50), balance double)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS account");
        onCreate(sqLiteDatabase);
    }

    public void insertData(Account account){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("accountNo",account.getAccountNo());
        contentValues.put("bankName",account.getBankName());
        contentValues.put("accountHolderName",account.getAccountHolderName());
        contentValues.put("balance",account.getBalance());
        long result=db.insert("account",null,contentValues);
    }

    public void removeData(String accountNumber){
        SQLiteDatabase db=this.getWritableDatabase();
        String selection = "accountNo = ?";
        String[] selectionArgs = { accountNumber };
        db.delete("account", selection, selectionArgs);
    }

    public Account getAccount(String accountNumber){
        String[] columns = {"accountNo","bankName","accountHolderName","balance"};
        String[] selectionArgs = {accountNumber};

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor = db.query("account",columns,"accountNo = ?",selectionArgs,null,null,null);

        Account account = null;
        while(cursor.moveToNext()) {
            String accountNo = cursor.getString(cursor.getColumnIndexOrThrow("accountNo"));
            String bankName = cursor.getString(cursor.getColumnIndexOrThrow("bankName"));
            String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow("accountHolderName"));
            double balance = cursor.getDouble(cursor.getColumnIndexOrThrow("balance"));

            account = new Account(accountNo, bankName, accountHolderName, balance);
        }
        cursor.close();
        return account;
    }

    public List<String> getAccountNumbersList(){

        List<String> accounNumbertList = new ArrayList<>();

        SQLiteDatabase db=this.getReadableDatabase();
        String[] columns = {"accountNo"};

        Cursor cursor = db.query("account",columns,null,null,null,null,null);

        while(cursor.moveToNext()) {
            String itemId = cursor.getString(cursor.getColumnIndexOrThrow("accountNo"));
            accounNumbertList.add(itemId);
        }

        cursor.close();
        return accounNumbertList;
    }

    public List<Account> getAccountsList(){
        List<Account> accountList = new ArrayList<>();

        SQLiteDatabase db=this.getReadableDatabase();
        String[] columns = {"accountNo","bankName", "accountHolderName","balance"};

        Cursor cursor = db.query("account",columns,null,null,null,null,null);

        while(cursor.moveToNext()) {
            String accountNo = cursor.getString(cursor.getColumnIndexOrThrow("accountNo"));
            String bankName = cursor.getString(cursor.getColumnIndexOrThrow("bankName"));
            String accountHolderName = cursor.getString(cursor.getColumnIndexOrThrow("accountHolderName"));
            double balance = cursor.getDouble(cursor.getColumnIndexOrThrow("balance"));

            Account account = new Account(accountNo, bankName, accountHolderName, balance);
            accountList.add(account);
        }

        cursor.close();
        return accountList;
    }

    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("accountNo",accountNo);
        values.put("date", date.toString());
        values.put("expenceType",expenseType.toString());
        values.put("amount",amount);

        long newRowId = db.insert("Transactions", null, values);
    }

    public List<Transaction> getAllTransactionLogs(){
        List<Transaction> transactionList = new ArrayList<>();

        SQLiteDatabase db=this.getReadableDatabase();
        String[] columns = {"date","accountNo", "expenceType","amount"};

        Cursor cursor = db.query("transaction",columns,null,null,null,null,null);

        while(cursor.moveToNext()) {
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String accountNo = cursor.getString(cursor.getColumnIndexOrThrow("accountNo"));
            String expenceType = cursor.getString(cursor.getColumnIndexOrThrow("expenceType"));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));

            ExpenseType x = null;

            if(expenceType.equals("EXPENSE")){
                x = ExpenseType.EXPENSE;
            }
            else{
                x = ExpenseType.INCOME;
            }

            Date date1= null;
            try {
                date1 = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy").parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Transaction myacc = new Transaction(date1, accountNo, x, amount);
            transactionList.add(myacc);
        }
        cursor.close();
        return transactionList;
    }

    public List<Transaction> getPaginatedTransactionLogs(int limit){
        SQLiteDatabase db=this.getReadableDatabase();

        long cnt  = DatabaseUtils.queryNumEntries(db, "Transactions");

        if(limit<=cnt){
            return getAllTransactionLogs();
        }
        else{
            String[] columns = {"date","accountNo", "expenceType","amount"};
            Cursor cursor = db.query("Transactions", columns,null,null,null,null,null);
            List<Transaction> transactions = new ArrayList<>();
            int count = 0;

            while(cursor.moveToNext() && count< limit) {

                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String accountNo = cursor.getString(cursor.getColumnIndexOrThrow("accountNo"));
                String expenceType = cursor.getString(cursor.getColumnIndexOrThrow("expenceType"));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));

                ExpenseType x = null;

                if(expenceType.equals("EXPENSE")){
                    x = ExpenseType.EXPENSE;
                }
                else{
                    x = ExpenseType.INCOME;
                }

                Date date1= null;
                try {
                    date1 = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy").parse(date);

                } catch (ParseException e) {

                    e.printStackTrace();
                }

                Transaction myacc = new Transaction(date1, accountNo, x, amount);
                transactions.add(myacc);
                count++;
            }

            cursor.close();
            return transactions;
        }
    }

    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        SQLiteDatabase db = this.getWritableDatabase();
        Account tempAccount = getAccount(accountNo);

        double value = 0;

        switch (expenseType) {
            case EXPENSE:
                value = tempAccount.getBalance() - amount;
                break;
            case INCOME:
                value = tempAccount.getBalance() + amount;
                break;
        }

        ContentValues values = new ContentValues();
        values.put("balance" , value );

        String selection = "accountNo = ?";
        String[] selectionArgs = { accountNo };

        int count = db.update(
                "Accounts",
                values,
                selection,
                selectionArgs);
    }
}
